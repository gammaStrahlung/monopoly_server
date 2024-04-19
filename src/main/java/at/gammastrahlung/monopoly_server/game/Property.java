package at.gammastrahlung.monopoly_server.game;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@SuperBuilder

public class Property extends Field{
    private Player owner;
    private int price;
    private int baseRent;
    private int oneHouseRent;
    private int twoHousesRent;
    private int threeHousesRent;
    private int hotelRent;
    private boolean buildable;
    private int housePrice = 20;
    private int houses = 0;


   /* public Property(int fieldID, String name, Player owner, int price, int baseRent, int oneHouseRent, int twoHousesRent, int threeHousesRent, int hotelRent) {
        super(fieldID, name);
        this.owner = owner;
        this.price = price;
        this.baseRent = baseRent;
        this.oneHouseRent = oneHouseRent;
        this.twoHousesRent = twoHousesRent;
        this.threeHousesRent = threeHousesRent;
        this.hotelRent = hotelRent;
    }*/

    public void buyAndSellProperty(Player buyer){
        owner.addBalance(price);
        buyer.subtractBalance(price);
        this.owner = buyer;
    }

    public void buildHouse(){
        if(houses < 4 && buildable){
            this.houses ++;
            this.owner.subtractBalance(housePrice);
        }
    }

    public void getRent(Player renter){
        if (houses == 0) {
            renter.subtractBalance(baseRent);
            this.owner.addBalance(baseRent);
        } else if (houses == 1) {
            renter.subtractBalance(oneHouseRent);
            this.owner.addBalance(oneHouseRent);
        } else if (houses == 2) {
            renter.subtractBalance(twoHousesRent);
            this.owner.addBalance(twoHousesRent);
        } else if (houses == 3) {
            renter.subtractBalance(threeHousesRent);
            this.owner.addBalance(threeHousesRent);
        }else {
            renter.subtractBalance(hotelRent);
            this.owner.addBalance(hotelRent);
        }
    }





}
