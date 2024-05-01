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
















}