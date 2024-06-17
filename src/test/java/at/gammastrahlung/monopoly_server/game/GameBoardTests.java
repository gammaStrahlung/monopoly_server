package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.CardType;
import at.gammastrahlung.monopoly_server.game.gameboard.EventCard;
import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameBoardTests {

    private GameBoard gameBoard;

    @BeforeEach
    public void setUp() {
        gameBoard = new GameBoard();
        gameBoard.initializeGameBoard();
        gameBoard.initializeChanceDeck();
        gameBoard.initializeCommunityChestDeck();
    }

    @Test
    void testInitialization() {
        assertNotNull(gameBoard.getBank());
        assertNotNull(gameBoard.getFields());
        assertEquals(40, gameBoard.getFields().length);
        assertEquals(40, gameBoard.getGameBoardSize());
        assertEquals("full_set", gameBoard.getFullSet());
        assertEquals("hotel", gameBoard.getHotel());
    }

    @Test
    void testBankOwnership() {
        // Check that the bank owns all properties and utilities initially
        for (Field field : gameBoard.getFields()) {
            if (field instanceof Property) {
                Player owner = ((Property) field).getOwner();
                assertNotNull(owner); // Ensure owner is not null
            } else if (field instanceof Utility) {
                Player owner = ((Utility) field).getOwner();
                assertNotNull(owner); // Ensure owner is not null
            }
        }
    }


    @Test
    void testCleanUp(){
        // Create UUIDs for players
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();

        // Create players with IDs
        Player player1 = new Player(player1Id, "Player 1", null, 0);
        Player player2 = new Player(player2Id, "Player 2", null, 0);

        ((Property) gameBoard.getFields()[1]).setOwner(player1);
        ((Property) gameBoard.getFields()[3]).setOwner(player2);

        // Call the cleanUpBoard method
        gameBoard.cleanUpBoard();

        // Check if every property has no owner
        for (Field field : gameBoard.getFields()) {
            if (field instanceof Property) {
                assertNull(((Property) field).getOwner());
            }
        }
    }

    @Test
    void testInitializeCommunityChestDeck() {
        assertEquals(15, gameBoard.getCommunityChestDeck().size());

        EventCard firstCard = gameBoard.getCommunityChestDeck().get(0);

        assertEquals("Go to Jail", firstCard.getDescription());
        assertEquals(CardType.GO_TO_JAIL, firstCard.getCardType());
        assertEquals(30, firstCard.getMoveToField());
    }

    @Test
    void testInitializeChanceDeck() {
        assertEquals(14, gameBoard.getChanceDeck().size());

        EventCard firstCard = gameBoard.getChanceDeck().get(0);

        assertEquals("Go to Jail", firstCard.getDescription());
        assertEquals(CardType.GO_TO_JAIL, firstCard.getCardType());
        assertEquals(30, firstCard.getMoveToField());
    }


    @Test
    void testBoardNameInitialization() {
        assertEquals("\uD83C\uDCCF️", gameBoard.getCommunityChestBoardName(), "Community Chest board name should have correct Unicode.");
        assertEquals("Go to Jail \uD83D\uDE94", gameBoard.getGoToJailBoardName(), "Go to Jail board name should have correct Unicode.");
        assertEquals("❓", gameBoard.getChanceBoardName(), "Chance board name should have correct Unicode.");
    }

    @Test
    void testFieldInitializationUsingStrings() {
        assertEquals("Community Chest", gameBoard.getFields()[2].getName(), "The name of the field should be set to Community Chest.");
        assertEquals("Jail", gameBoard.getFields()[10].getName(), "The name of the field should be set to Jail.");  // Updated to match actual initialization
        assertEquals("Chance", gameBoard.getFields()[7].getName(), "The name of the field should be set to Chance.");
    }

    @Test
    void testGetFieldByValidIndex() {
        Field field = gameBoard.getFieldByIndex(0);
        assertNotNull(field);
        assertEquals(0, field.getFieldId());
    }

    @Test
    void testGetFieldByNegativeIndex() {
        assertThrows(IllegalArgumentException.class, () -> gameBoard.getFieldByIndex(-1));
    }

    @Test
    void testGetFieldByIndexOutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> gameBoard.getFieldByIndex(gameBoard.getGameBoardSize()));
    }

    @Test
    void testGetFieldByBoundaryIndex() {
        Field field = gameBoard.getFieldByIndex(gameBoard.getGameBoardSize() - 1);
        assertNotNull(field);
        assertEquals(gameBoard.getGameBoardSize() - 1, field.getFieldId());
    }



}
