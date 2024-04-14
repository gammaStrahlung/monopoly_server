package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.Game;
import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import at.gammastrahlung.monopoly_server.network.dtos.ClientMessage;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.socket.WebSocketSession;

public class MonopolyMessageHandler {

    private static final Gson gson =  new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

    public static void handleMessage(ClientMessage clientMessage, WebSocketSession session) {
        ServerMessage response;

        // Call the different Message Handlers
        try {
            response = switch (clientMessage.getMessagePath()) {
                case "create" -> {
                    clientMessage.getPlayer().setWebSocketSession(session); // Needed for player WebSocketSession tracking
                    yield createGame(clientMessage.getPlayer());
                }
                case "join" -> {
                    clientMessage.getPlayer().setWebSocketSession(session); // Needed for player WebSocketSession tracking
                    yield joinGame(Integer.parseInt(clientMessage.getMessage()),
                            clientMessage.getPlayer());
                }
                case "players" -> {
                    clientMessage.setPlayer(WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId()));
                    yield getPlayers(clientMessage.getPlayer());
                }
                case "start" -> startGame(WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId()));
                case "end" -> endGame(WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId()));
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

        WebSocketSender.sendToAllGamePlayers(session, response);
    }

    /**
     * Called by the client to create a new game.
     *
     * @param player The player creating the game
     * @return ServerMessage that contains the GameId, that can be used by other clients to join the game.
     */
    public static ServerMessage createGame(WebSocketPlayer player) {

        // Create a new game
        Game g = new Game();

        // Player that creates the game should also join the game
        g.join(player);

        return new ServerMessage("create",
                ServerMessage.MessageType.SUCCESS,
                String.valueOf(g.getGameId()), player);
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
        Game g = Game.joinByGameId(gameId, player);

        // Joining the game was unsuccessful
        if (g == null)
            return new ServerMessage("join", ServerMessage.MessageType.ERROR, "", player);

        return new ServerMessage("join",
                ServerMessage.MessageType.SUCCESS,
                String.valueOf(g.getGameId()), player);
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
                    player);

        } catch (Exception e) {
            return new ServerMessage("players",
                    ServerMessage.MessageType.ERROR,
                    "",
                    player);
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
        Game g = player.getCurrentGame();

        if (g.startGame(player)) {
            return new ServerMessage("start",
                    ServerMessage.MessageType.SUCCESS,
                    "",
                    player);
        } else {
            return new ServerMessage("start",
                    ServerMessage.MessageType.ERROR,
                    "",
                    player);
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
        Game g = player.getCurrentGame();

        if (g.endGame(player)) {
            return new ServerMessage("end",
                    ServerMessage.MessageType.SUCCESS,
                    "",
                    player);
        } else {
            return new ServerMessage("end",
                    ServerMessage.MessageType.ERROR,
                    "",
                    player);
        }
    }
}
