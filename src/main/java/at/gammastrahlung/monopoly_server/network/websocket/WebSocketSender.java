package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.Player;
import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import at.gammastrahlung.monopoly_server.network.json.FieldSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;


@NoArgsConstructor
public class WebSocketSender {
    private static Player playersforAuction;

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(Field.class, new FieldSerializer())
            .create();

    /**
     * Sends a message only to the given WebSocketSession
     *
     * @param webSocketSession The WebSocketSession of the player.
     * @param message          The message to be sent.
     */
    public static void sendToPlayer(WebSocketSession webSocketSession, ServerMessage message) {
        try {
            // Send a message serialized as Json to the WebSocket client
            if (webSocketSession.isOpen()) // Check if WebSocket connection is open
                webSocketSession.sendMessage(new TextMessage(gson.toJson(message)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Sends a message to the WebSocketPlayers contained in players
     *
     * @param message The message to be sent.
     * @param players The players the message should be sent to.
     */
    public static void sendToPlayers(ServerMessage message,
                                     List<Player> players) {

        if (message.getMessagePath().equals("checkCurrentField")) {
            // Send message only to playerforAuction
            try {
                WebSocketSession ws = ((WebSocketPlayer) playersforAuction).getWebSocketSession();
                if (ws != null && ws.isOpen()) // Check if WebSocket connection exists and is open
                    ws.sendMessage(new TextMessage(gson.toJson(message)));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {


            for (Player player : players) {
                if (player.getClass() != WebSocketPlayer.class)
                    continue; // Skip non WebSocketPlayers

                try {
                    // Send message serialized as Json to each WebSocketPlayer
                    WebSocketSession ws = ((WebSocketPlayer) player).getWebSocketSession();

                    if (ws != null && ws.isOpen()) // Check if WebSocket connection exists and is open
                        ws.sendMessage(new TextMessage(gson.toJson(message)));

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    public static void setPlayersforAuction(Player playersforAuction) {
        WebSocketSender.playersforAuction = playersforAuction;
    }
}
