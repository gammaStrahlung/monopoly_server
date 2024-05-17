package at.gammastrahlung.monopoly_server.game;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
@Getter
@Setter
public class WebSocketPlayer extends Player {
    /**
     * Contains all players with the ID of the WebSocketSession as the key.
     * This allows the disconnect handler to find the associated user for the WebSocketConnection.
     */
    @Expose(serialize = false, deserialize = false) // Should not be sent to the client
    private static final ConcurrentHashMap<String, WebSocketPlayer> players = new ConcurrentHashMap<>();
    private static final int STARTING_BALANCE = 1500000;

    /**
     * The WebSocketSession, the user is connected through.
     */
    @Expose(serialize = false, deserialize = false) // Should not be sent to the client
    private WebSocketSession webSocketSession;

    WebSocketPlayer(UUID id, String name, Game currentGame, WebSocketSession webSocketSession) {
        super(id, name, currentGame, STARTING_BALANCE);
        setWebSocketSession(webSocketSession);
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        if (this.webSocketSession != null)
            players.remove(this.webSocketSession.getId());

        this.webSocketSession = webSocketSession;

        if (webSocketSession != null)
            players.put(webSocketSession.getId(), this);
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
            if (webSocketSession != null)
                players.remove(webSocketSession.getId());

            setWebSocketSession(((WebSocketPlayer) player).getWebSocketSession());
            players.put(webSocketSession.getId(), this);
        }
    }

    @Override
    public boolean equals(Object o) {
        // The only thing equals should check is the UUID, as everything else could change and the player would still be
        // the same
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), webSocketSession);
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
