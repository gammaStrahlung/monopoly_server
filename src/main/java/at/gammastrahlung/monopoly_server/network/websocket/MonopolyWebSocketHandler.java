package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.Game;
import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import at.gammastrahlung.monopoly_server.network.dtos.ClientMessage;
import com.google.gson.Gson;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class MonopolyWebSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        Gson gson = new Gson();
        ClientMessage clientMessage = gson.fromJson(message.getPayload().toString(), ClientMessage.class);

        MonopolyMessageHandler.handleMessage(clientMessage, session);
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
     * Notifies all players about another players disconnect
     * @param session the WebSocketSession that disconnected
     */
    private void notifyPlayersOfDisconnect(WebSocketSession session) {
        WebSocketPlayer p = WebSocketPlayer.getPlayerByWebSocketSessionID(session.getId());

        if (p == null)
            return; // Player was not playing a game

        // Player disconnected -> WebSocketSession is invalid
        p.setWebSocketSession(null);

        Game currentGame = p.getCurrentGame();
        if (currentGame == null)
            return; // Can't notify other players when not playing a game

        // Notify game of disconnect
        currentGame.playerDisconnected(p);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
