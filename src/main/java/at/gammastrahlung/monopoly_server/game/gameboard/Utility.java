package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Player;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


@Getter
@Setter
@SuperBuilder
public class Utility extends Field{
    private Player owner;
    private int toPay;
    private int price;
    private int mortgage;

    public void buyAndSellUtility(Player buyer){
        owner.addBalance(price);
        buyer.subtractBalance(price);
        this.owner = buyer;
    }
}
