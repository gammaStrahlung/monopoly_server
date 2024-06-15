package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Auction {
    private static List<Bid> bids = new CopyOnWriteArrayList<>();

    private Game game;
    private GameBoard gameBoard;
    private int expectedBids;
    private static int currentFieldIndexforBuying;



    public Auction() {

        this.game = new Game();
        this.gameBoard = new GameBoard();
        this.gameBoard.initializeGameBoard();
        this.expectedBids = Game.getPlayerListForId().size();


    }

    public static List<Bid> getBids() {
        return Auction.bids;
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
        // Empty the list of bids
        bids.clear();
       Player currentPlayer = game.getPlayerById(highestBid.getPlayerId());
        Field currentField = gameBoard.getFieldByIndex(currentFieldIndexforBuying);
        Property currentProperty = (Property) currentField;
        Player bank = gameBoard.getBank();
        if( currentProperty.getOwner().equals(bank)) {
            currentProperty.buyAndSellProperty(currentPlayer);
        }


        return highestBid;
    }

    /**
     * Check if the bank owns the current field
     *
     * @param currentFieldIndex the index of the current field
     */
    public boolean checkCurrentField(int currentFieldIndex) {
        currentFieldIndexforBuying = currentFieldIndex;
        Field currentField = gameBoard.getFieldByIndex(currentFieldIndex);
        Player bank = gameBoard.getBank();
        if (currentField instanceof Property currentProperty) {
            return currentProperty.getOwner().equals(bank);
        }

        return false;
    }

    public void buyCurrentField(Player player) {

        Field currentField = gameBoard.getFieldByIndex(currentFieldIndexforBuying);
        Property currentProperty = (Property) currentField;
        Player bank = gameBoard.getBank();
        if(currentProperty.getOwner().equals(bank)) {
            currentProperty.buyAndSellProperty(player);
        }
    }
}