package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.*;

@SpringBootTest
class GameTests {

    private Game game;
    private ArrayList<Player> players;
    private Player currentPlayer;

    @BeforeEach
    void initialize() {
        game = new Game();

        // Create Mock players
        players = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            Player mockPlayer = mock(Player.class);
            when(mockPlayer.getId()).thenReturn(UUID.randomUUID());
            when(mockPlayer.getName()).thenReturn("Player " + i);

            players.add(mockPlayer);
        }
        currentPlayer = players.get(0);
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
        currentPlayer = game.getCurrentPlayer();

        assertEquals(players.get(2), currentPlayer);
    }

    @Test
    void moveCheatingPlayer(){
        Player player = new Player(UUID.randomUUID(), "Test Player", null, 100);
        game.join(player);

        Dice dice = new Dice();
        dice.setValue1(1);
        dice.setValue2(3);

        game.setDice(dice);
        player.setCurrentFieldIndex(7);

        game.moveCheatingPlayer();
        assertEquals(11, game.getCurrentPlayer().getCurrentFieldIndex() );
    }

    @Test
    void isCheating(){
        Player player = new Player(UUID.randomUUID(), "Test Player", null, 100);
        game.join(player);

        assertFalse(player.isCheating());

        game.cheating();

        assertTrue(player.isCheating);
        player.setTurns(1);

        game.endCurrentPlayerTurn(player);
        assertFalse(player.isCheating);
        assertEquals(0, player.getTurns());
    }

    @Test
    void rollDice(){
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
        game.rollDice();
        assertEquals(4, player.getLastDicedValue());
    }

    @Test
    void movePlayer() {
        Player player = new Player(UUID.randomUUID(), "Test Player", null, 100);

        game.join(player);
        // Add other players
        for (int i = 0; i < 4; i++)
            game.join(players.get(i));

        int diceValue = 4;
        player.setLastDicedValue(diceValue);

        // start the game
        assertTrue(game.startGame(player));

        game.setCurrentPlayerIndex(0);

        assertEquals(player, game.getCurrentPlayer());

        // Call the method to be tested
        game.movePlayer();

        assertEquals(4, game.getCurrentPlayer().getCurrentFieldIndex());
        assertEquals(0, game.getCurrentPlayerIndex());
    }

    @Test
    void playerInJailDicesDoubles() {
        Player player = new Player(UUID.randomUUID(), "Test Player", null, 100);

        player.isInJail = true;
        game.join(player);
        // Add other players
        for (int i = 0; i < 4; i++)
            game.join(players.get(i));

        // start the game
        assertTrue(game.startGame(player));

        game.setCurrentPlayerIndex(0);

        assertEquals(player, game.getCurrentPlayer());

        // Call the method to be tested
        game.movePlayer();

        assertEquals(0, game.getCurrentPlayer().getCurrentFieldIndex());
        assertEquals(0, game.getCurrentPlayerIndex());
    }

    @Test
    void playerInJailDoesntDiceDoubles() {
        Player player = new Player(UUID.randomUUID(), "Test Player", null, 100);

        player.isInJail = true;
        player.setCurrentFieldIndex(30);

        game.join(player);
        // Add other players
        for (int i = 0; i < 4; i++)
            game.join(players.get(i));

        // Balance after join in a regular game is 1500, thus we need to change if for this test
        player.setBalance(100);

        // Create a mock for the dice
        Dice dice = mock(Dice.class);
        when(dice.roll()).thenReturn(5);

        // Assume player rolls doubles
        when(dice.getValue1()).thenReturn(3);
        when(dice.getValue2()).thenReturn(2);


        // start the game
        assertTrue(game.startGame(player));

        // Set the mock dice in the game object
        game.setDice(dice);
        game.setCurrentPlayerIndex(0);

        assertEquals(player, game.getCurrentPlayer());

        // Call the method to be tested
        game.movePlayer();

        //Player does not move since they are in jail and have not thrown doubles
        assertEquals(30, game.getCurrentPlayer().getCurrentFieldIndex());
        assertEquals(0, game.getCurrentPlayerIndex());

        // Player has not rolled doubles in 3 tries
        player.setRoundsInJail(3);
        game.movePlayer();

        //check if player has paid fine
        assertEquals(50, player.getBalance());
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
        assertEquals(1500, player.getBalance()); // Initial balance after join is 1500

        // Player lands on start (does not get money)
        game.awardBonusMoney(35, 0, player);
        assertEquals(1500, player.getBalance()); // Initial balance after join is 1500

        // Player passes start
        game.awardBonusMoney(30, 2, player);
        assertEquals(1500 + 200, player.getBalance()); // Initial balance after join is 1500, bonus money is 200
    }


    @Test
    void endPlayerTurn() {
        // Add four players
        for (int i = 0; i < 4; i++)
            game.join(players.get(i));

        // Set currentPlayerIndex to 1 and end turn
        game.setCurrentPlayerIndex(1);
        game.endCurrentPlayerTurn(currentPlayer);

        assertEquals(0, currentPlayer.getTurns());
        assertEquals(2, game.getCurrentPlayerIndex());
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
    void testProcessPropertyPayment_PropertyWithHouses_ShouldChargeCorrectRent() {
        Player payer = new Player(UUID.randomUUID(), "Payer", null, 1500);
        Player owner = new Player(UUID.randomUUID(), "Owner", null, 1500);
        Property property = new Property();
        property.setOwner(owner);
        property.setHouseCount(3);
        Map<Object, Integer> rentPrices = new HashMap<>();
        rentPrices.put(3, 300);
        property.setRentPrices(rentPrices);
        game.getGameBoard().getFields()[3] = property;

        boolean result = game.processPropertyPayment(payer, property);
        assertTrue(result, "Payment should succeed when payer has enough balance.");
        assertEquals(1200, payer.getBalance(), "Payer's balance should be decremented by rent amount.");
        assertEquals(1800, owner.getBalance(), "Owner's balance should be incremented by rent amount.");
    }


    @Test
    void testProcessRailroadPayment_UnownedRailroad_ShouldNotPayRent() {
        Player payer = new Player(UUID.randomUUID(), "Payer", null, 1500);
        Railroad railroad = new Railroad();
        railroad.setOwner(game.getGameBoard().getBank()); // Railroad is initially owned by the bank

        boolean result = game.processRailroadPayment(payer, railroad);
        assertFalse(result, "Payment should fail when railroad is not owned.");
        assertEquals(1500, payer.getBalance(), "Payer's balance should remain unchanged.");
    }

    @Test
    void testProcessPropertyPayment_UnownedProperty_ShouldNotChargeRent() {
        Player payer = new Player(UUID.randomUUID(), "Payer", null, 1500);
        Property property = new Property();
        property.setOwner(game.getGameBoard().getBank()); // Property is initially owned by the bank

        boolean result = game.processPropertyPayment(payer, property);
        assertFalse(result, "Payment should fail when property is not owned.");
        assertEquals(1500, payer.getBalance(), "Payer's balance should remain unchanged.");
    }

    @Test
    void testMakePayment_InsufficientFunds_ShouldNotTransferMoney() {
        Player from = new Player(UUID.randomUUID(), "Debtor", null, 100);
        Player to = new Player(UUID.randomUUID(), "Creditor", null, 1500);
        assertFalse(game.makePayment(from, to, 200), "Payment should fail if from player has insufficient funds.");
        assertEquals(100, from.getBalance(), "Debtor's balance should remain unchanged due to insufficient funds.");
        assertEquals(1500, to.getBalance(), "Creditor's balance should remain unchanged.");
    }

    @Test
    void testProcessRailroadPayment_OwnerAndNotOwner() {
        Player payer = new Player(UUID.randomUUID(), "Payer", game, 1500);
        Player owner = new Player(UUID.randomUUID(), "Owner", game, 1500);
        Railroad railroad = new Railroad();
        railroad.setOwner(owner);
        // Mock the method to return 2 railroads owned by the owner
        game.getGameBoard().getFields()[5] = railroad; // Assuming index 5 is a Railroad
        game.getGameBoard().getFields()[15] = railroad; // Assuming index 15 is also owned by the same owner

        assertTrue(game.processRailroadPayment(payer, railroad));
        // Validate that the payer's balance was deducted by the correct rent amount
        assertEquals(1450, payer.getBalance());

        railroad.setOwner(game.getGameBoard().getBank());
        assertFalse(game.processRailroadPayment(payer, railroad));
    }

    @Test
    void testProcessUtilityPayment_OwnerAndNotOwner() {
        Player payer = new Player(UUID.randomUUID(), "Payer", game, 1500);
        Player owner = new Player(UUID.randomUUID(), "Owner", game, 1500);
        Utility utility = Utility.builder()
                .owner(owner)
                .toPay(100)
                .build();

        assertTrue(game.processUtilityPayment(payer, utility));
        // Validate that the payer's balance was deducted by the correct payment amount
        assertEquals(1400, payer.getBalance());

        utility.setOwner(game.getGameBoard().getBank()); // Bank owns buildings no player owns
        assertFalse(game.processUtilityPayment(payer, utility));
    }

    @Test
    void testProcessPayment() {
        Player player = players.get(0);

        // Railroad Test
        Railroad r = (Railroad) Arrays.stream(game.getGameBoard().getFields()).filter(f -> f instanceof Railroad).findFirst().orElseThrow();
        when(player.getCurrentFieldIndex()).thenReturn(r.getFieldId());
        assertFalse(game.processPayment(player));

        // Property Test
        Property p = (Property) Arrays.stream(game.getGameBoard().getFields()).filter(f -> f instanceof Property).findFirst().orElseThrow();
        when(player.getCurrentFieldIndex()).thenReturn(p.getFieldId());
        assertFalse(game.processPayment(player));

        // Utility Test
        Utility u = (Utility) Arrays.stream(game.getGameBoard().getFields()).filter(f -> f instanceof Utility).findFirst().orElseThrow();
        when(player.getCurrentFieldIndex()).thenReturn(u.getFieldId());
        assertFalse(game.processPayment(player));

        when(player.getCurrentFieldIndex()).thenReturn(0); // Go field
        assertFalse(game.processPayment(player));
    }

    @Test
    void disconnectNotifier() {
        ArrayList<Player> disconnectedPlayers = new ArrayList<>();

        // This can only work with an actual player object
        Player player1 = new Player();
        player1.setId(UUID.randomUUID());
        player1.setName("Player1");

        Player player2 = new Player();
        player2.setId(UUID.randomUUID());
        player2.setName("Player2");

        DisconnectNotifier disconnectNotifier = disconnectedPlayers::add;

        game.setDisconnectNotifier(disconnectNotifier);
        assertEquals(disconnectNotifier, game.getDisconnectNotifier());

        game.join(player1);
        game.join(player2);

        // Test if disconnectNotifier is called
        game.playerDisconnected(player1);

        assertEquals(1,disconnectedPlayers.size());
        assertTrue(disconnectedPlayers.get(0).isComputerPlayer());
        assertEquals(player1, disconnectedPlayers.get(0));
        assertEquals(player2, game.getGameOwner());
    }

    @Test
    void getGameState() {
        assertNull(Game.getGameState(game.getGameId(), players.get(0)));
        game.join(players.get(0));
        assertEquals(Game.GameState.STARTED, Game.getGameState(game.getGameId(), players.get(0)));
    }

    @Test
    void winningPlayer() {
        Game g = new Game();
        // Winning player is null on create
        assertNull(g.getWinningPlayer());
    }

    @Test
    void roundAmount() {
        Game g = new Game();

        g.setRoundAmount(5);
        assertEquals(5, g.getRoundAmount());

        assertThrows(IllegalArgumentException.class, () -> game.setRoundAmount(0));
    }

    @Test
    void currentRound() {
        Game g = new Game();

        assertEquals(1, g.getCurrentRound());
    }

    @Test
    void selectWinningPlayer() {
        // Initialization
        Game g = new Game();

        Player p1 = new Player(UUID.randomUUID(), "Player 1", g, 5000);
        Player p2 = new Player(UUID.randomUUID(), "Player 2", g, 5000);

        g.join(p1);
        g.join(p2);

        // Player with higher balance is winner
        p1.addBalance(10);

        Property p = (Property) Arrays.stream(g.getGameBoard().getFields()).filter(field -> field instanceof Property).toList().get(0);
        Utility u = (Utility) Arrays.stream(g.getGameBoard().getFields()).filter(field -> field instanceof Utility).toList().get(0);
        Railroad r = (Railroad) Arrays.stream(g.getGameBoard().getFields()).filter(field -> field instanceof Railroad).toList().get(0);

        p.setOwner(p1);
        u.setOwner(p2);
        r.setOwner(p2);

        g.selectWinningPlayer();
        assertEquals(p2, g.getWinningPlayer());
        assertEquals(Game.GameState.ENDED, g.getState());
    }

    @Test
    void buyField() {
        // GO field is not buyable
        assertFalse(game.buyField(0, players.get(0)));

        // Field not owned and enough money
        when(players.get(0).getBalance()).thenReturn(10000);
        assertTrue(game.buyField(1, players.get(0)));

        // Field already owned
        when(players.get(1).getBalance()).thenReturn(10000);
        assertFalse(game.buyField(1, players.get(1)));

        // Field not owned and not enough money
        when(players.get(1).getBalance()).thenReturn(10);
        assertFalse(game.buyField(3, players.get(1)));
    }

    @Test
    void duplicateNameJoin() {
        Game g = new Game();

        when(players.get(0).getName()).thenReturn("NAME");

        assertTrue(g.join(players.get(0)));

        Player duplicateNamePlayer = Mockito.mock(Player.class);

        when(duplicateNamePlayer.getId()).thenReturn(UUID.randomUUID());
        when(duplicateNamePlayer.getName()).thenReturn("NAME");

        assertFalse(g.join(duplicateNamePlayer));
    }
}
