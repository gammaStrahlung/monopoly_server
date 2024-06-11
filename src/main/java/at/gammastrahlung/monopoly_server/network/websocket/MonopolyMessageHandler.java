package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.*;
import at.gammastrahlung.monopoly_server.game.gameboard.*;
import at.gammastrahlung.monopoly_server.network.dtos.ClientMessage;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import at.gammastrahlung.monopoly_server.network.json.FieldSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.web.socket.WebSocketSession;

import java.lang.reflect.Type;
import java.util.List;

import java.util.ArrayList;
import java.util.Map;

public class MonopolyMessageHandler {

    private MonopolyMessageHandler() {}

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Field.class, new FieldSerializer())
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public static void handleMessage(ClientMessage clientMessage, WebSocketSession session) {
        ServerMessage response;

        List<Player> receivers = new ArrayList<>();

        // Set player to player from server if it exists
        WebSocketPlayer p = WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId());
        if (p != null)
            clientMessage.setPlayer(p);

        Game currentGame = clientMessage.getPlayer().getCurrentGame();
        if (currentGame != null)
            receivers.addAll(currentGame.getPlayers());
        else
            receivers.add(clientMessage.getPlayer());

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

                    // Send player update if the join was successful
                    if (message.getType() == ServerMessage.MessageType.SUCCESS) {
                        WebSocketSender.sendToPlayers(
                                generateUpdateMessage(
                                        ServerMessage.MessageType.INFO,
                                        message.getPlayer()
                                ),
                                message.getPlayer().getCurrentGame().getPlayers()
                        );
                    }

                    yield message;
                }
                case "start" -> startGame(clientMessage.getPlayer());
                case "end" -> endGame(clientMessage.getPlayer());
                case "roll_dice" -> rollDice(clientMessage, clientMessage.getPlayer());
                case "initiate_round" -> initiateRound(clientMessage.getPlayer());
                case "end_current_player_turn" -> endCurrentPlayerTurn(clientMessage.getPlayer());
                case "move_avatar" -> movePlayer(clientMessage, clientMessage.getPlayer());
                case "move_avatar_cheating" ->
                        moveCheatingPlayer(clientMessage, clientMessage.getPlayer());
                case "cheating" -> cheating(Integer.parseInt(clientMessage.getMessage()), clientMessage.getPlayer());
                case "game_state" -> {
                    clientMessage.getPlayer().setWebSocketSession(session);
                    yield gameState(Integer.parseInt(clientMessage.getMessage()), clientMessage.getPlayer());
                }
                case "checkCurrentField"    -> checkCurrentField(clientMessage);
                default -> throw new IllegalArgumentException("Invalid MessagePath");
            };
        } catch (Exception e) {
            response = ServerMessage.builder()
                    .messagePath(clientMessage.getMessagePath())
                    .player(clientMessage.getPlayer())
                    .type(ServerMessage.MessageType.ERROR)
                    .build();

            WebSocketSender.sendToPlayer(session, response);
            return;
        }

        WebSocketSender.sendToPlayers(response, receivers);

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

        // Create the logger and pass the game
        GameLogger gameLogger = new WebSocketGameLogger(game);
        game.setLogger(gameLogger);

        // Add disconnect notifier (updates game for other players)
        game.setDisconnectNotifier(player1 -> {
            if (player1.getCurrentGame() == null)
                return;

            ServerMessage message = ServerMessage.builder()
                    .messagePath("initiate_round")
                    .player(player)
                    .jsonData(gson.toJson(player1.getCurrentGame().getCurrentPlayer()))
                    .build();

            WebSocketSender.sendToPlayers(message, player1.getCurrentGame().getPlayers());
        });

        // Player that creates the game should also join the game
        game.join(player);

        return ServerMessage.builder()
                .type(ServerMessage.MessageType.SUCCESS)
                .messagePath("create")
                .jsonData(gson.toJson(game))
                .player(player)
                .build();
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
        if (game == null) {
            return ServerMessage.builder()
                    .type(ServerMessage.MessageType.ERROR)
                    .messagePath("join")
                    .player(player)
                    .build();
        } else {
            // Used when re-joining as the old player object gets reused
            WebSocketPlayer newPlayer = (WebSocketPlayer) game.getPlayers().stream().filter(player::equals).findFirst().orElseThrow();

            return ServerMessage.builder()
                    .type(ServerMessage.MessageType.SUCCESS)
                    .messagePath("join")
                    .jsonData(gson.toJson(game))
                    .player(newPlayer)
                    .build();
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

        if (game.startGame(player))
            return generateUpdateMessage(ServerMessage.MessageType.SUCCESS, game.getState());
        else
            return generateUpdateMessage(ServerMessage.MessageType.ERROR, game.getState());
    }

    private static ServerMessage initiateRound(WebSocketPlayer player) {
        Game game = player.getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();

        ServerMessage message = ServerMessage.builder()
                .messagePath("initiate_round")
                .type(ServerMessage.MessageType.INFO)
                .jsonData(gson.toJson(currentPlayer))
                .build();

        WebSocketSender.sendToPlayers(message, game.getPlayers());

        return message;
    }

    public static ServerMessage endGame(WebSocketPlayer player) {
        Game game = player.getCurrentGame();

        if (game.endGame(player))
            return generateUpdateMessage(ServerMessage.MessageType.SUCCESS, game.getState());
        else
            return generateUpdateMessage(ServerMessage.MessageType.ERROR, game.getState());
    }

    /**
     * Generates an update message based on the type of the updateObject
     *
     * @param messageType  the type of the message
     * @param updateObject The Object that should be updated
     * @return ServerMessage wih the given type and a updateType matching the type of the updateObject
     */
    private static ServerMessage generateUpdateMessage(ServerMessage.MessageType messageType, Object updateObject) {
        var message = ServerMessage.builder()
                .type(messageType)
                .messagePath("update")
                .jsonData(gson.toJson(updateObject));

        if (updateObject instanceof Field) { // Field update
            message.updateType("field");
        } else if (updateObject instanceof Player) { // Player update
            message.updateType("player");
        } else if (updateObject instanceof GameBoard) { // GameBoard update
            message.updateType("gameboard");
        } else if (updateObject instanceof Game.GameState) { // Game state update
            message.updateType("gamestate");
        } else if (updateObject instanceof Game) { // Full game update, includes all other types. Should be used as little as possible.
            message.updateType("game");
        } else {
            throw new IllegalArgumentException();
        }

        return message.build();
    }

    private static ServerMessage rollDice(ClientMessage clientMessage, WebSocketPlayer player) {
        Game game = player.getCurrentGame();

        game.rollDice();

        return ServerMessage.builder()
                .messagePath("roll_dice")
                .type(ServerMessage.MessageType.INFO)
                .game(game)
                .build();
    }

    private static ServerMessage movePlayer(ClientMessage clientMessage, WebSocketPlayer player) {
        Game game = player.getCurrentGame();

        game.movePlayer();

        return generateUpdateMessage(ServerMessage.MessageType.INFO, clientMessage.getPlayer().getCurrentGame());
    }

    private static ServerMessage endCurrentPlayerTurn(WebSocketPlayer player) {
        Game game = player.getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        game.endCurrentPlayerTurn(currentPlayer);

        return initiateRound(player);
    }

    private static ServerMessage cheating(int totalValue, WebSocketPlayer player){
        Game game = player.getCurrentGame();
        Dice dice = player.getCurrentGame().getDice();

        dice.setValue1(totalValue/2);
        dice.setValue2(totalValue - totalValue/2);

        game.cheating();

        return ServerMessage.builder()
                .messagePath("cheating")
                .type(ServerMessage.MessageType.INFO)
                .game(game)
                .build();
    }

    /**
     * Handles move avatar differently if player wants to cheat
     * @param message Server message
     * @param player current player
     * @return generate a update message
     */
    private static ServerMessage moveCheatingPlayer(ClientMessage message, WebSocketPlayer player){
        Game game = player.getCurrentGame();
        game.moveCheatingPlayer();

        return generateUpdateMessage(ServerMessage.MessageType.INFO, message.getPlayer().getCurrentGame());
    }

    private static ServerMessage gameState(int gameId, WebSocketPlayer player) {
        Game.GameState gameState = Game.getGameState(gameId, player);

        return ServerMessage.builder()
                .messagePath("game_state")
                .type(ServerMessage.MessageType.INFO)
                .jsonData(gson.toJson(gameState))
                .build();
    }

   private static ServerMessage checkCurrentField(ClientMessage clientMessage){
    Auction auction = new Auction();
       String message = clientMessage.getMessage();
       int  currentFieldIndex;

       try {
           currentFieldIndex = Integer.parseInt(message);
       } catch (NumberFormatException e) {
           System.out.println("Fehler beim Parsen des Feldindex aus der Nachricht: " + message);
           return ServerMessage.builder()
                   .messagePath("checkCurrentField")
                   .type(ServerMessage.MessageType.ERROR)
                   .build();
       }

    boolean isProperty = auction.checkCurrentField(currentFieldIndex);
       if ( isProperty) {
           return ServerMessage.builder()
                   .messagePath("checkCurrentField")
                   .type(ServerMessage.MessageType.INFO)
                   .jsonData("true")
                   .build();
       } else {
           return ServerMessage.builder()
                   .messagePath("checkCurrentField")
                   .type(ServerMessage.MessageType.INFO)
                   .jsonData("false")
                   .build();
       }
}

}
