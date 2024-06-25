package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.Test;


import java.util.UUID;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PropertyTest {
    @Test
    void testBuyAndSellProperty() {
        Game currentGame = new Game();
        Player owner = new Player(UUID.randomUUID(),"OldOwner", currentGame, 1000);
        Player buyer = new Player(UUID.randomUUID(), "NewOwner", currentGame, 1000);
        Property property = Property.builder()
                .price(200)
                .owner(owner)
                .build();

        property.buyAndSellProperty(buyer);

        assertEquals(buyer, property.getOwner());
        assertEquals(800, buyer.getBalance());
        assertEquals(1200, owner.getBalance());
    }
/*
    @Test
    void testBuildHouseFalse(){
        Game currentGame = new Game();
        Player player1 = new Player(UUID.randomUUID(),"player1", currentGame, 5000);
        Player player2 = new Player(UUID.randomUUID(), "player2", currentGame, 5000);
        currentGame.gameBoard.initializeGameBoard();
        Field[] currentGAmeGamebord = player1.getCurrentGame().getGameBoard().getFields();
        Property property1 = (Property) currentGAmeGamebord[1];
        Property property2 = (Property) currentGAmeGamebord[3];


        property1.setGameBoard(currentGame.getGameBoard()); // Ensure gameBoard is set
        property2.setGameBoard(currentGame.getGameBoard());

        property1.buyAndSellProperty(player1);
        property2.buyAndSellProperty(player2);

        property1.buildHouse();
        assertEquals(0,property1.getHouseCount());
        assertFalse(property1.buildHouse());
        assertFalse(property1.buildable());


    }*/

    @Test
    void testBuildHouse(){
        Game currentGame = new Game();
        Player player1 = new Player(UUID.randomUUID(),"player1", currentGame, 5000);
        currentGame.gameBoard.initializeGameBoard();
        Field[] currentGAmeGamebord = player1.getCurrentGame().getGameBoard().getFields();
        Property property1 = (Property) currentGAmeGamebord[1];
        Property property2 = (Property) currentGAmeGamebord[3];


        property1.setGameBoard(currentGame.getGameBoard()); // Ensure gameBoard is set
        property2.setGameBoard(currentGame.getGameBoard());

        property1.buyAndSellProperty(player1);
        property2.buyAndSellProperty(player1);

        property1.buildHouse();
        assertEquals(1,property1.getHouseCount());
        assertTrue(property1.buildHouse());


    }

    @Test
    void testSetGameBoard() {
        Game currentGame = new Game();
        Player player1 = new Player(UUID.randomUUID(),"player1", currentGame, 5000);
        currentGame.gameBoard.initializeGameBoard();
        Field[] currentGamebord = currentGame.getGameBoard().getFields();
        Property property1 = (Property) currentGamebord[1];
        assertNull(property1.getGameBoard());
        property1.buyAndSellProperty(player1);

        property1.setGameBoard(currentGame.getGameBoard());
        assertEquals(property1.getGameBoard(), property1.getOwner().currentGame.gameBoard);
    }

    @Test
    void testGetPropertyValue() {
        Property property = new Property();
        property.setPrice(100);
        property.setHouseCost(5);
        property.setHotelCost(10);

        // No houses -> only price
        assertEquals(100, property.getPropertyValue());

        // two houses -> price + 2 * houseCost
        property.setHouseCount(2);
        assertEquals(110, property.getPropertyValue());

        // 4 houses + 1 hotel -> price + 4 * houseCost + hotelCost
        property.setHouseCount(5);
        assertEquals(130, property.getPropertyValue());
    }
}