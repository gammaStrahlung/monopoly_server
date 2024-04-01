package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class MonopolyWebSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {}

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {

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
        ServerMessage<String> message = ServerMessage.<String>builder()
                .messagePath("disconnect")
                .player(WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId()))
                .type(ServerMessage.MessageType.INFO)
                .build();

        new WebSocketSender<String>().sendToAllGamePlayers(session, message, true);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
