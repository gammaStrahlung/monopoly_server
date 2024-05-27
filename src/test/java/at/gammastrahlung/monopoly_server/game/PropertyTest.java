package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.PropertyColor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;
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

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(0, 100, 900, 1, true), // Initial houses, house cost, expected balance, expected house count
                Arguments.of(4, 500, 500, 5, true), // Initial houses, hotel cost, expected balance, expected house count
                Arguments.of(5, 0, 1000, 5, false)   // No further construction possible, balance unchanged
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void testBuildHouse(int initialHouseCount, int cost, int expectedBalance, int expectedHouseCount) {
        Game currentGame = new Game();
        GameBoard gameBoard = new GameBoard();
        gameBoard.initializeGameBoard();
        Player owner = new Player(UUID.randomUUID(), "Owner", currentGame, 1000);
        Property property = Property.builder()
                .houseCost(100)   // Assuming the cost for houses is always 100
                .hotelCost(500)   // Assuming the cost for hotel is always 500
                .houseCount(initialHouseCount)
                .owner(owner)
                .build();
        Property.setGameBoard(gameBoard);

        property.buildHouse();

        assertEquals(expectedHouseCount, property.getHouseCount());
        assertEquals(expectedBalance, owner.getBalance());
    }
    private static Stream<Arguments> provideBuildableCases() {
        Player owner1 = new Player(UUID.randomUUID(), "Owner1", null, 1000);
        Player owner2 = new Player(UUID.randomUUID(), "Owner2", null, 1000);

        Property property1 = Property.builder().color(PropertyColor.RED).owner(owner1).build();
        Property property2 = Property.builder().color(PropertyColor.RED).owner(owner1).build();
        Property property3 = Property.builder().color(PropertyColor.RED).owner(owner2).build();

        List<Field> sameOwner = new ArrayList<>();
        sameOwner.add(property1);
        sameOwner.add(property2);

        List<Field> differentOwners = new ArrayList<>();
        differentOwners.add(property1);
        differentOwners.add(property3);

        return Stream.of(
                Arguments.of(sameOwner, owner1, true),
                Arguments.of(differentOwners, owner1, false)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBuildableCases")
    void testBuildable(List<Field> gameBoardFields, Player propertyOwner, boolean expectedOutcome) {
        GameBoard gameBoard = new GameBoard();
        gameBoard.initializeGameBoard();
        Field[] fieldsArray = gameBoardFields.toArray(new Field[0]); // Convert List<Field> to Field[]
        gameBoard.setFields(fieldsArray); // Assuming this method sets the list of fields in the game board

        Property propertyUnderTest = Property.builder()
                .color(PropertyColor.RED)
                .owner(propertyOwner)
                .build();

        Property.setGameBoard(gameBoard);

        assertEquals(expectedOutcome, propertyUnderTest.buildable(),
                "Expected buildable() to return " + expectedOutcome);
    }

    private static Stream<Arguments> provideRentCases() {
        Map<Object, Integer> rentPrices = new HashMap<>();
        rentPrices.put(0, 6);
        rentPrices.put("fullSet", 12);
        rentPrices.put(1, 30);
        rentPrices.put(2, 90);
        rentPrices.put(3, 270);
        rentPrices.put(4, 400);
        rentPrices.put("hotel", 550);

        return Stream.of(
                Arguments.of(rentPrices, 0, true, 12), // No houses, buildable, full set rent
                Arguments.of(rentPrices, 0, false, 6), // No houses, not buildable, base rent
                Arguments.of(rentPrices, 1, false, 30), // 1 house
                Arguments.of(rentPrices, 2, false, 90), // 2 houses
                Arguments.of(rentPrices, 3, false, 270), // 3 houses
                Arguments.of(rentPrices, 4, false, 400), // 4 houses
                Arguments.of(rentPrices, 5, false, 550)  // Hotel
        );
    }

    @ParameterizedTest
    @MethodSource("provideRentCases")
    void testDefineCurrentRent(Map<Object, Integer> rentPrices, int houseCount, int expectedRent) {
        Property property;
        Player player = new Player(UUID.randomUUID(), "player", null, 1000);


        property = Property.builder()
                .rentPrices(rentPrices)
                .houseCount(houseCount)
                .color(PropertyColor.RED)
                .owner(player)
                .rentPrices(rentPrices)
                .build();

        Property property2 = Property.builder().color(PropertyColor.RED).owner(player).build();
        Property property3 = Property.builder().color(PropertyColor.RED).owner(player).build();

        assertEquals(expectedRent, property.defineCurrentRent());
    }
}
