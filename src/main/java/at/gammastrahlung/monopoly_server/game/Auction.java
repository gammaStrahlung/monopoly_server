package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
import at.gammastrahlung.monopoly_server.network.websocket.MonopolyMessageHandler;
import lombok.Data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Auction {
    private static List<Bid> bids = new CopyOnWriteArrayList<>();

    private static Game game= MonopolyMessageHandler.currentGame;
    private GameBoard gameBoard;
    private int expectedBids;
    private static int currentFieldIndexforBuying;



    public Auction() {



        this.gameBoard = game.getGameBoard();
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
        if (highestBid != null) {


       Player currentPlayer = game.getPlayerById(highestBid.getPlayerId());
       List <Player> playerPropertyOwner = game.getPlayerListForId();
        Field currentField = gameBoard.getFieldByIndex(currentFieldIndexforBuying);
            if (currentField instanceof Property) {
                Property currentProperty = (Property) currentField;




        if(!playerPropertyOwner.contains(currentProperty.getOwner())) {
            currentProperty.setBidValue(highestBid.getAmount());
            currentProperty.setBidActivated(true);
            currentProperty.buyAndSellProperty(currentPlayer);
        }
        }
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
        List<Player> playerPropertyOwner = game.getPlayerListForId();

        if(!playerPropertyOwner.contains(currentProperty.getOwner())) {
            currentProperty.buyAndSellProperty(player);
        }

    }


}