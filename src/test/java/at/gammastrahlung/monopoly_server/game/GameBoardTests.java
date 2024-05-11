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
        gameBoard = GameBoard.builder().build();
        gameBoard.initializeGameBoard();
        gameBoard.initializeChanceDeck();
        gameBoard.initializeCommunityChestDeck();
    }

    @Test
    void testInitialization() {
        assertNotNull(gameBoard.getBank());
        assertNotNull(gameBoard.getGameBoard());
        assertEquals(40, gameBoard.getGameBoard().length);
        assertEquals(40, gameBoard.getGAME_BOARD_SIZE());
        assertEquals("full_set", gameBoard.getFULL_SET());
        assertEquals("hotel", gameBoard.getHOTEL());
    }

    @Test
    void testBankOwnership() {
        // Check that the bank owns all properties and utilities initially
        for (Field field : gameBoard.getGameBoard()) {
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

        ((Property) gameBoard.getGameBoard()[1]).setOwner(player1);
        ((Property) gameBoard.getGameBoard()[3]).setOwner(player2);

        // Call the cleanUpBoard method
        gameBoard.cleanUpBoard();

        // Check if every property has no owner
        for (Field field : gameBoard.getGameBoard()) {
            if (field instanceof Property) {
                assertNull(((Property) field).getOwner());
            }
        }
    }

    @Test
    public void testInitializeCommunityChestDeck() {
        assertEquals(15, gameBoard.getCommunityChestDeck().size());

        EventCard firstCard = gameBoard.getCommunityChestDeck().get(0);

        assertEquals("Go to Jail", firstCard.getDescription());
        assertEquals(CardType.GO_TO_JAIL, firstCard.getCardType());
        assertEquals(30, firstCard.getMoveToField());
    }

    @Test
    public void testInitializeChanceDeck() {
        assertEquals(14, gameBoard.getChanceDeck().size());

        EventCard firstCard = gameBoard.getChanceDeck().get(0);

        assertEquals("Go to Jail", firstCard.getDescription());
        assertEquals(CardType.GO_TO_JAIL, firstCard.getCardType());
        assertEquals(30, firstCard.getMoveToField());
    }
    @Test
    void testGetPropertyByIdValid() {
        // Test for a valid property ID that exists on the board
        int validPropertyId = 1; // Angenommen, dies ist eine gültige ID für "Mediterranean Avenue"
        Property property = gameBoard.getPropertyById(validPropertyId);
        assertNotNull(property, "Property should not be null for a valid ID.");
        assertEquals(validPropertyId, property.getFieldId(), "Property ID should match the requested ID.");
    }

    @Test
    void testGetPropertyByIdInvalid() {
        // Test for an invalid property ID that does not exist on the board
        int invalidPropertyId = 100; // Eine ID, die definitiv nicht auf dem Brett existiert
        Property property = gameBoard.getPropertyById(invalidPropertyId);
        assertNull(property, "Property should be null for an invalid ID.");
    }
}
