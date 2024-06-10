 package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import lombok.Data;

import java.util.List;

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
            if (field instanceof Property property) {
                property.setOwner(winningPlayer);
            }
        }
    }


    public boolean checkCurrentField(int currentFieldIndex, Player bank) {
        Field currentField = gameBoard.getFieldByIndex(currentFieldIndex);

        if (currentField instanceof Property currentProperty) {
            return currentProperty.getOwner().equals(bank);
        }

        return false;
    }
}