package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
            Player mockPlayer = mock(Player.class);
            when(mockPlayer.getId()).thenReturn(UUID.randomUUID());
            when(mockPlayer.getName()).thenReturn("Player " + i);

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
    void getCurrentPlayer() {
        // Add four players
        for (int i = 0; i < 4; i++)
            game.join(players.get(i));

        // start the game
        assertTrue(game.startGame(players.get(0)));

        game.setCurrentPlayerIndex(2);
        Player currentPlayer = game.getCurrentPlayer();

        assertEquals(players.get(2), currentPlayer);
    }

    @Test
    void rollDiceAndMoveCurrentPlayer() {
        Player player = new Player(UUID.randomUUID(), "Test Player", null, 100);

        game.join(player);
        // Add other players
        for (int i = 0; i < 4; i++)
            game.join(players.get(i));

        // Create a mock for the dice
        Dice dice = mock(Dice.class);
        when(dice.roll()).thenReturn(4);

        // start the game
        assertTrue(game.startGame(player));

        // Set the mock dice in the game object
        game.setDice(dice);
        game.setCurrentPlayerIndex(0);

        assertEquals(player, game.getCurrentPlayer());

        // Call the method to be tested
        game.rollDiceAndMoveCurrentPlayer();

        assertEquals(4, game.getCurrentPlayer().getCurrentFieldIndex());
        assertEquals(0, game.getCurrentPlayerIndex());
    }

    @Test
    void awardBonusMoney(){
        Player player = new Player(UUID.randomUUID(), "Test Player", null, 0);

        game.join(player);
        // Add other players
        for (int i = 0; i < 4; i++)
            game.join(players.get(i));

        // Set current player
        game.setCurrentPlayerIndex(0);

        // Player does not pass start
        game.awardBonusMoney(5, 10, player);
        assertEquals(0, player.getBalance());

        // Player lands on start (does not get money)
        game.awardBonusMoney(35, 0, player);
        assertEquals(0, player.getBalance());

        // Player passes start
        game.awardBonusMoney(30, 2, player);
        assertEquals(200, player.getBalance());
    }

    @Test
    void endPlayerTurn(){
        // Add four players
        for (int i = 0; i < 4; i++)
            game.join(players.get(i));

        // start the game
        assertTrue(game.startGame(players.get(0)));

        game.setCurrentPlayerIndex(1);
        game.endCurrentPlayerTurn();
        assertEquals(2, game.getCurrentPlayerIndex());

        game.setCurrentPlayerIndex(4);
        game.endCurrentPlayerTurn();
        assertEquals(1, game.getCurrentPlayerIndex());
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
    @Test
    void testSetterAndGetters() {
        Game game = new Game();
        Dice dice = new Dice();
        game.setDice(dice);
        assertEquals(dice, game.getDice(), "The setter or getter for dice is not functioning properly.");
    }

    @Test
    void testUniqueGameIdGeneration() {
        Set<Integer> gameIds = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            Game game = new Game();
            gameIds.add(game.getGameId());
        }
        assertEquals(100, gameIds.size(), "Game IDs are not unique.");
    }


    @Test
    void testUniqueIdGeneration() {
        Set<Integer> ids = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            Game game = new Game();
            ids.add(game.getGameId());
        }
        assertEquals(100, ids.size(), "Game IDs should be unique across multiple instantiations.");
    }

    @Test
    void testGameLoopIdUniqueness() {
        Game game = new Game();
        int initialGameId = game.getGameId();
        while (true) {
            game = new Game();
            if (game.getGameId() != initialGameId) {
                break;
            }
        }
        assertNotEquals(initialGameId, game.getGameId(), "Game ID should eventually be unique after re-instantiation.");
    }

    @Test
    void testStartAuction() {
        Property property = new Property();  // Assume you have a constructor or a setup method
        game.startAuction(property);
        assertNotNull(game.getCurrentAuction(), "Auction should be initialized");
    }



    @Test
    void testGameEnd() {
        game.join(players.get(0));
        game.setGameOwner(players.get(0));
        game.endGame(players.get(0));
        assertEquals(Game.GameState.ENDED, game.getState(), "Game should be set to ENDED state");
        assertTrue(game.getPlayers().isEmpty(), "Players list should be cleared after game ends");
    }

    @Test
    void testInvalidGameStartByNonOwner() {
        game.join(players.get(0));
        game.join(players.get(1)); // Player 1 is not the owner
        assertFalse(game.startGame(players.get(1)), "Non-owner should not be able to start the game");
    }

    @Test
    void testPlayerRejoin() {
        game.join(players.get(0));
        game.startGame(players.get(0));
        assertTrue(game.join(players.get(0)), "Player should be able to re-join the game");
    }

    @Test
    void testGameInitialization() {
        assertNotNull(game.getGameId(), "Game ID should be initialized");
        assertEquals(Game.GameState.STARTED, game.getState(), "Initial game state should be STARTED");
    }


}
