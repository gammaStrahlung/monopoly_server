package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.Railroad;
import at.gammastrahlung.monopoly_server.game.gameboard.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.platform.commons.logging.Logger;

import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.Mockito;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GameTests {

    private Game game;
    private static final Logger LOGGER = LoggerFactory.getLogger(GameTests.class);
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
    void awardBonusMoney() {
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
    void endPlayerTurn() {
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
    void processRailroadPayment_NoOwner_Fails() {
        Game game = new Game();
        Player payer = new Player(UUID.randomUUID(), "Payer", game, 100);
        Railroad railroad = mock(Railroad.class);
        when(railroad.getOwner()).thenReturn(null);

        assertFalse(game.processRailroadPayment(payer, railroad));
    }

    @Test
    void processPropertyPayment_OwnerExistsAndNotPayer_SuccessfulPayment() {
        Game game = new Game();
        Player payer = new Player(UUID.randomUUID(), "Payer", game, 100);
        Player owner = new Player(UUID.randomUUID(), "Owner", game, 50);
        Property property = mock(Property.class);
        when(property.getOwner()).thenReturn(owner);
        when(property.getRentPrices()).thenReturn(Map.of(0, 20));

        assertTrue(game.processPropertyPayment(payer, property));
        assertEquals(80, payer.getBalance());
        assertEquals(70, owner.getBalance());
    }

    @Test
    void processUtilityPayment_OwnerExistsAndNotPayer_SuccessfulPayment() {
        Game game = new Game();
        Player payer = new Player(UUID.randomUUID(), "Payer", game, 100);
        Player owner = new Player(UUID.randomUUID(), "Owner", game, 50);
        Utility utility = mock(Utility.class);
        when(utility.getOwner()).thenReturn(owner);
        when(utility.getToPay()).thenReturn(30);

        assertTrue(game.processUtilityPayment(payer, utility));
        assertEquals(70, payer.getBalance());
        assertEquals(80, owner.getBalance());
    }

    @Test
    void makePayment_InsufficientFunds() {
        Game game = new Game();
        Player from = new Player(UUID.randomUUID(), "From", game, 10);
        Player to = new Player(UUID.randomUUID(), "To", game, 50);

        assertFalse(game.makePayment(from, to, 20));
        assertEquals(10, from.getBalance());
        assertEquals(50, to.getBalance());
    }
    @Test
    void processRailroadPayment_AllOwnedBySamePlayer() {
        Game game = new Game();
        Player owner = new Player(UUID.randomUUID(), "Owner", null, 1500);
        Player payer = new Player(UUID.randomUUID(), "Payer", null, 500);
        Railroad railroad = Mockito.mock(Railroad.class);

        when(railroad.getOwner()).thenReturn(owner);
        when(railroad.getRentPrices()).thenReturn(Map.of("1RR", 100));

        // Simulate that the game board includes this railroad and it is owned by the owner
        Field[] gameBoard = new Field[40];
        gameBoard[5] = railroad;
        game.getGameBoard().setGameBoard(gameBoard);

        // The owner owns one railroad, hence "1RR" price is applicable
        boolean result = game.processRailroadPayment(payer, railroad);

        assertTrue(result);
        assertEquals(400, payer.getBalance());
        assertEquals(1600, owner.getBalance());  // Adjusted to reflect the correct expected balance after transaction
    }





    @Test
    void processPropertyPayment_NoOwner() {
        Game game = new Game();
        Player payer = new Player(UUID.randomUUID(), "Payer", null, 500);
        Property property = Mockito.mock(Property.class);
        when(property.getOwner()).thenReturn(null);

        boolean result = game.processPropertyPayment(payer, property);

        assertFalse(result);
        assertEquals(500, payer.getBalance());
    }

    @Test
    void processUtilityPayment_Successful() {
        Game game = new Game();
        Player payer = new Player(UUID.randomUUID(), "Payer", null, 500);
        Utility utility = Mockito.mock(Utility.class);
        Player owner = new Player(UUID.randomUUID(), "Owner", null, 500);
        when(utility.getOwner()).thenReturn(owner);
        when(utility.getToPay()).thenReturn(100);

        boolean result = game.processUtilityPayment(payer, utility);

        assertTrue(result);
        assertEquals(400, payer.getBalance());
        assertEquals(600, owner.getBalance());
    }

    @Test
    void processRailroadPayment_OwnerDoesNotExist() {
        Game game = new Game();
        Player payer = new Player(UUID.randomUUID(), "Payer", null, 500);
        Railroad railroad = new Railroad(); // Kein Besitzer gesetzt

        boolean result = game.processRailroadPayment(payer, railroad);

        assertFalse(result);
        assertEquals(500, payer.getBalance());
    }



    @Test
    void processRailroadPayment_Failure_OwnerIsPayer() {
        Game game = new Game();
        Player owner = new Player(UUID.randomUUID(), "Owner", null, 1500);
        Railroad railroad = Mockito.mock(Railroad.class);

        when(railroad.getOwner()).thenReturn(owner);
        when(railroad.getRentPrices()).thenReturn(Map.of("1RR", 100));

        // Owner is also the payer
        boolean result = game.processRailroadPayment(owner, railroad);

        assertFalse(result);
        assertEquals(1500, owner.getBalance()); // Balance remains unchanged since owner is paying to themselves
    }

}
