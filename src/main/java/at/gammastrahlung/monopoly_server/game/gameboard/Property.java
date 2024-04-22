package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Player;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;


@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Property extends Field{
    private int price;
    private Player owner;
    private PropertyColor color;
    private Map<Object, Integer> rentPrices;
    private int mortgageValue;
    private int houseCost;
    private int hotelCost;
    private int houseCount = 0;

    public void buyAndSellProperty(Player buyer){
        owner.addBalance(price);
        buyer.subtractBalance(price);
        this.owner = buyer;
    }
    private static GameBoard gameBoard;
    public static void setGameBoard(GameBoard gb) {
        Property.gameBoard = gb;
    }

    public void buildHouse() {

            if (houseCount < 4 && buildable()) {
                this.houseCount++;
                this.owner.subtractBalance(houseCost);
            } else if (houseCount == 4 && buildable()) {
                this.houseCount++;
                this.owner.subtractBalance(hotelCost);
            } else {
                System.out.print("Building on this property isn't possible!");
            }
    }


    private boolean buildable(){
        boolean buildable = true;
        for (Field field : gameBoard.getGameBoard()) {
            if (field instanceof Property && ((Property) field).getColor() == this.color) {
                if (!((Property) field).getOwner().equals(this.owner)) {
                    buildable = false;
                }
            }
        }
        return buildable;
    }

   /* public void getRent(Player renter){
        if (houseCount == 0) {
            renter.subtractBalance(baseRent);
            this.owner.addBalance(baseRent);
        } else if (houseCount == 1) {
            renter.subtractBalance(oneHouseRent);
            this.owner.addBalance(oneHouseRent);
        } else if (houseCount == 2) {
            renter.subtractBalance(twoHousesRent);
            this.owner.addBalance(twoHousesRent);
        } else if (houseCount == 3) {
            renter.subtractBalance(threeHousesRent);
            this.owner.addBalance(threeHousesRent);
        }else {
            renter.subtractBalance(hotelRent);
            this.owner.addBalance(hotelRent);
        }
    }*/


}
