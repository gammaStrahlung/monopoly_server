package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.PropertyColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MortgageManagerTest {
    private MortgageManager mortgageManager;
    private GameBoard gameBoard;
    private Player player1;
    private Player player2;

    @BeforeEach
    void setUp() {
        gameBoard = Mockito.spy(GameBoard.class);
        mortgageManager = new MortgageManager(gameBoard);
        player1 = new Player(UUID.randomUUID(), "Player 1", null, 1000);
        player2 = new Player(UUID.randomUUID(), "Player 2", null, 1000);
        mortgageManager = MortgageManager.builder()
                .gameBoard(gameBoard)
                .build();
    }

    @Test
    void constructorTest_gameBoardSet() {
        assertEquals(gameBoard, mortgageManager.getGameBoard(), "GameBoard should be set correctly by the constructor.");
    }

    @Test
    void mortgageProperty_successful() {
        Property property = createProperty(1, 100, 50, player1);
        when(gameBoard.getPropertyById(1)).thenReturn(property);

        assertTrue(mortgageManager.mortgageProperty(1, player1));
        assertTrue(property.isMortgaged());
        assertEquals(1050, player1.getBalance());
    }

    @Test
    void mortgageProperty_alreadyMortgaged() {
        Property property = createProperty(1, 100, 50, player1);
        property.setMortgaged(true);
        when(gameBoard.getPropertyById(1)).thenReturn(property);

        assertFalse(mortgageManager.mortgageProperty(1, player1));
    }

    @Test
    void mortgageProperty_notOwner() {
        Property property = createProperty(1, 100, 50, player2);
        when(gameBoard.getPropertyById(1)).thenReturn(property);

        assertFalse(mortgageManager.mortgageProperty(1, player1));
    }

    @Test
    void mortgageProperty_hasBuildings() {
        Property property = createProperty(1, 100, 50, player1);
        property.setHouseCount(1);
        when(gameBoard.getPropertyById(1)).thenReturn(property);

        assertFalse(mortgageManager.mortgageProperty(1, player1));
    }


    @Test
    void repayMortgage_notMortgaged() {
        Property property = createProperty(1, 100, 50, player1);
        when(gameBoard.getPropertyById(1)).thenReturn(property);

        assertFalse(mortgageManager.repayMortgage(1, player1));
    }

    @Test
    void repayMortgage_notOwner() {
        Property property = createProperty(1, 100, 50, player2);
        property.setMortgaged(true);
        when(gameBoard.getPropertyById(1)).thenReturn(property);

        assertFalse(mortgageManager.repayMortgage(1, player1));
    }

    @Test
    void repayMortgage_insufficientBalance() {
        Property property = createProperty(1, 100, 50, player1);
        property.setMortgaged(true);
        when(gameBoard.getPropertyById(1)).thenReturn(property);
        player1.subtractBalance(950);

        assertFalse(mortgageManager.repayMortgage(1, player1));
    }

    @Test
    void transferMortgagedProperty_notMortgaged() {
        Property property = createProperty(1, 100, 50, player1);
        when(gameBoard.getPropertyById(1)).thenReturn(property);

        assertFalse(mortgageManager.transferMortgagedProperty(1, player1, player2));
    }

    @Test
    void transferMortgagedProperty_notOwner() {
        Property property = createProperty(1, 100, 50, player2);
        property.setMortgaged(true);
        when(gameBoard.getPropertyById(1)).thenReturn(property);

        assertFalse(mortgageManager.transferMortgagedProperty(1, player1, player2));
    }
    @Test
    void canCollectRent_noMortgagedProperties() {
        when(gameBoard.anyMortgagedInGroup(PropertyColor.BROWN)).thenReturn(false);
        assertTrue(mortgageManager.canCollectRent(PropertyColor.BROWN));
    }

    @Test
    void canCollectRent_hasMortgagedProperties() {
        when(gameBoard.anyMortgagedInGroup(PropertyColor.BROWN)).thenReturn(true);
        assertFalse(mortgageManager.canCollectRent(PropertyColor.BROWN));
    }

    @Test
    void enableBuildingOnGroup_noMortgagedProperties() {
        when(gameBoard.anyMortgagedInGroup(PropertyColor.BROWN)).thenReturn(false);
        mortgageManager.enableBuildingOnGroup(PropertyColor.BROWN);
        verify(gameBoard).enableBuilding(PropertyColor.BROWN);
    }

    @Test
    void enableBuildingOnGroup_hasMortgagedProperties() {
        when(gameBoard.anyMortgagedInGroup(PropertyColor.BROWN)).thenReturn(true);
        mortgageManager.enableBuildingOnGroup(PropertyColor.BROWN);
        verify(gameBoard, never()).enableBuilding(PropertyColor.BROWN);
    }

    @Test
    void sellBuildingsBeforeMortgage_hasBuildings() {
        when(gameBoard.hasBuildings(PropertyColor.BROWN)).thenReturn(true);
        mortgageManager.sellBuildingsBeforeMortgage(PropertyColor.BROWN, player1);
        verify(gameBoard).sellBuildings(PropertyColor.BROWN, player1);
    }

    @Test
    void sellBuildingsBeforeMortgage_noBuildings() {
        when(gameBoard.hasBuildings(PropertyColor.BROWN)).thenReturn(false);
        mortgageManager.sellBuildingsBeforeMortgage(PropertyColor.BROWN, player1);
        verify(gameBoard, never()).sellBuildings(PropertyColor.BROWN, player1);
    }

    private Property createProperty(int fieldId, int price, int mortgageValue, Player owner) {
        Property property = Property.builder()
                .fieldId(fieldId)
                .price(price)
                .mortgageValue(mortgageValue)
                .owner(owner)
                .build();
        return property;
    }

   @Test
   void mortgageProperty_propertyNotFound() {
       when(gameBoard.getPropertyById(1)).thenReturn(null);
       assertFalse(mortgageManager.mortgageProperty(1, player1));
   }
    @Test
    void transferMortgagedProperty_successful() {
        Property property = createProperty(1, 100, 50, player1);
        property.setMortgaged(true);
        when(gameBoard.getPropertyById(1)).thenReturn(property);

        assertTrue(mortgageManager.transferMortgagedProperty(1, player1, player2));
        assertEquals(player2, property.getOwner());
    }

    @Test
    void canCollectRent_onlyMortgagedInOtherGroups() {
        when(gameBoard.anyMortgagedInGroup(PropertyColor.BROWN)).thenReturn(false);
        when(gameBoard.anyMortgagedInGroup(PropertyColor.LIGHT_BLUE)).thenReturn(true);
        assertTrue(mortgageManager.canCollectRent(PropertyColor.BROWN));
    }
}