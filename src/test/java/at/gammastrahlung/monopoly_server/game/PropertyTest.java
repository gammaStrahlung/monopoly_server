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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class PropertyTest {
    private Property property;
    private Player player;
    private GameBoard gameBoard;

    @BeforeEach
    public void setUp() {
        player = new Player(UUID.randomUUID(),"Player", new Game(), 1000);
        property = Property.builder()
                .price(200)
                .owner(player)
                .build();
        gameBoard = new GameBoard();
        gameBoard.initializeGameBoard();
        Property.setGameBoard(gameBoard);
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

    @Test
    void testPropertyNotBuildableIfNotFullSetOwned() {
        GameBoard mockGameBoard = mock(GameBoard.class);
        Property.setGameBoard(mockGameBoard);
        Player owner = new Player(UUID.randomUUID(), "Owner", null, 1500);
        Property property = new Property();
        property.setOwner(owner);
        property.setColor(PropertyColor.RED);

        Property otherProperty = new Property();
        otherProperty.setColor(PropertyColor.RED);
        otherProperty.setOwner(new Player(UUID.randomUUID(), "OtherOwner", null, 1500));

        when(mockGameBoard.getFields()).thenReturn(new Field[] {property, otherProperty});

        assertFalse(property.buildable(), "Property should not be buildable if not all properties of the same color are owned.");
    }

    ///-----------------AuctionTest-----------------///
    @Test
    void testDefaultConstructor() {
        Property property = new Property();
        assertNull(property.getOwner());
        assertEquals(0, property.getPrice());
    }

    @Test
    void testSuperBuilderInitialization() {
        assertEquals(player, property.getOwner());
        assertEquals(200, property.getPrice());
    }

    @Test
    void testGettersAndSetters() {
        property.setPrice(300);
        assertEquals(300, property.getPrice());
    }

    @Test
    void testBuildableTrue() {
        Property otherProperty = Property.builder()
                .color(PropertyColor.RED)
                .owner(player)
                .build();
        gameBoard.setFields(new Field[] {property, otherProperty});
        assertTrue(property.buildable());
    }

    @Test
    void testBuyAndSellPropertyWithBidActivated() {
        Player newOwner = new Player(UUID.randomUUID(), "NewOwner", new Game(), 1000);
        property.setBidActivated(true);
        property.setBidValue(250);
        property.buyAndSellProperty(newOwner);
        assertEquals(newOwner, property.getOwner());
        assertEquals(750, newOwner.getBalance());
        assertEquals(1250, player.getBalance());
    }

    @Test
    void testBuyAndSellPropertyWithoutBidActivated() {
        Player newOwner = new Player(UUID.randomUUID(), "NewOwner", new Game(), 1000);
        property.setBidActivated(false);
        property.buyAndSellProperty(newOwner);
        assertEquals(newOwner, property.getOwner());
        assertEquals(800, newOwner.getBalance());
        assertEquals(1200, player.getBalance());
    }




}

