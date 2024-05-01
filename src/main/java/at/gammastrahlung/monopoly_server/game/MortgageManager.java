package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.PropertyColor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;



@Getter
@Setter
@SuperBuilder
public class MortgageManager {

    private GameBoard gameBoard;


    public MortgageManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public boolean mortgageProperty(int propertyId, Player player) {
        Property property = gameBoard.getPropertyById(propertyId);
        if (property == null || property.isMortgaged() || !property.getOwner().equals(player) || property.hasBuildings()) {
            return false;
        }
        gameBoard.sellBuildings(property.getColor(), player); // Sell buildings at half price
        property.setMortgaged(true);
        player.addBalance(property.getMortgageValue());
        return true;
    }













}