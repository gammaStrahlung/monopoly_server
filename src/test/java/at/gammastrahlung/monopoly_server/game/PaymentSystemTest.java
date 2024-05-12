package at.gammastrahlung.monopoly_server.game;

import static org.junit.jupiter.api.Assertions.*;

import at.gammastrahlung.monopoly_server.game.Game;
import at.gammastrahlung.monopoly_server.game.PaymentSystem;
import at.gammastrahlung.monopoly_server.game.Player;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.Railroad;
import at.gammastrahlung.monopoly_server.game.gameboard.Utility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;


class PaymentSystemTest {

    @Test
    void testProcessRailroadPaymentSuccess() {
        Game gameMock = mock(Game.class);
        Player payer = mock(Player.class);
        Player owner = mock(Player.class);
        Railroad railroad = mock(Railroad.class);
        when(railroad.getOwner()).thenReturn(owner);
        when(gameMock.processRailroadPayment(payer, railroad)).thenReturn(true);

        PaymentSystem system = new PaymentSystem(gameMock);
        assertTrue(system.processRailroadPayment(payer, railroad), "Payment should succeed when the owner is not the payer.");
    }

    @Test
    void testProcessRailroadPaymentFailure() {
        Game gameMock = mock(Game.class);
        Player payer = mock(Player.class);
        Railroad railroad = mock(Railroad.class);
        when(railroad.getOwner()).thenReturn(payer);
        when(gameMock.processRailroadPayment(payer, railroad)).thenReturn(false);

        PaymentSystem system = new PaymentSystem(gameMock);
        assertFalse(system.processRailroadPayment(payer, railroad), "Payment should fail when the payer owns the railroad.");
    }

    @Test
    void testProcessUtilityPaymentSuccess() {
        Game gameMock = mock(Game.class);
        Player payer = mock(Player.class);
        Player owner = mock(Player.class);
        Utility utility = mock(Utility.class);
        when(utility.getOwner()).thenReturn(owner);
        when(gameMock.processUtilityPayment(payer, utility)).thenReturn(true);

        PaymentSystem system = new PaymentSystem(gameMock);
        assertTrue(system.processUtilityPayment(payer, utility), "Payment should succeed when the owner is not the payer.");
    }

    @Test
    void testProcessUtilityPaymentFailure() {
        Game gameMock = mock(Game.class);
        Player payer = mock(Player.class);
        Utility utility = mock(Utility.class);
        when(utility.getOwner()).thenReturn(payer);
        when(gameMock.processUtilityPayment(payer, utility)).thenReturn(false);

        PaymentSystem system = new PaymentSystem(gameMock);
        assertFalse(system.processUtilityPayment(payer, utility), "Payment should fail when the payer owns the utility.");
    }

    @Test
    void testProcessPropertyPaymentSuccess() {
        Game gameMock = mock(Game.class);
        Player payer = mock(Player.class);
        Player owner = mock(Player.class);
        Property property = mock(Property.class);
        when(property.getOwner()).thenReturn(owner);
        when(gameMock.processPropertyPayment(payer, property)).thenReturn(true);

        PaymentSystem system = new PaymentSystem(gameMock);
        assertTrue(system.processPropertyPayment(payer, property), "Payment should succeed when the owner is not the payer.");
    }

    @Test
    void testProcessPropertyPaymentFailure() {
        Game gameMock = mock(Game.class);
        Player payer = mock(Player.class);
        Property property = mock(Property.class);
        when(property.getOwner()).thenReturn(payer);
        when(gameMock.processPropertyPayment(payer, property)).thenReturn(false);

        PaymentSystem system = new PaymentSystem(gameMock);
        assertFalse(system.processPropertyPayment(payer, property), "Payment should fail when the payer owns the property.");
    }

    @Test
    void testMakePaymentSuccess() {
        Game gameMock = mock(Game.class);
        Player from = mock(Player.class);
        Player to = mock(Player.class);
        when(gameMock.makePayment(from, to, 100)).thenReturn(true);

        PaymentSystem system = new PaymentSystem(gameMock);
        assertTrue(system.makePayment(from, to, 100), "Payment should succeed when the payer has sufficient funds.");
    }

    @Test
    void testMakePaymentFailure() {
        Game gameMock = mock(Game.class);
        Player from = mock(Player.class);
        Player to = mock(Player.class);
        when(gameMock.makePayment(from, to, 100)).thenReturn(false);

        PaymentSystem system = new PaymentSystem(gameMock);
        assertFalse(system.makePayment(from, to, 100), "Payment should fail when the payer does not have sufficient funds.");
    }
}