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

    UUID sampleId = UUID.randomUUID();
    Bid bid1 = new Bid(sampleId, 5000, 10);


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
    public void testGettersAndSetters() {
        assertEquals(sampleId, bid1.getPlayerId());
        assertEquals(5000, bid1.getAmount());
        assertEquals(10, bid1.getFieldIndex());
    }




}