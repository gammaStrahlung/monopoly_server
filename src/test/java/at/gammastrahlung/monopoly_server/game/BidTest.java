package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class BidTests {

    private Bid bid;
    private UUID playerId;
    private int amount;
    private int fieldIndex;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        amount = 500;
        fieldIndex = 10;
        bid = new Bid(playerId, amount, fieldIndex);
    }

    @Test
    void testCreateBid() {
        assertNotNull(bid);
        assertEquals(playerId, bid.getPlayerId());
        assertEquals(amount, bid.getAmount());
        assertEquals(fieldIndex, bid.getFieldIndex());
    }

    @Test
    void testChangeBidAmount() {
        // Assume there is a method to update the bid amount
        int newAmount = 1000;
        bid.setAmount(newAmount);
        assertEquals(newAmount, bid.getAmount());
    }



    @Test
    void testChangeFieldIndex() {
        // Changing field index to simulate a bid on a different property
        int newFieldIndex = 20;
        bid.setFieldIndex(newFieldIndex);
        assertEquals(newFieldIndex, bid.getFieldIndex());
    }

  
}