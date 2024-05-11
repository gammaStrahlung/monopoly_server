package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.EventCard;
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
            case GO, FREE_PARKING, JAIL:
                // Nothing should be done on the GO or FREE_PARKING or JAIL (just visiting) field
                break;
            case GO_TO_JAIL:
                goToJail(currentPlayer);
                break;
            case COMMUNITY_CHEST:
                card = drawCard(game.getGameBoard().getCommunityChestDeck());
                card.applyAction(currentPlayer, card, game);
                break;
            case CHANCE:
                card = drawCard(game.getGameBoard().getChanceDeck());
                card.applyAction(currentPlayer, card, game);
                break;
            case INCOME_TAX:
                payTax(currentPlayer, fieldType);
                break;
            case LUXURY_TAX:
                payTax(currentPlayer, fieldType);
                break;
            // TODO Add cases for other field types RAILROAD, UTILITY, PROPERTY
            default:
                // Temporary Log a message for unimplemented field types but do nothing
                logger.info("Unhandled field type: " + fieldType);
                break;
        }
    }

    void payTax(Player currentPlayer, FieldType fieldType) {
        // Check if field income or luxury tax, deduct amount from player account
        int taxAmount = (fieldType == FieldType.INCOME_TAX) ? INCOME_TAX_AMOUNT : LUXURY_TAX_AMOUNT;
        currentPlayer.pay(taxAmount);

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
