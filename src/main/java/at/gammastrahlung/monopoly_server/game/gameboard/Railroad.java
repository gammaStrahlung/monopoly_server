package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Player;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Railroad extends Field {
    @Expose
    private Player owner;
    @Expose
    private int price;
    @Expose
    private static final Map<String, Integer> rentPrices = initRentPrices();

    private static Map<String, Integer> initRentPrices() {
        Map<String, Integer> prices = new HashMap<>();
        prices.put("1RR", 25);  // Rent with 1 railroad
        prices.put("2RR", 50);  // Rent with 2 railroads
        prices.put("3RR", 100); // Rent with 3 railroads
        prices.put("4RR", 200); // Rent with 4 railroads
        return prices;
    }

    public void buyAndSellRailroad(Player buyer){
        owner.addBalance(price);
        buyer.subtractBalance(price);
        this.owner = buyer;
    }

}
