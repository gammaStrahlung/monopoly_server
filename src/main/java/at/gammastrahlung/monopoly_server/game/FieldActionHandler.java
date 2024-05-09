package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.EventCard;
import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.FieldType;

import java.security.SecureRandom;
import java.util.List;
import java.util.logging.Logger;

public class FieldActionHandler {

    private static final int INCOME_TAX_AMOUNT = 200;
    private static final int LUXURY_TAX_AMOUNT = 100;
    private Logger logger = Logger.getLogger(FieldActionHandler.class.getName());


    public void handleFieldAction(FieldType fieldType, Player currentPlayer, Game game) {// add other param if needed
        EventCard card;
        switch (fieldType) {
            case GO_TO_JAIL:
                goToJail(currentPlayer);
                break;
            case COMMUNITY_CHEST:
                card = drawCard(game.getGameBoard().getCommunityChestDeck());
                card.applyAction(currentPlayer, card);
                break;
            case CHANCE:
                card = drawCard(game.getGameBoard().getChanceDeck());
                card.applyAction(currentPlayer, card);
                break;
            case INCOME_TAX:
                payTax(currentPlayer, fieldType);
                break;
            case LUXURY_TAX:
                payTax(currentPlayer, fieldType);
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

    void payTax(Player currentPlayer, FieldType fieldType) {
        int taxAmount = (fieldType == FieldType.INCOME_TAX) ? INCOME_TAX_AMOUNT : LUXURY_TAX_AMOUNT;

        if (currentPlayer.getBalance() >= taxAmount) {
            currentPlayer.setBalance(currentPlayer.getBalance() - taxAmount);
        } else {
            /**
             * Player has the options: sell, trade, mortgage, declare bankruptcy (they leave the game)
             */
        }
    }

    public void goToJail(Player currentPlayer) {
        // Logic for handling the "Go to Jail" action
        currentPlayer.goToJail();
    }

    public EventCard drawCard(List<EventCard> deck) {
        SecureRandom random = new SecureRandom();
        if (deck.isEmpty()) {
            // Handle the case when the deck is empty
            return null;
        }

        int randomIndex = random.nextInt(deck.size());
        return deck.get(randomIndex);
    }

}
