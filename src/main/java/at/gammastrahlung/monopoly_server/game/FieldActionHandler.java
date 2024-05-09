package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.FieldType;

public class FieldActionHandler {

    public static void handleFieldAction(FieldType fieldType, Player currentPlayer) {// add other param if needed
        switch (fieldType) {
            case GO_TO_JAIL:
                goToJail(currentPlayer);
                break;
            case INCOME_TAX:
                // pay income tax
                break;
            case FREE_PARKING:
                // Nothing should be done on the free parking field
                break;
            // Add cases for other field types
            default:
                throw new IllegalArgumentException("Invalid FieldType");
        }
    }

    private static void goToJail(Player currentPlayer) {
        // Logic for handling the "Go to Jail" action
        currentPlayer.goToJail();
    }
}
