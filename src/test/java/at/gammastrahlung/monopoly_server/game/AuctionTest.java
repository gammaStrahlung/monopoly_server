package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuctionTests {

    private Auction auction;
    private Game game;
    private GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        game = new Game();
        gameBoard = new GameBoard();
        gameBoard.initializeGameBoard();
        auction = new Auction();
    }

    @Test
    void testAddBid() {
        Bid bid = new Bid(UUID.randomUUID(), 300, 5);
        auction.addBid(bid);
        List<Bid> bids = Auction.getBids();
        assertTrue(bids.contains(bid));
    }

    @Test
    void testEvaluateHighestBid() {
        auction.addBid(new Bid(UUID.randomUUID(), 200, 5));
        auction.addBid(new Bid(UUID.randomUUID(), 300, 5));
        auction.addBid(new Bid(UUID.randomUUID(), 100, 5));

        Bid highestBid = auction.evaluateHighestBids();
        assertNotNull(highestBid);
        assertEquals(300, highestBid.getAmount());
        assertTrue(Auction.getBids().isEmpty()); // Ensure bids are cleared after evaluation
    }


    @Test
    void testCheckCurrentFieldOwnedByBank() {
        // Assuming field index 1 is a property and initially owned by the bank
        assertTrue(auction.checkCurrentField(1));
    }


    @Test
    void testInvalidFieldIndex() {
        // Assuming the gameBoard implementation throws IllegalArgumentException for out of bounds
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            auction.checkCurrentField(999); // Index clearly out of bounds
        });
        assertTrue(exception.getMessage().contains("Index out of bounds"), "Expected IllegalArgumentException with an out of bounds message");
    }

    @Test
    void testNoBids() {
        Auction.getBids().clear(); // Ensure no bids are present at the start of the test
        Bid highestBid = auction.evaluateHighestBids();
        assertNull(highestBid, "Expected no highest bid when there are no bids.");
    }





}