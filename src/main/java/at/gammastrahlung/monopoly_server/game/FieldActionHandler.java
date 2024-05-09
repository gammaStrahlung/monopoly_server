package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.FieldType;

import java.util.logging.Logger;

public class FieldActionHandler {

    static Logger logger = Logger.getLogger(FieldActionHandler.class.getName());

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
                // Temporary Log a message for unimplemented field types but do nothing
                logger.info("Unhandled field type: " + fieldType);
                break;
        }
    }

    private static void goToJail(Player currentPlayer) {
        // Logic for handling the "Go to Jail" action
        currentPlayer.goToJail();
    }
}
