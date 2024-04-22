package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.UUID;
import java.util.stream.Stream;
class PropertyTest {
    private Property property;
    private Game currentGame;
    private GameBoard mockedGameBoard;

    @Test
    void testBuyAndSellProperty() {
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

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(0, 100, 900, 1), // Initial houses, house cost, expected balance, expected house count
                Arguments.of(4, 500, 500, 5), // Initial houses, hotel cost, expected balance, expected house count
                Arguments.of(5, 0, 1000, 5)   // No further construction possible, balance unchanged
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testBuildHouse(int initialHouseCount, int cost, int expectedBalance, int expectedHouseCount) {
        Game currentGame = new Game();
        GameBoard mockedGameBoard = new GameBoard();
        Player owner = new Player(UUID.randomUUID(), "Owner", currentGame, 1000);
        Property property = Property.builder()
                .houseCost(100)   // Assuming the cost for houses is always 100
                .hotelCost(500)   // Assuming the cost for hotel is always 500
                .houseCount(initialHouseCount)
                .owner(owner)
                .build();
        Property.setGameBoard(mockedGameBoard);

        property.buildHouse();

        assertEquals(expectedHouseCount, property.getHouseCount());
        assertEquals(expectedBalance, owner.getBalance());
    }


}
