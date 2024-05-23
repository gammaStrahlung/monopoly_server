package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.Game;
import at.gammastrahlung.monopoly_server.game.GameLogger;
import at.gammastrahlung.monopoly_server.game.Player;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public class WebSocketGameLogger implements GameLogger {
    private final Game game;

    public WebSocketGameLogger(Game game) {
        this.game = game;
    }

    @Override
    public void logMessage(String message) {
        ServerMessage logMessage = ServerMessage.builder()
                .type(ServerMessage.MessageType.INFO)
                .messagePath("log")
                .jsonData("{\"log\": \"" + message + "\"}")  // Embedding message in JSON format
                .build();

        WebSocketSender.sendToPlayers(logMessage, game.getPlayers());
    }
}
