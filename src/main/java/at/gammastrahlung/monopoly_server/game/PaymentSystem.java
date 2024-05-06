package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;


// Class to handle all payment transactions within the game
public class PaymentSystem {

    private final GameBoard gameBoard; // GameBoard instance for accessing game fields

    // Constructor to initialize PaymentSystem with a GameBoard

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentSystem {

    private final GameBoard gameBoard;


    public PaymentSystem(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }


    // Method to process payments for railroads
    public boolean processRailroadPayment(Player payer, Railroad railroad) {
        if (railroad.getOwner() != null && !railroad.getOwner().equals(payer)) {
            int ownedRailroads = countOwnedRailroads(railroad.getOwner());
            String key = ownedRailroads + "RR";
            if (railroad.getRentPrices().containsKey(key)) {
                int rentAmount = railroad.getRentPrices().get(key);
                return makePayment(payer, railroad.getOwner(), rentAmount);
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


    // Method to process payments for properties
    public boolean processPropertyPayment(Player payer, Property property) {
        if (property.getOwner() != null && !property.getOwner().equals(payer)) {
            int rentAmount = property.getRentPrices().getOrDefault("0", 0); // Using string key "0" if using a map with string keys


    private boolean processPropertyPayment(Player payer, Property property) {
        if (!property.getOwner().equals(payer)) {
            int rentAmount = property.getRentPrices().getOrDefault(0, 0); // Default rent if no houses

            return makePayment(payer, property.getOwner(), rentAmount);
        }
        return false;
    }


    // Method to process payments for utilities
    public boolean processUtilityPayment(Player payer, Utility utility) {
        if (utility.getOwner() != null && !utility.getOwner().equals(payer)) {
            int rentAmount = utility.getToPay();  // Assumes getToPay() gives the correct amount due



    private boolean processUtilityPayment(Player payer, Utility utility) {
        if (!utility.getOwner().equals(payer)) {
            int rentAmount = utility.getToPay(); // Oder eine dynamische Berechnung

            return makePayment(payer, utility.getOwner(), rentAmount);
        }
        return false;
    }


    // Helper method to count the number of railroads owned by a player
    public int countOwnedRailroads(Player owner) {
        return (int) java.util.Arrays.stream(gameBoard.getGameBoard())
                .filter(field -> field instanceof Railroad && ((Railroad) field).getOwner().equals(owner))
                .count();
    }

    // Method to execute a payment transaction between two players
    public boolean makePayment(Player from, Player to, int amount) {

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


    // Method to handle cases when a player has insufficient funds
    public boolean handleInsufficientFunds(Player player) {
        // Logic to manage insufficient funds, e.g., selling assets or declaring bankruptcy
        return false; // Placeholder for actual logic
    }
}

    private boolean handleInsufficientFunds(Player player) {
        // Implementiere hier die Logik für unzureichende Mittel
        // Zum Beispiel: Verkaufe Häuser, nimm Hypothek auf, oder erkläre Bankrott
        return false;
    }
}
