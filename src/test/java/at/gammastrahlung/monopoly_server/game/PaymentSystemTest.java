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

    @Test
    void testProcessRailroadPaymentOwnerNotPayer() {
        Railroad railroad = mock(Railroad.class);
        when(railroad.getOwner()).thenReturn(owner);
        when(railroad.getRentPrices()).thenReturn(Map.of("1RR", 25));
        when(payer.getBalance()).thenReturn(50);
        when(owner.getBalance()).thenReturn(50);
        when(gameBoard.getGameBoard()).thenReturn(new Field[]{railroad}); // Only one owned railroad

        assertTrue(paymentSystem.processRailroadPayment(payer, railroad));
        verify(payer).subtractBalance(25);
        verify(owner).addBalance(25);
    }

    @Test
    void testProcessUtilityPaymentOwnerNotPayer() {
        Utility utility = mock(Utility.class);
        when(utility.getOwner()).thenReturn(owner);
        when(utility.getToPay()).thenReturn(75);
        when(payer.getBalance()).thenReturn(100);
        when(owner.getBalance()).thenReturn(50);

        assertTrue(paymentSystem.processUtilityPayment(payer, utility));
        verify(payer).subtractBalance(75);
        verify(owner).addBalance(75);
    }

    @Test
    void testProcessPaymentZeroFunds() {
        when(payer.getBalance()).thenReturn(0);
        assertFalse(paymentSystem.makePayment(payer, owner, 100));
        verify(payer, never()).subtractBalance(anyInt());
        verify(owner, never()).addBalance(anyInt());
    }



    @Test
    void testProcessRailroadPaymentWithNoRailroadsOwned() {
        Railroad railroad = mock(Railroad.class);
        when(railroad.getOwner()).thenReturn(owner);
        when(gameBoard.getGameBoard()).thenReturn(new Field[]{});
        when(railroad.getRentPrices()).thenReturn(Map.of("1RR", 25)); // Rent with 1 railroad, but none owned

        assertFalse(paymentSystem.processRailroadPayment(payer, railroad));
    }
    @Test
    void testProcessUtilityPaymentWithHighPayment() {
        Utility utility = mock(Utility.class);
        when(utility.getOwner()).thenReturn(owner);
        when(utility.getToPay()).thenReturn(150); // Höherer Zahlungsbetrag
        when(payer.getBalance()).thenReturn(200);
        when(owner.getBalance()).thenReturn(50);

        assertTrue(paymentSystem.processUtilityPayment(payer, utility));
        verify(payer).subtractBalance(150);
        verify(owner).addBalance(150);
    }

    @Test
    void testProcessUtilityPaymentWhenOwnerIsPayer() {
        Utility utility = mock(Utility.class);
        when(utility.getOwner()).thenReturn(payer); // Zahler ist der Eigentümer
        when(utility.getToPay()).thenReturn(75);

        assertFalse(paymentSystem.processUtilityPayment(payer, utility));
    }
//    @Test
//    void testCountOwnedRailroadsVariousOwners() {
//        Railroad railroad1 = mock(Railroad.class);
//        Railroad railroad2 = mock(Railroad.class);
//        Railroad railroad3 = mock(Railroad.class);
//        when(railroad1.getOwner()).thenReturn(owner);
//        when(railroad2.getOwner()).thenReturn(payer);
//        when(railroad3.getOwner()).thenReturn(null); // Kein Eigentümer
//
//        when(gameBoard.getGameBoard()).thenReturn(new Field[]{railroad1, railroad2, railroad3, railroad1});
//
//        assertEquals(2, paymentSystem.countOwnedRailroads(owner));
//        assertEquals(1, paymentSystem.countOwnedRailroads(payer));
//        assertEquals(0, paymentSystem.countOwnedRailroads(null)); // Kein Besitz für null Eigentümer
//    }


}


