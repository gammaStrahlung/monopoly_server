package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.FieldType;

public class FieldActionHandler {

    public static void handleFieldAction(FieldType fieldType, Player currentPlayer, Game game) {
        switch (fieldType) {
            case GO_TO_JAIL:
                goToJail(currentPlayer);
                break;
            case INCOME_TAX:
                // pay income tax
                break;
            case LUXURY_TAX:
                // pay luxury tax
                break;

            // Add cases for other field types
        }
    }

    private static void goToJail(Player currentPlayer) {
        // Logic for handling the "Go to Jail" action
        currentPlayer.goToJail();
    }
}
