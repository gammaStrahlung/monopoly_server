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


    public boolean repayMortgage(int propertyId, Player player) {
        Property property = gameBoard.getPropertyById(propertyId);
        int repaymentAmount = (int) (property.getMortgageValue() * 1.1); // Including 10% interest
        if (property == null || !property.getOwner().equals(player) || !property.isMortgaged() || player.getBalance() < repaymentAmount) {
            return false;
        }
        player.subtractBalance(repaymentAmount);
        property.setMortgaged(false);
        gameBoard.enableBuilding(property.getColor()); // Enable building ability on property group
        return true;
    }





    public boolean transferMortgagedProperty(int propertyId, Player fromPlayer, Player toPlayer) {
        Property property = gameBoard.getPropertyById(propertyId);
        if (property == null || !property.getOwner().equals(fromPlayer) || !property.isMortgaged()) {
            return false;
        }
        int interestPayment = (int) (property.getMortgageValue() * 0.1);
        if (toPlayer.getBalance() < interestPayment) {
            return false;
        }
        toPlayer.subtractBalance(interestPayment);
        property.setOwner(toPlayer);
        return true;
    }


    public boolean canCollectRent(PropertyColor color) {
        return !gameBoard.anyMortgagedInGroup(color);
    }

    public void enableBuildingOnGroup(PropertyColor color) {
        if (!gameBoard.anyMortgagedInGroup(color)) {
            gameBoard.enableBuilding(color);
        }
    }


}