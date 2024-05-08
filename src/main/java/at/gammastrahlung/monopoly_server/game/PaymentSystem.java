package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;

public class PaymentSystem {
    private final GameBoard gameBoard;

    public PaymentSystem(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public boolean processRailroadPayment(Player payer, Railroad railroad) {
        if (railroad.getOwner() != null && !railroad.getOwner().equals(payer)) {
            int ownedRailroads = countOwnedRailroads(railroad.getOwner());
            String key = ownedRailroads + "RR";
            Integer rentAmount = railroad.getRentPrices().get(key);
            if (rentAmount != null) {
                return makePayment(payer, railroad.getOwner(), rentAmount);
            }
        }
        return false;
    }

    public boolean processPropertyPayment(Player payer, Property property) {
        if (property.getOwner() != null && !property.getOwner().equals(payer)) {
            int rentAmount = property.getRentPrices().getOrDefault("0", 0); // Default rent if no houses
            return makePayment(payer, property.getOwner(), rentAmount);
        }
        return false;
    }

    public boolean processUtilityPayment(Player payer, Utility utility) {
        if (utility.getOwner() != null && !utility.getOwner().equals(payer)) {
            int rentAmount = utility.getToPay(); // Assumes getToPay() gives the correct amount due
            return makePayment(payer, utility.getOwner(), rentAmount);
        }
        return false;
    }

    private int countOwnedRailroads(Player owner) {
        return (int) java.util.Arrays.stream(gameBoard.getGameBoard())
                .filter(field -> field instanceof Railroad && ((Railroad) field).getOwner().equals(owner))
                .count();
    }

    public boolean makePayment(Player from, Player to, int amount) {
        if (from.getBalance() >= amount) {
            from.subtractBalance(amount);
            to.addBalance(amount);
            return true;
        } else {
            return handleInsufficientFunds(from);
        }
    }

    private boolean handleInsufficientFunds(Player player) {
        // Implement logic for insufficient funds here
        // For example: Sell houses, take out mortgage, or declare bankruptcy
        return false;
    }
}