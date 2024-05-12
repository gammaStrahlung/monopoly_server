package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;

public class PaymentSystem {
    private final GameBoard gameBoard;

    public PaymentSystem(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }






    private boolean handleInsufficientFunds(Player player) {
        // Implement logic for insufficient funds here
        // For example: Sell houses, take out mortgage, or declare bankruptcy
        return false;
    }
}