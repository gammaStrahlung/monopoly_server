package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.network.websocket.MonopolyMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionTests {
    Game mockGame;
    GameBoard mockBoard;
    Auction auction;
    Player mockPlayer;
    Player mockPlayer2;

    @BeforeEach
    void setUp() {
        mockGame = mock(Game.class);
        mockBoard = mock(GameBoard.class);
        when(mockGame.getGameBoard()).thenReturn(mockBoard);
        MonopolyMessageHandler.currentGame = mockGame;
        auction = new Auction();
        mockPlayer = mock(Player.class);
        mockPlayer2 = mock(Player.class);
    }


    @Test
    void testCheckCurrentFieldNotProperty() {
        Field field = mock(Field.class);
        when(mockBoard.getFieldByIndex(11)).thenReturn(field);

        assertFalse(auction.checkCurrentField(-1));
    }

    @Test
    void testCheckCurrentFieldOwnedByAnotherPlayer() {
        Property property = mock(Property.class);
        Player anotherPlayer = mock(Player.class);
        when(mockBoard.getFieldByIndex(12)).thenReturn(property);
        when(property.getOwner()).thenReturn(anotherPlayer);
        when(mockBoard.getBank()).thenReturn(mock(Player.class));

        assertFalse(auction.checkCurrentField(-1));
    }

    @Test
    void testNoBids() {
        assertNull(auction.evaluateHighestBids());
    }





    @Test
    void checkCurrentFieldShouldReturnTrueWhenBankOwnsProperty() {
        int currentFieldIndex = 0;
        Player bank = mock(Player.class);
        Property property = mock(Property.class);
        when(mockBoard.getFieldByIndex(currentFieldIndex)).thenReturn(property);
        when(mockBoard.getBank()).thenReturn(bank);
        when(property.getOwner()).thenReturn(bank);

        assertTrue(auction.checkCurrentField(currentFieldIndex));
    }

    @Test
    public void testEvaluateHighestBids_NoBids() {
        assertTrue(auction.getBids().isEmpty());
        assertNull(auction.evaluateHighestBids());
    }

    @Test
    public void testEvaluateHighestBids_OneBid() {
        Bid bid = new Bid(UUID.randomUUID(), 100,  3 ); // playerId, amount
        Auction.getBids().add(bid);
        assertEquals(bid, auction.evaluateHighestBids());
        assertTrue(Auction.getBids().isEmpty());
    }

    @Test
    public void testEvaluateHighestBids_MultipleBids() {
        Auction.getBids().add(new Bid(UUID.randomUUID(), 100,3));
        Auction.getBids().add(new Bid(UUID.randomUUID(), 200,3));
        Auction.getBids().add(new Bid(UUID.randomUUID(), 150,3));
        Bid highestBid = auction.evaluateHighestBids();
        assertEquals(200, highestBid.getAmount());
        assertTrue(Auction.getBids().isEmpty());
    }

     


}