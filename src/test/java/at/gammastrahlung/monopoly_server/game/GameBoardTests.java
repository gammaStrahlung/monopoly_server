package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
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














}
