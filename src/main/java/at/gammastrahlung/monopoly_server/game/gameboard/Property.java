package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Player;
import com.google.gson.annotations.Expose;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.Map;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Property extends Field{
    @Expose
    private int price;
    @Expose
    private Player owner;
    @Expose
    private PropertyColor color;
    @Expose
    private Map<Object, Integer> rentPrices;
    @Expose
    private int mortgageValue;
    @Expose
    private int houseCost;
    @Expose
    private int hotelCost;
    @Expose
    private int houseCount = 0;
    private boolean hasHotel;
    private boolean mortgaged;

    /**
     * Method to buy or sell a property, changing the owner and updating balances.
     * @param buyer The player buying the property.
     */
    public void buyAndSellProperty(Player buyer){
        owner.addBalance(price);
        buyer.subtractBalance(price);
        this.owner = buyer;
    }
    private static GameBoard gameBoard;
    public static void setGameBoard(GameBoard gb) {
        Property.gameBoard = gb;
    }
    /**
     * Builds a house on this property if the property has less than five houses and it is buildable.
     * @return true if the house was built, false otherwise.
     */
    public boolean buildHouse() {
            if (houseCount < 5 && buildable()) {
                if (houseCount == 4 && buildable()){
                    this.owner.subtractBalance(hotelCost);
                } else this.owner.subtractBalance(houseCost);
                this.houseCount++;
                return true;
            } else return false;

    }
    /**
     * Checks if building on this property is allowable based on ownership within the same color group.
     * @return true if buildable, false otherwise.
     */

    public boolean buildable() {
        boolean buildable = true;
        for (Field field : gameBoard.getGameBoard()) {
            if (field instanceof Property property && property.getColor() == this.color && !property.getOwner().equals(this.owner)) {
                buildable = false;
                break;  // If a condition is met that makes it unbuildable, exit the loop early
            }
        }
        return buildable;
    }
<<<<<<< HEAD

    /**
     * Checks if the property has any buildings (houses or hotel).
     * @return true if there are buildings, false otherwise.
     */
    public boolean hasBuildings() {
        return houseCount > 0 || hasHotel;
    }

=======
>>>>>>> origin/main
}
