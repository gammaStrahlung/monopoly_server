package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebSocketPlayerTest {
    WebSocketSession webSocketSession;
    WebSocketSession webSocketSession2;
    Game game;

    @BeforeEach
    public void initialize() {
        webSocketSession = Mockito.mock(WebSocketSession.class);
        Mockito.when(webSocketSession.getId()).thenReturn("WS-ID");

        webSocketSession2 = Mockito.mock(WebSocketSession.class);
        Mockito.when(webSocketSession2.getId()).thenReturn("WSS2");

        game = Mockito.mock(Game.class);
    }

    @Test
    void newWebSocketPlayer() {
        WebSocketPlayer p1 = new WebSocketPlayer();

        // Default constructor -> all fields should be null
        assertNull(p1.getWebSocketSession());
        assertNull(p1.getName());
        assertNull(p1.getId());
        assertNull(p1.getCurrentGame());


        UUID id = UUID.randomUUID();
        WebSocketPlayer p2 = new WebSocketPlayer(id, "NAME", game, webSocketSession);

        assertEquals(game, p2.getCurrentGame());
        assertEquals("NAME", p2.getName());
        assertEquals(id, p2.getId());
        assertEquals(webSocketSession, p2.getWebSocketSession());

        WebSocketPlayer p3 = WebSocketPlayer.getPlayerByWebSocketSessionID(webSocketSession.getId());

        assertEquals(p2, p3);
    }

    @Test
    void updatePlayer() {
        WebSocketPlayer p1 = new WebSocketPlayer();
        p1.setWebSocketSession(webSocketSession);

        WebSocketPlayer p2 = new WebSocketPlayer();
        p2.setWebSocketSession(webSocketSession2);

        p1.update(p2);

        assertEquals(webSocketSession2, p1.getWebSocketSession());

        // Do not update when Player is not a WebSocketPlayer
        Player player = new Player() {
            @Override
            public void update(Player player) {}

            public WebSocketSession getWebSocketSession() {
                return webSocketSession;
            }
        };

        p2.update(player);

        assertEquals(webSocketSession2, p2.getWebSocketSession());
    }

    @Test
    void setWebSocketSession() {
        WebSocketPlayer p = new WebSocketPlayer();
        // Set random UUID
        p.setId(UUID.randomUUID());

        p.setWebSocketSession(webSocketSession);

        assertEquals(webSocketSession, p.getWebSocketSession());
        assertEquals(p, WebSocketPlayer.getPlayerByWebSocketSessionID(webSocketSession.getId()));

        p.setWebSocketSession(null);

        assertNull(p.getWebSocketSession());
        assertNull(WebSocketPlayer.getPlayerByWebSocketSessionID(webSocketSession.getId()));
    }

    @Test
    void getPlayerById_PlayerExists() {
        // Setup
        UUID playerId = UUID.randomUUID();
        WebSocketSession session = Mockito.mock(WebSocketSession.class);
        Mockito.when(session.getId()).thenReturn("session1");
        WebSocketPlayer player = new WebSocketPlayer(playerId, "Test Player", null, session);
        player.setWebSocketSession(session);  // This adds the player to the map

        // Action
        WebSocketPlayer result = WebSocketPlayer.getPlayerById(session.getId());

        // Assert
        assertNotNull(result);
        assertEquals(player, result);

        // Cleanup
        player.setWebSocketSession(null);  // Removes the player from the map
    }

    @Test
    void getPlayerById_PlayerDoesNotExist() {
        // Action
        WebSocketPlayer result = WebSocketPlayer.getPlayerById("nonexistent");

        // Assert
        assertNull(result);
    }
}
