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


}
