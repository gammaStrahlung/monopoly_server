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
public class Property extends OwnableField {
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
    private int houseCount;

    public void buyAndSellProperty(Player buyer){
        owner.addBalance(price);
        buyer.subtractBalance(price);
        this.owner = buyer;
    }
    private static GameBoard gameBoard;
    public static void setGameBoard(GameBoard gb) {
        Property.gameBoard = gb;
    }

    public boolean buildHouse() {
            if (houseCount < 5 && buildable()) {
                if (houseCount == 4 && buildable()){
                    this.owner.subtractBalance(hotelCost);
                } else this.owner.subtractBalance(houseCost);
                this.houseCount++;
                return true;
            } else return false;

    }

    public boolean buildable() {
        boolean buildable = true;
        for (Field field : gameBoard.getFields()) {
            if (field instanceof Property property && property.getColor() == this.color && !property.getOwner().equals(this.owner)) {
                buildable = false;
                break;  // If a condition is met that makes it unbuildable, exit the loop early
            }
        }
        return buildable;
    }

    /**
     * Calculates the property value (includes price, amount of houses, if hotel was build)
     * @return The property value
     */
    public int getPropertyValue() {
        if (houseCount < 4) {
            return price + houseCount * houseCost;
        } else if (houseCount == 5) {
            return price + houseCount * 4 + hotelCost;
        } else {
            return price;
        }
    }
}
