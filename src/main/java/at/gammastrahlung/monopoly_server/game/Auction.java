
package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;
import at.gammastrahlung.monopoly_server.network.websocket.MonopolyMessageHandler;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class Auction {

    private static List<Bid> bids = new ArrayList<>();
    @Setter
    private static Game game= MonopolyMessageHandler.currentGame;

    private GameBoard gameBoard;
    private int expectedBids;
    @Getter
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
          UUID currentPlayerId = highestBid.getPlayerId();
            Player currentPlayer = game.getPlayerById(currentPlayerId);
            List<Player> playerPropertyOwner = game.getPlayerListForId();
            Field currentField = gameBoard.getFieldByIndex(currentFieldIndexforBuying);

            if (currentField instanceof Property currentProperty) {
                if(!playerPropertyOwner.contains(currentProperty.getOwner())) {
                    currentProperty.setBidValue(highestBid.getAmount());
                    currentProperty.setBidActivated(true);
                    currentProperty.buyAndSellProperty(currentPlayer);
                }
            } else if (currentField instanceof Railroad currentRailroad) {
                if(!playerPropertyOwner.contains(currentRailroad.getOwner())) {
                    currentRailroad.setBidValue(highestBid.getAmount());
                    currentRailroad.setBidActivated(true);
                    currentRailroad.buyAndSellRailroad(currentPlayer);
                }
            } else if (currentField instanceof Utility currentUtility) {
                if(!playerPropertyOwner.contains(currentUtility.getOwner())) {
                    currentUtility.setBidValue(highestBid.getAmount());
                    currentUtility.setBidActivated(true);
                    currentUtility.buyAndSellUtility(currentPlayer);
                }
            }
        }
        return highestBid;
    }

    /**
     * Check if the bank owns the current field     *     * @param currentFieldIndex the index of the current field
     */    public boolean checkCurrentField(int currentFieldIndex) {
        currentFieldIndexforBuying = currentFieldIndex;
        Field currentField = gameBoard.getFieldByIndex(currentFieldIndex);
        Player bank = gameBoard.getBank();

        if (currentField instanceof Property currentProperty) {
            return currentProperty.getOwner().equals(bank);
        } else if (currentField instanceof Railroad currentRailroad) {
            return currentRailroad.getOwner().equals(bank);
        } else if (currentField instanceof Utility currentUtility) {
            return currentUtility.getOwner().equals(bank);
        }

        return false;
    }

    public void buyCurrentField(Player player) {
        int currentFieldId = currentFieldIndexforBuying;
        Field currentField = gameBoard.getFieldByIndex(currentFieldId);
        List<Player> playerPropertyOwner = game.getPlayerListForId();

        if (currentField instanceof Property currentProperty) {
            if(!playerPropertyOwner.contains(currentProperty.getOwner())) {
                currentProperty.buyAndSellProperty(player);
            }
        } else if (currentField instanceof Railroad currentRailroad) {
            if(!playerPropertyOwner.contains(currentRailroad.getOwner())) {
                currentRailroad.buyAndSellRailroad(player);
            }
        } else if (currentField instanceof Utility currentUtility) {
            if(!playerPropertyOwner.contains(currentUtility.getOwner())) {
                currentUtility.buyAndSellUtility(player);
            }
        }
    }


}