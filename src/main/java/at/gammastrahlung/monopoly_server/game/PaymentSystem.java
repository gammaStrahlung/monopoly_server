package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSystem {

    private final GameBoard gameBoard;

    public PaymentSystem(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    private boolean processRailroadPayment(Player payer, Railroad railroad) {
        if (!railroad.getOwner().equals(payer)) {
            int ownedRailroads = countOwnedRailroads(railroad.getOwner());
            // Zugriff auf die RentPrices über den von Lombok generierten Getter
            int rentAmount = railroad.getRentPrices().get(ownedRailroads + "RR");
            return makePayment(payer, railroad.getOwner(), rentAmount);
        }
        return false;
    }


    private boolean processPropertyPayment(Player payer, Property property) {
        if (!property.getOwner().equals(payer)) {
            int rentAmount = property.getRentPrices().getOrDefault(0, 0); // Default rent if no houses
            return makePayment(payer, property.getOwner(), rentAmount);
        }
        return false;
    }



    private boolean processUtilityPayment(Player payer, Utility utility) {
        if (!utility.getOwner().equals(payer)) {
            int rentAmount = utility.getToPay(); // Oder eine dynamische Berechnung
            return makePayment(payer, utility.getOwner(), rentAmount);
        }
        return false;
    }

    private int countOwnedRailroads(Player owner) {
        int count = 0;
        for (Field field : gameBoard.getGameBoard()) {
            if (field instanceof Railroad && ((Railroad) field).getOwner().equals(owner)) {
                count++;
            }
        }
        return count;
    }

    private boolean makePayment(Player from, Player to, int amount) {
        if (from.getBalance() >= amount) {
            from.subtractBalance(amount);
            to.addBalance(amount);
            return true;
        } else {
            // Implementiere eine Logik für Insolvenz oder ähnliches
            return handleInsufficientFunds(from);
        }
    }

    private boolean handleInsufficientFunds(Player player) {
        // Implementiere hier die Logik für unzureichende Mittel
        // Zum Beispiel: Verkaufe Häuser, nimm Hypothek auf, oder erkläre Bankrott
        return false;
    }
}