package at.gammastrahlung.monopoly_server.game;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.network.websocket.MonopolyMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;

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
        when(mockBoard.getFieldByIndex(anyInt())).thenReturn(field);

        assertFalse(auction.checkCurrentField(0));
    }

    @Test
    void testCheckCurrentFieldOwnedByAnotherPlayer() {
        Property property = mock(Property.class);
        Player anotherPlayer = mock(Player.class);
        when(mockBoard.getFieldByIndex(anyInt())).thenReturn(property);
        when(property.getOwner()).thenReturn(anotherPlayer);
        when(mockBoard.getBank()).thenReturn(mock(Player.class));

        assertFalse(auction.checkCurrentField(0));
    }



    @Test
    void testGetBids() {
        Bid bid1 = new Bid(UUID.randomUUID(), 100, 1);
        Bid bid2 = new Bid(UUID.randomUUID(), 200, 1);
        auction.addBid(bid1);
        auction.addBid(bid2);
        assertEquals(2, Auction.getBids().size());
    }
}