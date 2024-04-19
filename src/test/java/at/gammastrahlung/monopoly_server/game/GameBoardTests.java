package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameBoardTests {

    private GameBoard gameBoard;

    @BeforeEach
    public void setUp() {
        gameBoard = GameBoard.builder().size(40).build();
        gameBoard.initializeGameBoard();
    }



    @Test
    public void testInitialization() {
        assertNotNull(gameBoard.getBank());
        assertNotNull(gameBoard.getGameBoard());
        assertEquals(40, gameBoard.getGameBoard().length);
    }

    @Test
    public void testCleanUp(){
        // Set owners for some properties
        Player player1 = new Player();
        Player player2 = new Player();
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
}
