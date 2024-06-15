package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class BidTest {

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

    @Test
    void testBidInitialization() {
        UUID playerId = UUID.randomUUID();
        int amount = 150;
        int fieldIndex = 5;

        Bid bid = new Bid(playerId, amount, fieldIndex);

        assertEquals(playerId, bid.getPlayerId(), "Player ID should match the provided UUID.");
        assertEquals(amount, bid.getAmount(), "Bid amount should match the provided value.");
        assertEquals(fieldIndex, bid.getFieldIndex(), "Field index should match the provided value.");
    }

    @Test
    void testSettersAndGetters() {
        Bid bid = new Bid(UUID.randomUUID(), 100, 10);

        bid.setAmount(200);
        assertEquals(200, bid.getAmount(), "Setter for amount should update the amount in the Bid object.");

        UUID newPlayerId = UUID.randomUUID();
        bid.setPlayerId(newPlayerId);
        assertEquals(newPlayerId, bid.getPlayerId(), "Setter for playerId should update the playerId in the Bid object.");

        int newFieldIndex = 20;
        bid.setFieldIndex(newFieldIndex);
        assertEquals(newFieldIndex, bid.getFieldIndex(), "Setter for fieldIndex should update the fieldIndex in the Bid object.");
    }
    @Test
    void testEqualityAndHashCode() {
        UUID playerId = UUID.randomUUID();
        Bid bid1 = new Bid(playerId, 300, 8);
        Bid bid2 = new Bid(playerId, 300, 8);

        assertEquals(bid1, bid2, "Two bids with the same state should be equal.");
        assertEquals(bid1.hashCode(), bid2.hashCode(), "Hash codes of two equal bids should also be equal.");
    }

    @Test
    void testToString() {
        UUID playerId = UUID.randomUUID();
        Bid bid = new Bid(playerId, 500, 2);

        String expectedString = "Bid(playerId=" + playerId + ", amount=500, fieldIndex=2)";
        assertTrue(bid.toString().contains(expectedString), "ToString should contain the correct formatting and data.");
    }



}