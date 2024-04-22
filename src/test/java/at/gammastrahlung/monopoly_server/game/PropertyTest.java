package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;

public class PropertyTest {
    private Property property;
    private Game currentGame;
    private GameBoard mockedGameBoard;

    @Test
    public void testBuyAndSellProperty() {
        currentGame = new Game();
        Player owner = new Player(UUID.randomUUID(),"OldOwner",currentGame, 1000);
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

    @Test
    public void testBuildHouse() {
        currentGame = new Game();
        mockedGameBoard = new GameBoard();
        Player owner = new Player(UUID.randomUUID(),"Owner", currentGame, 1000);
        Property property = Property.builder()
                .houseCost(100)
                .hotelCost(500)
                .houseCount(0)
                .owner(owner)
                .build();
        Property.setGameBoard(mockedGameBoard);

        property.buildHouse();
        assertEquals(1, property.getHouseCount());
        assertEquals(900, owner.getBalance());
    }



}
