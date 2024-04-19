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
}
