package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

@Data
public class Auction {
   private int safeone = 0;

    private Game game ;
    private GameBoard gameBoard;
    private static List<Bid> bids = new CopyOnWriteArrayList<>();
    private int expectedBids;
    private CountDownLatch latch;
    public Auction() {

        this.game = new Game();
        this.gameBoard = new GameBoard();
        this.gameBoard.initializeGameBoard();
        this.expectedBids = Game.getPlayerListForId().size();


    }






    public void addBid(Bid bid) {
        bids.add(bid);

    }

 public Bid evaluateHighestBids() {
    Bid highestBid = null;

    for (Bid bid : bids) {
        if (highestBid == null || bid.getAmount() > highestBid.getAmount()) {
            highestBid = bid;
        }
    }

    return highestBid;
}






//    public Bid evaluateBids(Bid newBid) {
//        bids.add(newBid); // Add the new bid to the list of bids
//
//        Bid highestBid = null;
//        Player winningPlayer = null;
//
//        for (Bid bid : bids) {
//            if (highestBid == null || bid.getAmount() > highestBid.getAmount()) {
//                highestBid = bid;
//                highestBid.setAmount(4444);
//                winningPlayer = game.getPlayerById(bid.getPlayerId());
//            }
//        }
//
//        Field field = game.getGameBoard().getFieldByIndex(highestBid.getFieldIndex());
//        if (field instanceof Property property) {
//            property.setOwner(winningPlayer);
//        }
//        // Create a new bid with the highest amount and return it
//        Bid highestPlayerBid = new Bid(winningPlayer.getId(), highestBid.getAmount(), highestBid.getFieldIndex());
//        return highestPlayerBid;
//
//    }

    /**
     * Check if the current field is owned by the bank
     *
     * @param currentFieldIndex the index of the current field
     */
    public boolean checkCurrentField(int currentFieldIndex) {

//        if(safeone == 0) {
//            currentFieldIndex = currentFieldIndex -1;
//            safeone = 1;
//        }
        Field currentField = gameBoard.getFieldByIndex(currentFieldIndex);
        Player bank = gameBoard.getBank();
        if (currentField instanceof Property currentProperty) {
            return currentProperty.getOwner().equals(bank);
        }

        return false;
    }

    public static void setBids(List<Bid> bids) {
    Auction.bids = bids;
}

public static List<Bid> getBids() {
    return Auction.bids;
}
}