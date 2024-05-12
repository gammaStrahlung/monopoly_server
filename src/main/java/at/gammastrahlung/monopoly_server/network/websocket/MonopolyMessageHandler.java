package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.Game;
import at.gammastrahlung.monopoly_server.game.Player;
import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import at.gammastrahlung.monopoly_server.game.gameboard.*;
import at.gammastrahlung.monopoly_server.network.dtos.ClientMessage;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import at.gammastrahlung.monopoly_server.network.json.FieldSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

import java.util.ArrayList;

public class MonopolyMessageHandler {

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
                                        clientMessage.getPlayer()
                                ),
                                clientMessage.getPlayer().getCurrentGame().getPlayers()
                        );
                    }

                    yield message;
                }
                case "start" -> startGame(clientMessage.getPlayer());
                case "end" -> endGame(clientMessage.getPlayer());
                case "roll_dice" -> rollDiceAndMoveCurrentPlayer(clientMessage, clientMessage.getPlayer());
                case "initiate_round" -> initiateRound(clientMessage.getPlayer());
                case "end_current_player_turn" -> endCurrentPlayerTurn(clientMessage.getPlayer());
                case "move_avatar" ->
                        generateUpdateMessage(ServerMessage.MessageType.INFO, clientMessage.getPlayer().getCurrentGame());
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
        player.setBalance(1500);

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
        if (game == null){
            if (!game.getPlayers().contains(player)) {
                player.setBalance(1500);
            }
        return ServerMessage.builder()
                .type(ServerMessage.MessageType.ERROR)
                .messagePath("join")
                .player(player)
                .build();
    } else
            return ServerMessage.builder()
                    .type(ServerMessage.MessageType.SUCCESS)
                    .messagePath("join")
                    .jsonData(gson.toJson(game))
                    .player(player)
                    .build();
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

    private static ServerMessage rollDiceAndMoveCurrentPlayer(ClientMessage clientMessage, WebSocketPlayer player) {
        Game game = player.getCurrentGame();

        game.rollDiceAndMoveCurrentPlayer();

        return ServerMessage.builder()
                .messagePath("roll_dice")
                .type(ServerMessage.MessageType.INFO)
                .game(game)
                .build();
    }

    private static ServerMessage endCurrentPlayerTurn(WebSocketPlayer player) {
        Game game = player.getCurrentGame();
        game.endCurrentPlayerTurn();

        return initiateRound(player);
    }

    /**
     * Initiates a payment transaction between two players.
     * @param clientMessage The client message containing transaction details.
     * @param session The WebSocket session of the initiating player.
     * @return ServerMessage indicating the result of the transaction initiation.
     */
    public static ServerMessage initiatePayment(ClientMessage clientMessage, WebSocketSession session) {
        WebSocketPlayer initiatingPlayer = WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId());
        if (initiatingPlayer == null) {
            return ServerMessage.builder()
                    .type(ServerMessage.MessageType.ERROR)
                    .jsonData("Player not found")
                    .build();
        }

        int paymentAmount;
        try {
            paymentAmount = Integer.parseInt(clientMessage.getMessage());
        } catch (NumberFormatException e) {
            return ServerMessage.builder()
                    .type(ServerMessage.MessageType.ERROR)
                    .jsonData("Invalid payment amount")
                    .build();
        }

        WebSocketPlayer targetPlayer = WebSocketPlayer.getPlayerById(clientMessage.getTargetPlayerId());
        if (targetPlayer == null) {
            return ServerMessage.builder()
                    .type(ServerMessage.MessageType.ERROR)
                    .jsonData("Target player not found")
                    .build();
        }

        return ServerMessage.builder()
                .type(ServerMessage.MessageType.SUCCESS)
                .jsonData("Payment initiation successful")
                .build();
    }
    public static ServerMessage handlePayment(ClientMessage clientMessage, WebSocketSession session) {
        WebSocketPlayer player = WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId());
        if (player == null) {
            return ServerMessage.builder()
                    .messagePath("payment")
                    .type(ServerMessage.MessageType.ERROR)
                    .jsonData("Player not found")
                    .build();
        }

        // Extract payment details from clientMessage
        String targetFieldIdString = clientMessage.getMessage();
        int targetFieldId;
        try {
            targetFieldId = Integer.parseInt(targetFieldIdString);
        } catch (NumberFormatException e) {
            return ServerMessage.builder()
                    .messagePath("payment")
                    .type(ServerMessage.MessageType.ERROR)
                    .jsonData("Invalid field ID")
                    .build();
        }

        Field targetField = player.getCurrentGame().getGameBoard().getGameBoard()[targetFieldId];
        boolean paymentResult = false;

        // Check the type of the field and process payment accordingly
        if (targetField instanceof Railroad) {
            paymentResult = player.getCurrentGame().processRailroadPayment(player, (Railroad) targetField);
        } else if (targetField instanceof Property) {
            paymentResult = player.getCurrentGame().processPropertyPayment(player, (Property) targetField);
        } else if (targetField instanceof Utility) {
            paymentResult = player.getCurrentGame().processUtilityPayment(player, (Utility) targetField);
        }

        // Create response message based on the result of the payment
        if (paymentResult) {
            return ServerMessage.builder()
                    .messagePath("payment")
                    .type(ServerMessage.MessageType.SUCCESS)
                    .jsonData("Payment processed successfully")
                    .build();
        } else {
            return ServerMessage.builder()
                    .messagePath("payment")
                    .type(ServerMessage.MessageType.ERROR)
                    .jsonData("Payment failed")
                    .build();
        }
    }


}




