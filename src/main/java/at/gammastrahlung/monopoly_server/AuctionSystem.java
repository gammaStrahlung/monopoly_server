package at.gammastrahlung.monopoly_server;

import java.util.HashMap;
import java.util.Map;

import at.gammastrahlung.monopoly_server.game.Player;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;

/**
 * This class manages the auction system in a Monopoly game for Android.
 * It handles bids, tracks the highest bidder, finalizes the auction process, and notifies players about auction updates.
 */
public class AuctionSystem {

    private final Map<Player, Integer> bids; // Stores the current highest bid for each player
    private final Property auctionProperty;  // The auctioned property
    private Player highestBidder;            // Current highest bidder
    private int highestBid;                  // Current highest bid

    /**
     * Interface for handling auction events.
     */
    public interface AuctionEventListener {
        void onBidUpdated(Player player, int newBid);
        void onAuctionFinalized(Player winner, int winningBid);
    }

    private final AuctionEventListener eventListener;

    /**
     * Constructor to initialize the auction system with a specific property and event listener.
     * @param auctionProperty The property being auctioned.
     * @param eventListener Listener to handle auction events.
     */
    public AuctionSystem(Property auctionProperty, AuctionEventListener eventListener) {
        this.auctionProperty = auctionProperty;
        this.eventListener = eventListener;
        this.bids = new HashMap<>();
        this.highestBid = 0;
        this.highestBidder = null;
    }

    /**
     * Allows a player to place a bid on the property.
     * @param player The player making the bid.
     * @param bid The amount of the bid.
     * @return true if the bid is higher than the current highest bid, false otherwise.
     */
    public boolean placeBid(Player player, int bid) {
        if (bid <= highestBid) {
            return false;
        }
        if (bid <= 0) {
            throw new IllegalArgumentException("Bid must be greater than zero.");
        }
        if (player.getBalance() < bid) {
            throw new IllegalArgumentException("Player does not have enough balance to place the bid.");
        }
        bids.put(player, bid);
        highestBid = bid;
        highestBidder = player;
        eventListener.onBidUpdated(player, bid);
        return true;
    }


}
