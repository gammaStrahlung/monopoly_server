package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;
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

        // Reset properties to ensure a clean state for each test
        for (Field field : gameBoard.getGameBoard()) {
            if (field instanceof Property) {
                Property property = (Property) field;
                property.setHouseCount(0);
                property.setHasHotel(false);
            }
        }
    }

    @Test
    void testRailroadInitialization() {
        Field railroad = gameBoard.getGameBoard()[5]; // Assuming index 5 is a railroad
        assertTrue(railroad instanceof Railroad, "Field at index 5 should be a Railroad.");
    }

    @Test
    void testUtilityInitialization() {
        Field utility = gameBoard.getGameBoard()[12]; // Assuming index 12 is a utility
        assertTrue(utility instanceof Utility, "Field at index 12 should be a Utility.");
    }

    @Test
    void testPropertyRentPricesInitialization() {
        Property property = (Property) gameBoard.getGameBoard()[1];
        assertNotNull(property.getRentPrices(), "Rent prices should be initialized.");
        assertFalse(property.getRentPrices().isEmpty(), "Rent prices map should not be empty.");
    }

    @Test
    void testCommunityChestCardInitialization() {
        EventCard card = gameBoard.getCommunityChestDeck().get(0);
        assertNotNull(card.getDescription(), "First community chest card should have a description.");
        assertNotNull(card.getCardType(), "First community chest card should have a card type.");
    }

    @Test
    void testChanceCardInitialization() {
        EventCard card = gameBoard.getChanceDeck().get(0);
        assertNotNull(card.getDescription(), "First chance card should have a description.");
        assertNotNull(card.getCardType(), "First chance card should have a card type.");
    }

    @Test
    void testResettingCommunityChestDeck() {
        gameBoard.initializeCommunityChestDeck(); // Reinitialize to reset any changes
        assertEquals(15, gameBoard.getCommunityChestDeck().size(), "Resetting should result in 15 cards again.");
    }

    @Test
    void testResettingChanceDeck() {
        gameBoard.initializeChanceDeck(); // Reinitialize to reset any changes
        assertEquals(14, gameBoard.getChanceDeck().size(), "Resetting should result in 14 cards again.");
    }

    @Test
    void testGetInvalidPropertyById() {
        assertNull(gameBoard.getPropertyById(-1), "Should return null for invalid property ID.");
        assertNull(gameBoard.getPropertyById(100), "Should return null for non-existent property ID.");
    }

    @Test
    void testCorrectNumberOfCommunityChestCards() {
        assertEquals(15, gameBoard.getCommunityChestDeck().size(), "There should be exactly 15 Community Chest cards.");
    }

    @Test
    void testCorrectNumberOfChanceCards() {
        assertEquals(14, gameBoard.getChanceDeck().size(), "There should be exactly 14 Chance cards.");
    }

    @Test
    void testCorrectFieldInitialization() {
        Field goField = gameBoard.getGameBoard()[0];
        assertEquals(FieldType.GO, goField.getType(), "The first field should be GO.");
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
    void testCleanUp() {
        UUID player1Id = UUID.randomUUID();
        UUID player2Id = UUID.randomUUID();

        Player player1 = new Player(player1Id, "Player 1", null, 0);
        Player player2 = new Player(player2Id, "Player 2", null, 0);

        ((Property) gameBoard.getGameBoard()[1]).setOwner(player1);
        ((Property) gameBoard.getGameBoard()[3]).setOwner(player2);

        gameBoard.cleanUpBoard();

        for (Field field : gameBoard.getGameBoard()) {
            if (field instanceof Property) {
                assertNull(((Property) field).getOwner());
            }
        }
    }

    @Test
    public void getPropertyById_ShouldReturnProperty_WhenIdIsValid() {
        int validId = 1; // Assuming this is a valid ID.
        Property result = gameBoard.getPropertyById(validId);
        assertNotNull(result, "Property with ID " + validId + " should exist.");
    }

    @Test
    public void getPropertyById_ShouldReturnNull_WhenIdIsInvalid() {
        int invalidId = 999; // An ID that is definitely not present.
        Property result = gameBoard.getPropertyById(invalidId);
        assertNull(result, "No property object should be returned for an invalid ID.");
    }

    @Test
    public void hasBuildings_ShouldReturnFalse_WhenNoPropertiesInGroupHaveBuildings() {
        PropertyColor groupColor = PropertyColor.RED; // Replace this with the actual group color without buildings.
        boolean result = gameBoard.hasBuildings(groupColor);
        assertFalse(result, "It should be false if no buildings are present in the group.");
    }

    @Test
    public void hasBuildings_ShouldReturnTrue_WhenPropertiesInGroupHaveBuildings() {
        ((Property) gameBoard.getGameBoard()[21]).setHouseCount(1); // Ensure this index corresponds to a RED property
        PropertyColor groupColor = PropertyColor.RED; // Assume this color is handled in the test
        boolean result = gameBoard.hasBuildings(groupColor);
        assertTrue(result, "It should be true if buildings are present in the group.");
    }

    @Test
    public void anyMortgagedInGroup_ShouldReturnFalse_WhenNoPropertiesInGroupAreMortgaged() {
        PropertyColor groupColor = PropertyColor.RED; // Replace this with the actual group color that is not mortgaged.
        boolean result = gameBoard.anyMortgagedInGroup(groupColor);
        assertFalse(result, "It should be false if no properties in the group are mortgaged.");


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
}