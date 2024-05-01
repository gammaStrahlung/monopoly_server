package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.Game;
import at.gammastrahlung.monopoly_server.game.Player;
import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import at.gammastrahlung.monopoly_server.network.dtos.ClientMessage;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;

public class MonopolyMessageHandler {

    private static final Gson gson =  new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    public static void handleMessage(ClientMessage clientMessage, WebSocketSession session) {
        ServerMessage response;

        List<Player> recievers = new ArrayList<>();

        // Set player to player from server if it exists
        WebSocketPlayer p = WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId());
        if (p != null)
            clientMessage.setPlayer(p);

        Game currentGame = clientMessage.getPlayer().getCurrentGame();
        if (currentGame != null)
            recievers.addAll(currentGame.getPlayers());
        else
            recievers.add(clientMessage.getPlayer());

        // Call the different Message Handlers
        try {
            response = switch (clientMessage.getMessagePath()) {
                case "create" -> {
                    clientMessage.getPlayer().setWebSocketSession(session); // Needed for player WebSocketSession tracking
                    yield createGame(clientMessage.getPlayer());
                }
                case "join" -> {
                    clientMessage.getPlayer().setWebSocketSession(session); // Needed for player WebSocketSession tracking
                    var message = joinGame(Integer.parseInt(clientMessage.getMessage()), clientMessage.getPlayer());

                    // Update recievers to all players
                    recievers.clear();
                    recievers.addAll(clientMessage.getPlayer().getCurrentGame().getPlayers());
                    yield message;
                }
                case "players" -> getPlayers(clientMessage.getPlayer());
                case "start" -> startGame(WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId()));
                case "end" -> endGame(WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId()));
                case "roll_dice" -> handleRollDice(clientMessage, WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId()));
                default -> throw new IllegalArgumentException("Invalid MessagePath");
            };
        } catch (Exception e) {
            response = ServerMessage.builder()
                    .messagePath(clientMessage.getMessagePath())
                    .player(clientMessage.getPlayer())
                    .message(e.getMessage())
                    .type(ServerMessage.MessageType.ERROR)
                    .build();

            WebSocketSender.sendToPlayer(session, response);
            return;
        }

        WebSocketSender.sendToPlayers(response, recievers);
    }

    /**
     * Called by the client to create a new game.
     *
     * @param player The player creating the game
     * @return ServerMessage that contains the GameId, that can be used by other clients to join the game.
     */
    public static ServerMessage createGame(WebSocketPlayer player) {

        // Create a new game
        Game game = new Game();

        // Player that creates the game should also join the game
        game.join(player);

        return new ServerMessage("create",
                ServerMessage.MessageType.SUCCESS,
                String.valueOf(game.getGameId()), player, game);
    }

    /**
     * Called by the client to join a new game or to re-join after connection loss.
     *
     * @param gameId The gameId of the game, the player wants to join
     * @param player The player that wants to join the game.
     * @return ServerMessage with MessageType SUCCESS containing the GameId as the Message if joining was successful,
     * else ServerMessage has MessageType ERROR.
     */
    public static ServerMessage joinGame(int gameId, WebSocketPlayer player) {

        // Try to join the game
        Game game = Game.joinByGameId(gameId, player);

        // Joining the game was unsuccessful
        if (game == null)
            return new ServerMessage("join", ServerMessage.MessageType.ERROR, "", player, null);

        return new ServerMessage("join",
                ServerMessage.MessageType.SUCCESS,
                String.valueOf(game.getGameId()), player, game);
    }

    /**
     * Returns all Players playing the same game as the client.
     *
     * @param player The player calling.
     * @return ServerMessage containing an array of Players.
     */
    public static ServerMessage getPlayers(WebSocketPlayer player) {
        try {
            var players = player.getCurrentGame().getPlayers().toArray();

            return new ServerMessage("players",
                    ServerMessage.MessageType.SUCCESS,
                    gson.toJson(players),
                    player, null);

        } catch (Exception e) {
            return new ServerMessage("players",
                    ServerMessage.MessageType.ERROR,
                    "",
                    player, null);
        }
    }

    /**
     * Called by the client to start the current game
     *
     * @param player The player that wants to start the game.
     * @return ServerMessage with MessageType SUCCESS as the Message if starting the game was successful,
     * else ServerMessage has MessageType ERROR.
     */
    public static ServerMessage startGame(WebSocketPlayer player) {
        Game game = player.getCurrentGame();

        if (game.startGame(player)) {
            return new ServerMessage("start",
                    ServerMessage.MessageType.SUCCESS,
                    "",
                    player,
                    game);
        } else {
            return new ServerMessage("start",
                    ServerMessage.MessageType.ERROR,
                    "",
                    player,
                    game);
        }
    }

    /**
     * Called by the client to end the current game
     *
     * @param player The player that wants to end the game.
     * @return ServerMessage with MessageType SUCCESS as the Message if ending the game was successful,
     * else ServerMessage has MessageType ERROR.
     */
    public static ServerMessage endGame(WebSocketPlayer player) {
        Game game = player.getCurrentGame();

        if (game.endGame(player)) {
            return new ServerMessage("end",
                    ServerMessage.MessageType.SUCCESS,
                    "",
                    player,
                    game);
        } else {
            return new ServerMessage("end",
                    ServerMessage.MessageType.ERROR,
                    "",
                    player,
                    game);
        }
    }

    public static ServerMessage handleRollDice(ClientMessage clientMessage, WebSocketPlayer player) {
        Game game = player.getCurrentGame();

        return ServerMessage.builder()
                .messagePath("roll_dice")
                .type(ServerMessage.MessageType.INFO)
                .message(clientMessage.getMessage()) // Send the dice values as the message
                .player(clientMessage.getPlayer())
                .game(game)
                .build();
    }
}
