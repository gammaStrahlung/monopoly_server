package at.gammastrahlung.monopoly_server.game;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WebSocketPlayer extends Player {
    /**
     * Contains all players with the ID of the WebSocketSession as the key.
     * This allows the disconnect handler to find the associated user for the WebSocketConnection.
     */
    private static final ConcurrentHashMap<String, WebSocketPlayer> players = new ConcurrentHashMap<>();

    /**
     * The WebSocketSession, the user is connected through.
     */
    @Expose(serialize = false, deserialize = false) // Should not be sent to the client
    private WebSocketSession webSocketSession;

    @Override
    protected void finalize() throws Throwable {
        // Remove player from players list
        players.remove(this.getWebSocketSession().getId());

        super.finalize();
    }

    /**
     * Updates the player Object with the new WebSocketSession if player is of type WebSocketPlayer.
     *
     * @param player The player object with changed properties.
     */
    @Override
    public void update(Player player) {
        if (player.getClass() == this.getClass()) {
            // Only update if player is a WebSocketPlayer
            players.remove(webSocketSession.getId());
            setWebSocketSession(((WebSocketPlayer) player).getWebSocketSession());
            players.put(webSocketSession.getId(), this);
        }
    }

    /**
     * Gets the Player associated with a WebSocket.
     * @param webSocketSessionId The ID of the WebSocketSession.
     * @return the player if one is associated with the given id, else null.
     */
    public static WebSocketPlayer getPlayerByWebSocketSessionID(String webSocketSessionId) {
        return players.get(webSocketSessionId);
    }
}
