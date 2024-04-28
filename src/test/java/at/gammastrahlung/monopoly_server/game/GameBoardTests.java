package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.PropertyColor;
import at.gammastrahlung.monopoly_server.game.gameboard.Utility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class GameBoardTests {

    private GameBoard gameBoard;

    @BeforeEach
    public void setUp() {
        gameBoard = GameBoard.builder().build();
        gameBoard.initializeGameBoard();
    }
    @BeforeEach
    public void setup() {
        gameBoard = new GameBoard();
        gameBoard.initializeGameBoard(); // Ensure this method exists and works correctly.
        // Manually adding buildings to a property of the RED color group.
        Property redProperty = (Property) gameBoard.getGameBoard()[21]; // Example index for a red property.
        redProperty.setHouseCount(1); // Adds one house.
    }


    @Test
    void testInitialization() {
        assertNotNull(gameBoard.getBank());
        assertNotNull(gameBoard.getGameBoard());
        assertEquals(40, gameBoard.getGameBoard().length);
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
    public void getPropertyById_ShouldReturnProperty_WhenIdIsValid() {
        // Arrange
        int validId = 1; // Assuming this is a valid ID.
        // Act
        Property result = gameBoard.getPropertyById(validId);
        // Assert
        assertNotNull(result, "Property with ID " + validId + " should exist.");
    }

    @Test
    public void getPropertyById_ShouldReturnNull_WhenIdIsInvalid() {
        // Arrange
        int invalidId = 999; // An ID that is definitely not present.
        // Act
        Property result = gameBoard.getPropertyById(invalidId);
        // Assert
        assertNull(result, "No property object should be returned for an invalid ID.");
    }




    @Test
    public void hasBuildings_ShouldReturnFalse_WhenNoPropertiesInGroupHaveBuildings() {
        // Arrange
        PropertyColor groupColor = PropertyColor.RED; // Replace this with the actual group color without buildings.
        // Act
        boolean result = gameBoard.hasBuildings(groupColor);
        // Assert
        assertFalse(result, "It should be false if no buildings are present in the group.");
    }


    @Test
    public void hasBuildings_ShouldReturnTrue_WhenPropertiesInGroupHaveBuildings() {
        // Arrange
        // You need to ensure that the properties in the test have buildings.
        PropertyColor groupColor = PropertyColor.RED; // Replace this with the actual group color.
        // Act
        boolean result = gameBoard.hasBuildings(groupColor);
        // Assert
        assertTrue(result, "It should be true if buildings are present in the group.");
    }







}
