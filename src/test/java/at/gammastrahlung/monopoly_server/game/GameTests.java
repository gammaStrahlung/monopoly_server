package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameTests {

    private Game game;

    private ArrayList<Player> players;

    @BeforeEach
    public void initialize() {
        game = new Game();

        // Create Mock players
        players = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            Player mockPlayer = Mockito.mock(Player.class);
            Mockito.when(mockPlayer.getId()).thenReturn(UUID.randomUUID());
            Mockito.when(mockPlayer.getName()).thenReturn("Player " + i);

            players.add(mockPlayer);
        }
    }

    @Test
    void newGame() {
        assertNotEquals(0, game.getGameId()); // GameId was set to something other than Java default
        assertEquals(Game.GameState.STARTED, game.getState()); // When created the game is started but not jet playing

        game.join(players.get(0)); // First player joins
        assertEquals(players.get(0), game.getGameOwner()); // First joined player is the gameOwner
    }

    @Test
    void startGame() {
        // Can't start a game without players
        assertFalse(game.startGame(null));

        // Add single player
        game.join(players.get(0));

        // Can't start a game with only on player
        assertFalse(game.startGame(players.get(0)));

        // Add some more players
        for (int i = 1; i < 4; i++)
            game.join(players.get(i));

        // Can't start the game if player is not gameOwner
        assertFalse(game.startGame(players.get(1)));

        // Can't start the game if player is null
        assertFalse(game.startGame(null));

        // Can start the game if player is gameOwner
        assertTrue(game.startGame(players.get(0)));

        // Can't start a second time
        assertFalse(game.startGame(players.get(0)));
    }

    @Test
    void endGame() {
        // Can't end the game if player is not gameOwner
        assertFalse(game.endGame(players.get(1)));

        // Can't end the game if player is null
        assertFalse(game.endGame(null));

        game.join(players.get(0)); // Add player to act as gameOwner

        // Can end the game if player is gameOwner
        assertTrue(game.endGame(players.get(0)));

        assertEquals(0, game.getGameId()); // Game ID is default int value
        assertEquals(Game.GameState.ENDED, game.getState()); // State is ended
    }

    @Test
    void joinByGameId() {
        int gameId = game.getGameId() + 100; // Game ID that does not exist

        // Can't join a game that doesn't exist
        assertNull(Game.joinByGameId(gameId, players.get(0)));

        // Can join a game that does exist
        assertNotNull(Game.joinByGameId(game.getGameId(), players.get(0)));
        assertNotNull(Game.joinByGameId(game.getGameId(), players.get(1)));

        // New player can't join a game that has already started
        game.startGame(players.get(0));
        assertNull(Game.joinByGameId(game.getGameId(), players.get(2)));
    }

    @Test
    void join() {
        // First 8 unique players can join
        for (int i = 0; i < 8; i++) {
            assertTrue(game.join(players.get(i)));
        }

        // 9th player can not join
        assertFalse(game.join(players.get(8)));

        // Can re-join game when not playing yet
        assertTrue(game.join(players.get(1)));

        game.startGame(players.get(0));

        // Can re-join even when game has already started
        assertTrue(game.join(players.get(1)));
    }

    @Test
    void getPlayers() {
        // No players added -> players has to be empty
        List<Player> gamePlayers = game.getPlayers();

        assertNotNull(gamePlayers);
        assertEquals(0, gamePlayers.size());

        // Add 5 players
        for (int i = 0; i < 5; i++) {
            game.join(players.get(i));
        }

        gamePlayers = game.getPlayers();

        // 5 Players should be in list
        assertNotNull(gamePlayers);
        assertEquals(5, gamePlayers.size());

        // Check for all 4 players
        for (int i = 0; i < 5; i++) {
            assertTrue(gamePlayers.contains(players.get(i)));
        }
    }
}
