package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.Railroad;
import at.gammastrahlung.monopoly_server.game.gameboard.Utility;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PaymentSystemTest {

    @Mock
    private GameBoard gameBoard;

    @Mock
    private Player payer, owner;

    @InjectMocks
    private PaymentSystem paymentSystem;

    @Test
    void testProcessRailroadPaymentOwnerIsPayer() {
        Railroad railroad = mock(Railroad.class);
        when(railroad.getOwner()).thenReturn(payer);
        assertFalse(paymentSystem.processRailroadPayment(payer, railroad));
    }
    @Test
    void testProcessPropertyPaymentOwnerIsPayer() {
        Property property = mock(Property.class);
        when(property.getOwner()).thenReturn(payer);

        assertFalse(paymentSystem.processPropertyPayment(payer, property));
    }
    @Test
    void testMakePaymentInsufficientFunds() {
        when(payer.getBalance()).thenReturn(50);
        assertFalse(paymentSystem.makePayment(payer, owner, 100));
        verify(payer, never()).subtractBalance(anyInt());
        verify(owner, never()).addBalance(anyInt());
    }
    @Test
    void testMakePaymentSufficientFunds() {
        when(payer.getBalance()).thenReturn(200);

        assertTrue(paymentSystem.makePayment(payer, owner, 100));
        verify(payer).subtractBalance(100);
        verify(owner).addBalance(100);
    }
    @Test
    void testProcessRailroadPaymentMultipleRailroads() {
        Railroad railroad = mock(Railroad.class);
        when(railroad.getOwner()).thenReturn(null); // No owner
        when(railroad.getRentPrices()).thenReturn(Map.of("2RR", 50));
        when(gameBoard.getGameBoard()).thenReturn(new Field[]{railroad, railroad}); // Simulate 2 owned railroads

        assertFalse(paymentSystem.processRailroadPayment(payer, railroad)); // Expecting payment failure
    }
    @Test
    void testProcessUtilityPaymentValidPayment() {
        Utility utility = mock(Utility.class);
        when(utility.getOwner()).thenReturn(null); // No owner
        when(utility.getToPay()).thenReturn(75);

        assertFalse(paymentSystem.processUtilityPayment(payer, utility)); // Expecting payment failure
    }
    @Test
    void testProcessPropertyPayment() {
        Property property = mock(Property.class);
        when(property.getOwner()).thenReturn(null); // No owner
        when(property.getRentPrices()).thenReturn(Map.of("0", 50)); // Using a map with string keys

        assertFalse(paymentSystem.processPropertyPayment(payer, property)); // Expecting payment failure
    }

    @Test
    void testMakePaymentEdgeCaseExactFunds() {
        when(payer.getBalance()).thenReturn(100);
        assertTrue(paymentSystem.makePayment(payer, owner, 100));
        verify(payer).subtractBalance(100);
        verify(owner).addBalance(100);
    }

    @Test
    void testMakePaymentEdgeCaseInsufficientFunds() {
        when(payer.getBalance()).thenReturn(99);
        assertFalse(paymentSystem.makePayment(payer, owner, 100));
        verify(payer, never()).subtractBalance(anyInt());
        verify(owner, never()).addBalance(anyInt());
    }


}


