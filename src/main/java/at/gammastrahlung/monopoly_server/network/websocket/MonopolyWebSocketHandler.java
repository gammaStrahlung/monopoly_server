package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import at.gammastrahlung.monopoly_server.network.dtos.ClientMessage;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import com.google.gson.Gson;
import org.springframework.web.socket.*;

public class MonopolyWebSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {}

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        Gson gson = new Gson();
        ClientMessage clientMessage = gson.fromJson(message.getPayload().toString(), ClientMessage.class);

        ServerMessage response;

        // Call the different Message Handlers
        try {
            switch (clientMessage.getMessagePath()) {
            case "create":
                response = MonopolyMessageHandler.createGame(clientMessage.getPlayer());
                break;
            case "join":
                response = MonopolyMessageHandler.joinGame(Integer.parseInt(clientMessage.getMessage()),
                        clientMessage.getPlayer());
                break;
            default:
                throw new IllegalArgumentException("Invalid MessagePath");
            }
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

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        notifyPlayersOfDisconnect(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) {
        notifyPlayersOfDisconnect(session);
    }

    /**
     *
     * @param session the WebSocketSession that disconnected
     */
    private void notifyPlayersOfDisconnect(WebSocketSession session) {
        ServerMessage message = ServerMessage.<String>builder()
                .messagePath("disconnect")
                .player(WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId()))
                .type(ServerMessage.MessageType.INFO)
                .build();

        WebSocketSender.sendToAllGamePlayers(session, message, true);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
