package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.game.gameboard.PropertyColor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * The MortgageManager class handles mortgage-related operations for properties in the game.
 */
@Getter
@Setter
@SuperBuilder
public class MortgageManager {

    private GameBoard gameBoard;

    /**
     * Constructs a MortgageManager with the specified game board.
     *
     * @param gameBoard The game board to associate with the MortgageManager.
     */
    public MortgageManager(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Mortgages a property owned by the player.
     *
     * @param propertyId The ID of the property to mortgage.
     * @param player     The player attempting to mortgage the property.
     * @return True if the property was successfully mortgaged; false otherwise.
     */
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

    /**
     * Repays the mortgage on a property.
     *
     * @param propertyId The ID of the property to repay the mortgage on.
     * @param player     The player attempting to repay the mortgage.
     * @return True if the mortgage was successfully repaid; false otherwise.
     */
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

    /**
     * Transfers ownership of a mortgaged property to another player.
     *
     * @param propertyId The ID of the mortgaged property to transfer.
     * @param fromPlayer The current owner of the property.
     * @param toPlayer   The player to transfer ownership to.
     * @return True if the transfer was successful; false otherwise.
     */
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

    /**
     * Checks if rent can be collected on properties of the specified color group.
     *
     * @param color The color group to check for mortgage status.
     * @return True if rent can be collected on all properties in the group; false if any are mortgaged.
     */
    public boolean canCollectRent(PropertyColor color) {
        return !gameBoard.anyMortgagedInGroup(color);
    }

    /**
     * Enables building on properties of the specified color group if none are mortgaged.
     *
     * @param color The color group to enable building on.
     */
    public void enableBuildingOnGroup(PropertyColor color) {
        if (!gameBoard.anyMortgagedInGroup(color)) {
            gameBoard.enableBuilding(color);
        }
    }

    /**
     * Sells buildings on properties of the specified color group before mortgaging.
     *
     * @param color  The color group to sell buildings from.
     * @param player The player selling the buildings.
     */
    public void sellBuildingsBeforeMortgage(PropertyColor color, Player player) {
        if (gameBoard.hasBuildings(color)) {
            gameBoard.sellBuildings(color, player);
        }
    }

}