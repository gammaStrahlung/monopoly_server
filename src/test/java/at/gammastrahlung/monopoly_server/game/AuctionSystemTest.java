package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import at.gammastrahlung.monopoly_server.game.gameboard.Property;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuctionSystemTest {

    @Mock
    private Property auctionProperty;

    @Mock
    private Player player;

    @Mock
    private AuctionSystem.AuctionEventListener eventListener;

    @InjectMocks
    private AuctionSystem auctionSystem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        auctionSystem = new AuctionSystem(auctionProperty, eventListener);
        when(player.getBalance()).thenReturn(1000); // Assuming the player has enough balance for most tests
    }

    @Test
    void testPlaceValidBid() {
        assertTrue(auctionSystem.placeBid(player, 500), "Player should successfully place a bid.");
        verify(eventListener).onBidUpdated(player, 500);
    }

    @Test
    void testPlaceBidWithInsufficientFunds() {
        when(player.getBalance()).thenReturn(100); // Not enough balance
        assertThrows(IllegalArgumentException.class, () -> auctionSystem.placeBid(player, 500),
                "Placing a bid with insufficient funds should throw IllegalArgumentException.");
    }

    @Test
    void testPlaceLowerBidThanHighest() {
        auctionSystem.placeBid(player, 300); // Set an initial bid
        assertFalse(auctionSystem.placeBid(player, 200), "Placing a lower bid than the highest should fail.");
    }

    @Test
    void testFinalizeAuction() {
        auctionSystem.placeBid(player, 500);
        Player winner = auctionSystem.finalizeAuction();
        assertEquals(player, winner, "The correct player should win the auction.");
        verify(eventListener).onAuctionFinalized(player, 500);
        verify(auctionProperty).setOwner(player);
        verify(player).subtractBalance(500);

}}