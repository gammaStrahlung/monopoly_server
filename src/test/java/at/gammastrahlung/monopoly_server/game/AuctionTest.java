package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;
import at.gammastrahlung.monopoly_server.network.websocket.MonopolyMessageHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuctionTest {
    Game mockGame;
    GameBoard mockBoard;
    Auction auction;
    Player mockPlayer;
    Player mockPlayer2;
    private Bid mockBid;
    private GameBoard mockGameBoard;

    private Property mockProperty;
    private Railroad mockRailroad;
    private Utility mockUtility;

    @BeforeEach
    void setUp() {
        mockGame = mock(Game.class);
        mockBoard = mock(GameBoard.class);
        when(mockGame.getGameBoard()).thenReturn(mockBoard);
        MonopolyMessageHandler.currentGame = mockGame;
        auction = new Auction();
        auction.setGameBoard(mockBoard);

        mockPlayer = mock(Player.class);
        mockPlayer2 = mock(Player.class);
        mockGame = mock(Game.class);
        mockGameBoard = mock(GameBoard.class);
        mockGameBoard.initializeGameBoard();
        mockPlayer = mock(Player.class);
        mockProperty = mock(Property.class);
        mockRailroad = mock(Railroad.class);
        mockUtility = mock(Utility.class);
        mockBid = mock(Bid.class);
        when(mockGame.getGameBoard()).thenReturn(mockGameBoard);

        MonopolyMessageHandler.currentGame = mockGame;



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

//    @Test
//    void testNoBids() {
//
//
//        Auction auction1 = new Auction();
//
//
//        auction1.addBid(null);
//        assertNull(auction1.evaluateHighestBids());
//
//    }





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

//    @Test
//    public void testEvaluateHighestBids_OneBid() {
//        Bid bid = new Bid(UUID.randomUUID(), 100,  3, mockPlayer ); // playerId, amount
//        Auction.getBids().add(bid);
//        assertEquals(bid, auction.evaluateHighestBids());
//        assertTrue(Auction.getBids().isEmpty());
//    }
//
//    @Test
//    public void testEvaluateHighestBids_MultipleBids() {
//        Auction.getBids().clear();
//        Auction.getBids().add(new Bid(UUID.randomUUID(), 100,3, mockPlayer));
//        Auction.getBids().add(new Bid(UUID.randomUUID(), 200,3, mockPlayer));
//        Auction.getBids().add(new Bid(UUID.randomUUID(), 150,3, mockPlayer));
//        Bid highestBid = auction.evaluateHighestBids();
//        assertEquals(200, highestBid.getAmount());
//        assertTrue(Auction.getBids().isEmpty());
//    }
//
//
//    @Test
//    void testAddBid() {
//
//        Bid bid = new Bid(UUID.randomUUID(), 100, 1, mockPlayer);
//        auction.addBid(bid);
//        assertEquals(1, auction.getBids().size());
//        assertEquals(bid, auction.getBids().get(0));
//    }

//    @Test
//    void testBuyCurrentField_Property() {
//
//        mockStatic(Game.class);
//        mockStatic(Auction.class);
//        mockGameBoard.initializeGameBoard();
//        Auction.setGame(mockGame);
//
//        Auction auction1 = new Auction();
//        auction1.setGameBoard(mockGameBoard);
//
//        when(Game.getPlayerListForId()).thenReturn(new ArrayList<>());
//        when(Auction.getCurrentFieldIndexforBuying()).thenReturn(0);
//        // Setup Mocks
//        List<Player> playerList = new ArrayList<>();
//        playerList.add(mockPlayer);
//
//        when(Game.getPlayerListForId()).thenReturn(playerList);
//
//
//        when(mockGameBoard.getFieldByIndex(0)).thenReturn(mockProperty);
//        when(mockProperty.getOwner()).thenReturn(null);
//
//
//
//
//        // Call method under test
//        auction1.buyCurrentField(mockPlayer);
//
//        // Verify interactions
//        verify(mockProperty).buyAndSellProperty(mockPlayer);
//    }

    @Test
    void testBuyCurrentField_Railroad() {

        mockStatic(Game.class);
        mockStatic(Auction.class);
        mockGameBoard.initializeGameBoard();
        Auction.setGame(mockGame);

        Auction auction1 = new Auction();
        auction1.setGameBoard(mockGameBoard);

        when(Game.getPlayerListForId()).thenReturn(new ArrayList<>());
        when(Auction.getCurrentFieldIndexforBuying()).thenReturn(0);
        // Setup Mocks
        List<Player> playerList = new ArrayList<>();
        playerList.add(mockPlayer);

        when(Game.getPlayerListForId()).thenReturn(playerList);


        when(mockGameBoard.getFieldByIndex(0)).thenReturn(mockRailroad);
        when(mockRailroad.getOwner()).thenReturn(null);




        // Call method under test
        auction1.buyCurrentField(mockPlayer);

        // Verify interactions
        verify(mockRailroad).buyAndSellRailroad(mockPlayer);
    }

//    @Test
//    void testBuyCurrentField_Utility() {
//        mockStatic(Game.class);
//        mockStatic(Auction.class);
//        mockGameBoard.initializeGameBoard();
//        Auction.setGame(mockGame);
//
//        Auction auction1 = new Auction();
//        auction1.setGameBoard(mockGameBoard);
//
//        when(Game.getPlayerListForId()).thenReturn(new ArrayList<>());
//        when(Auction.getCurrentFieldIndexforBuying()).thenReturn(0);
//        // Setup Mocks
//        List<Player> playerList = new ArrayList<>();
//        playerList.add(mockPlayer);
//
//        when(Game.getPlayerListForId()).thenReturn(playerList);
//
//
//        when(mockGameBoard.getFieldByIndex(0)).thenReturn(mockUtility);
//        when(mockUtility.getOwner()).thenReturn(null);
//
//
//
//
//        // Call method under test
//        auction1.buyCurrentField(mockPlayer);
//
//        // Verify interactions
//        verify(mockUtility).buyAndSellUtility(mockPlayer);
//    }

    @Test
    void testBuyCurrentField_NotOwned() {
        when(mockGameBoard.getFieldByIndex(2)).thenReturn(mockProperty);
        when(mockProperty.getOwner()).thenReturn(null);
        auction.buyCurrentField(mockPlayer);
        verify(mockProperty, times(0)).buyAndSellProperty(any());
    }

//    @Test
//    void testEvaluateHighestBids_Property() {
//        Bid bid = new Bid(UUID.randomUUID(), 100, 1, mockPlayer);
//        auction.addBid(bid);
//        when(mockGameBoard.getFieldByIndex(2)).thenReturn(mockProperty);
//        when(mockProperty.getOwner()).thenReturn(null);
//        auction.evaluateHighestBids();
//        verify(mockProperty, times(1)).setBidValue(bid.getAmount());
//        verify(mockProperty, times(1)).setBidActivated(true);
//        verify(mockProperty, times(1)).buyAndSellProperty(mockPlayer);
//    }




}