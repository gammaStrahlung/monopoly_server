package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.PropertyColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Method;
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
    void testProcessMortgage() {
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "Test Player", null, 1000);
        game.addPlayer(player);  // Correct way to add players

        Property property = Property.builder()
                .fieldId(1)
                .name("Test Property")
                .price(500)
                .owner(player)
                .color(PropertyColor.BROWN)
                .rentPrices(new HashMap<>())
                .mortgageValue(250)
                .houseCost(50)
                .hotelCost(50)
                .build();
        game.getGameBoard().getGameBoard()[1] = property;

        assertTrue(game.processMortgage(1, playerId));
        assertTrue(property.isMortgaged());
        assertEquals(1250, player.getBalance());

        assertFalse(game.processMortgage(1, playerId));
    }

    @Test
    void testRepayMortgage() {
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "Test Player", null, 1000);
        game.addPlayer(player);

        Property property = Property.builder()
                .fieldId(1)
                .name("Test Property")
                .price(500)
                .owner(player)
                .color(PropertyColor.BROWN)
                .rentPrices(new HashMap<>())
                .mortgageValue(250)
                .houseCost(50)
                .hotelCost(50)
                .build();
        property.setMortgaged(true);
        game.getGameBoard().getGameBoard()[1] = property;

        assertTrue(game.repayMortgage(1, playerId));
        assertFalse(property.isMortgaged());
        assertEquals(725, player.getBalance()); // Corrected balance after mortgage repayment

        player.setBalance(50); // Set balance to an amount insufficient for repayment
        assertFalse(game.repayMortgage(1, playerId)); // Verify that repayment fails with insufficient funds
    }


    @Test
    void testPlayerRetrievalUsingReflection() {
        UUID playerId = UUID.randomUUID();
        Player player = new Player(playerId, "Test Player", null, 1000);
        game.addPlayer(player);

        try {
            Method getPlayerById = Game.class.getDeclaredMethod("getPlayerById", UUID.class);
            getPlayerById.setAccessible(true); // Make the method accessible

            Player retrievedPlayer = (Player) getPlayerById.invoke(game, playerId);
            assertNotNull(retrievedPlayer);
            assertEquals(playerId, retrievedPlayer.getId());
        } catch (Exception e) {
            fail("Reflection to access getPlayerById failed", e);
        }
    }

}
