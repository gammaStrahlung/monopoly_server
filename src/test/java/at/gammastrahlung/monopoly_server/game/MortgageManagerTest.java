package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;

class MortgageManagerTests {

    private MortgageManager mortgageManager;
    private GameBoard gameBoard;
    private Player player;
    private Property property;

    @BeforeEach
    void setUp() {
        gameBoard = mock(GameBoard.class);
        player = mock(Player.class);
        property = mock(Property.class);
        mortgageManager = new MortgageManager(gameBoard);

        when(gameBoard.getPropertyById(anyInt())).thenReturn(property);
        when(property.getOwner()).thenReturn(player);

    }
    @Test
    void mortgagePropertyShouldSucceed() {
        when(property.isMortgaged()).thenReturn(false);
        when(property.hasBuildings()).thenReturn(false);
        when(property.getOwner()).thenReturn(player);
        when(property.getMortgageValue()).thenReturn(100);

        boolean result = mortgageManager.mortgageProperty(1, player);

        assertTrue(result);
        verify(player,
                times(1)).addBalance(anyInt());
        verify(property, times(1)).setMortgaged(true);
    }

    @Test
    void mortgagePropertyShouldFailIfAlreadyMortgaged() {
        when(property.isMortgaged()).thenReturn(true);

        boolean result = mortgageManager.mortgageProperty(1, player);

        assertFalse(result);
        verify(player, never()).addBalance(anyInt());
    }
    @Test
    void repayMortgageShouldSucceed() {
        when(property.isMortgaged()).thenReturn(true);
        when(property.getMortgageValue()).thenReturn(100);
        when(player.getBalance()).thenReturn(200);

        boolean result = mortgageManager.repayMortgage(1, player);

        assertTrue(result);
        verify(player, times(1)).subtractBalance(110); // 100 + 10% interest
        verify(property, times(1)).setMortgaged(false);
    }

    @Test
    void repayMortgageShouldFailIfNotEnoughBalance() {
        when(property.isMortgaged()).thenReturn(true);
        when(property.getMortgageValue()).thenReturn(1000);
        when(player.getBalance()).thenReturn(500);

        boolean result = mortgageManager.repayMortgage(1, player);

        assertFalse(result);
        verify(player, never()).subtractBalance(anyInt());
    }


}












}