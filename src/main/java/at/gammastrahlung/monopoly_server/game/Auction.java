 package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.Bid;
import at.gammastrahlung.monopoly_server.game.Game;
import at.gammastrahlung.monopoly_server.game.Player;
import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import lombok.Data;

import java.util.List;
import java.util.UUID;
 @Data
public class Auction {

    private List<Bid> bids;
    private Game game;
    private GameBoard gameBoard;

    public void evaluateBids(Bid newBid) {
        bids.add(newBid); // Add the new bid to the list of bids

        Bid highestBid = null;
        Player winningPlayer = null;

        for (Bid bid : bids) {
            if (highestBid == null || bid.getAmount() > highestBid.getAmount()) {
                highestBid = bid;
                winningPlayer = game.getPlayerById(bid.getPlayerId());
            }
        }

        if (highestBid != null) {
            Field field = game.getGameBoard().getFieldByIndex(highestBid.getFieldIndex());
            if (field instanceof Property) {
                Property property = (Property) field;
                property.setOwner(winningPlayer);
            }
        }
    }
}














}