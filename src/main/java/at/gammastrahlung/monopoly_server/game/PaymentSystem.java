package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;

/**
 * Handles payments related to game actions.
 */
public class PaymentSystem {

    private final Game game;

    public PaymentSystem(Game game) {
        this.game = game;
    }

    /**
     * Processes payments for landing on a railroad property.
     *
     * @param payer The player making the payment.
     * @param railroad The railroad property involved.
     * @return true if payment was successful, false otherwise.
     */
    public boolean processRailroadPayment(Player payer, Railroad railroad) {
        return game.processRailroadPayment(payer, railroad);
    }

    /**
     * Processes payments for landing on a utility property.
     *
     * @param payer The player making the payment.
     * @param utility The utility property involved.
     * @return true if payment was successful, false otherwise.
     */
    public boolean processUtilityPayment(Player payer, Utility utility) {
        return game.processUtilityPayment(payer, utility);
    }

    /**
     * Processes payments for landing on a property with houses or hotels.
     *
     * @param payer The player making the payment.
     * @param property The property involved.
     * @return true if payment was successful, false otherwise.
     */
    public boolean processPropertyPayment(Player payer, Property property) {
        return game.processPropertyPayment(payer, property);
    }

    /**
     * Makes a payment from one player to another.
     *
     * @param from The player who is paying.
     * @param to The recipient of the payment.
     * @param amount The amount to be paid.
     * @return true if the payment was successful, false if the payer did not have enough money.
     */
    public boolean makePayment(Player from, Player to, int amount) {
        return game.makePayment(from, to, amount);
    }
}