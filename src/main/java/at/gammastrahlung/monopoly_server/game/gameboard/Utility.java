package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Player;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class Utility extends Field{
    @Expose
    private Player owner;
    @Expose
    private int toPay;
    @Expose
    private int price;
    @Expose
    private int mortgage;

    public void buyAndSellUtility(Player buyer){
        owner.addBalance(price);
        buyer.subtractBalance(price);
        this.owner = buyer;
    }
}
