package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.Game;
import at.gammastrahlung.monopoly_server.game.Player;
import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;


@NoArgsConstructor
public class WebSocketSender {
    /**
     * Sends a message only to the given WebSocketSession
     *
     * @param webSocketSession The WebSocketSession of the player.
     * @param message          The message to be sent.
     */
    public static void sendToPlayer(WebSocketSession webSocketSession, ServerMessage message) {
        try {
            Gson gson =  new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
            // Send a message serialized as Json to the WebSocket client
            webSocketSession.sendMessage(new TextMessage(gson.toJson(message)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends a message to the Player associated with the given WebSocketSession
     * and to all other players in the same game
     *
     * @param webSocketSession    The WebSocketSession of the player.
     * @param message             The message to be sent.
     * @param excludeGivenSession If true, the message is not sent to the given webSocketSession
     *                            (Used for disconnect handler).
     */
    public static void sendToAllGamePlayers(WebSocketSession webSocketSession,
                                     ServerMessage message,
                                     boolean excludeGivenSession) {
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        WebSocketPlayer p = WebSocketPlayer.getPlayerByWebSocketSessionID(webSocketSession.getId());
        Game g = p.getCurrentGame();
        if (g != null) {
            for (Player player : g.getPlayers()) {
                if (player.getClass() != WebSocketPlayer.class)
                    continue; // Skip non WebSocketPlayers

                if (excludeGivenSession && player == p)
                    continue; // Skip the given player if the skip option is enabled

                try {
                    // Send message serialized as Json to each WebSocketPlayer
                    ((WebSocketPlayer) player)
                            .getWebSocketSession()
                            .sendMessage(new TextMessage(gson.toJson(message)));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }

            }
        } else {
            // Game does not exist -> only send to given session
            try {
                webSocketSession.sendMessage(new TextMessage(gson.toJson(message)));
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * Sends a message to the Player associated with the given WebSocketSession
     * and to all other players in the same game
     *
     * @param webSocketSession The WebSocketSession of the player.
     * @param message          The message to be sent.
     */
    public static void sendToAllGamePlayers(WebSocketSession webSocketSession,
                                     ServerMessage message) {
        sendToAllGamePlayers(webSocketSession, message, false);
    }
}
