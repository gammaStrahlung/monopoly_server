package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
