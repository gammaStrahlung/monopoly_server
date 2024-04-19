package at.gammastrahlung.monopoly_server.game;

import java.awt.event.FocusEvent;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Property extends Field{
    private Player owner;
    private int price;
    private int baseRent;
    private int oneHouseRent;
    private int twoHousesRent;
    private int threeHousesRent;
    private int hotelRent;

    public Property(int fieldID, String name, Player owner, int price, int baseRent, int oneHouseRent, int twoHousesRent, int threeHousesRent, int hotelRent) {
        super(fieldID, name);
        this.owner = owner;
        this.price = price;
        this.baseRent = baseRent;
        this.oneHouseRent = oneHouseRent;
        this.twoHousesRent = twoHousesRent;
        this.threeHousesRent = threeHousesRent;
        this.hotelRent = hotelRent;
    }

    private void buyAndSellProperty(Player buyer){
        owner.addBalance(price);
        buyer.subtractBalance(price);
        this.owner = buyer;
    }




}
