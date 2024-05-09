package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.PropertyColor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;

class PropertyTest {
    private GameBoard gameBoard;
    private Player owner;
    private Property property;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard();
        owner = new Player(UUID.randomUUID(), "Owner", null, 1000);
        property = new Property();
        property.setOwner(owner);
        property.setHouseCost(100);
        property.setHotelCost(500);
        property.setColor(PropertyColor.RED);
        Property.setGameBoard(gameBoard);
        gameBoard.setGameBoard(new Field[]{property});
        property = Property.builder()
                .houseCost(100)
                .hotelCost(500)
                .price(200)
                .owner(owner)
                .color(PropertyColor.RED)
                .build();
        Property.setGameBoard(gameBoard);
        gameBoard.setGameBoard(new Field[]{property});
    }
    @Test
    void testBuyAndSellProperties() {
        Player buyer = new Player(UUID.randomUUID(), "Buyer", null, 1000);
        property.buyAndSellProperty(buyer);

        assertEquals(buyer, property.getOwner(), "Owner should change to buyer.");
        assertEquals(800, buyer.getBalance(), "Buyer's balance should decrease by the price of the property.");
        assertEquals(1200, owner.getBalance(), "Owner's balance should increase by the price of the property.");
    }

    @Test
    void testBuildHouse() {
        assertTrue(property.buildHouse(), "Should return true when house is built.");
        assertEquals(1, property.getHouseCount(), "House count should be incremented.");

        property.setHouseCount(4); // Set to just before hotel
        assertTrue(property.buildHouse(), "Should return true when hotel is built.");
        assertEquals(5, property.getHouseCount(), "House count should increment to 5 for hotel.");

        assertFalse(property.buildHouse(), "Should return false when no more houses can be built.");
    }

    @Test
    void testBuildable() {
        Property anotherProperty = Property.builder()
                .color(PropertyColor.RED)
                .owner(new Player(UUID.randomUUID(), "Different Owner", null, 1000))
                .build();
        gameBoard.setGameBoard(new Field[]{property, anotherProperty});

        assertFalse(property.buildable(), "Should return false, another RED property is owned by a different owner.");
    }

    @Test
    void testHasBuildings() {
        assertFalse(property.hasBuildings(), "Should return false when there are no buildings.");
        property.setHouseCount(1);
        assertTrue(property.hasBuildings(), "Should return true when there is at least one building.");
    }

    @Test
    void testPriceSetAndGet() {
        property.setPrice(300);
        assertEquals(300, property.getPrice(), "Price should be retrievable and match the set value.");
    }

    @Test
    void testHouseAndHotelCostSetAndGet() {
        property.setHouseCost(150);
        assertEquals(150, property.getHouseCost(), "House cost should be set and get correctly.");
        property.setHotelCost(550);
        assertEquals(550, property.getHotelCost(), "Hotel cost should be set and get correctly.");
    }

    @Test
    void testHasHotel() {
        property.setHasHotel(true);
        assertTrue(property.hasBuildings(), "Should return true when hotel is set.");
    }
    @Test
    void testSellProperty() {
        Player buyer = new Player(UUID.randomUUID(), "Buyer", null, 1000);
        property.setPrice(300);
        property.buyAndSellProperty(buyer);

        assertEquals(buyer, property.getOwner(), "Property owner should change to buyer after transaction.");
        assertEquals(700, buyer.getBalance(), "Buyer's balance should decrease by the property price.");
        assertEquals(1300, owner.getBalance(), "Seller's balance should increase by the property price.");
    }

    @Test
    void testSetRentPrices() {
        Map<Object, Integer> rentPrices = new HashMap<>();
        rentPrices.put(1, 50); // Rent with 1 house
        property.setRentPrices(rentPrices);

        assertEquals(rentPrices, property.getRentPrices(), "Rent prices should be set correctly in the property.");
    }
    @Test
    void testBuildHouseWhenNotBuildable() {
        // Setup another property of the same color but different owner to simulate non-buildable scenario
        Property otherProperty = new Property();
        otherProperty.setColor(PropertyColor.RED);
        otherProperty.setOwner(new Player(UUID.randomUUID(), "Different Owner", null, 1000));
        gameBoard.setGameBoard(new Field[]{property, otherProperty});

        assertFalse(property.buildHouse(), "Property should not be buildable due to different owner in color group");
    }

    @Test
    void testBuildHouseMaxHouses() {
        property.setHouseCount(4); // Set to max houses before a hotel
        assertTrue(property.buildHouse(), "Should be able to build hotel");
        assertEquals(5, property.getHouseCount(), "Should have 5 houses now (including hotel)");
        assertEquals(500, owner.getBalance(), "Balance should decrease by hotel cost");
    }

    @Test
    void testBuildHouseAlreadyHasHotel() {
        property.setHouseCount(5); // Hotel already built
        assertFalse(property.buildHouse(), "Should not be able to build more than a hotel");
        assertEquals(5, property.getHouseCount(), "House count should remain the same");
        assertEquals(1000, owner.getBalance(), "Balance should not change");
    }

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
        Field[] fieldsArray = gameBoardFields.toArray(new Field[0]); // Convert List<Field> to Field[]
        gameBoard.setGameBoard(fieldsArray); // Assuming this method sets the list of fields in the game board

        Property propertyUnderTest = Property.builder()
                .color(PropertyColor.RED)
                .owner(propertyOwner)
                .build();

        Property.setGameBoard(gameBoard);

        assertEquals(expectedOutcome, propertyUnderTest.buildable(),
                "Expected buildable() to return " + expectedOutcome);
    }

    @Test
    void propertyHasBuildings() {
        Property property = new Property();
        property.setHouseCount(1);
        assertTrue(property.hasBuildings());
    }

    @Test
    void propertyHasNoBuildings() {
        Property property = new Property();
        property.setHouseCount(0);
        assertFalse(property.hasBuildings());
    }


}
