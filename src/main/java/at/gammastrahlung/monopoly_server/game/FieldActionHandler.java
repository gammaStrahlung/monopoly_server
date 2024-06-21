package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.EventCard;
import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.FieldType;

import java.security.SecureRandom;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FieldActionHandler {

    private static final int INCOME_TAX_AMOUNT = 200;
    private static final int LUXURY_TAX_AMOUNT = 100;
    private static final String LANDED_ON = " landed on the ";
    private final Logger logger = Logger.getLogger(FieldActionHandler.class.getName());



    public void handleFieldAction(Field field, Player currentPlayer, Game game) {// add other param if needed
        EventCard card;
        switch (field.getType()) {
            case GO, FREE_PARKING, JAIL:
                String messageNoAction = currentPlayer.getName() + LANDED_ON + field.getName() + " field. No action needed.";
                game.getLogger().logMessage(messageNoAction);
                // Nothing should be done on the GO or FREE_PARKING or JAIL (just visiting) field
                break;
            case GO_TO_JAIL:
                goToJail(currentPlayer, game);
                break;
            case COMMUNITY_CHEST:
                String messageCommunity = currentPlayer.getName() + LANDED_ON + field.getName() + " field.";
                game.getLogger().logMessage(messageCommunity);
                card = drawCard(game.getGameBoard().getCommunityChestDeck());
                card.applyAction(currentPlayer, card, game);
                break;
            case CHANCE:
                String messageChance = currentPlayer.getName() + LANDED_ON + field.getName() + " field.";
                game.getLogger().logMessage(messageChance);
                card = drawCard(game.getGameBoard().getChanceDeck());
                card.applyAction(currentPlayer, card, game);
                break;
            case INCOME_TAX, LUXURY_TAX:
                payTax(currentPlayer, field.getType(), game);
                break;
            case RAILROAD, UTILITY, PROPERTY:
                String message = currentPlayer.getName() + " landed on the '" + field.getName() + "' field.";
                game.getLogger().logMessage(message);

                game.processPayment(currentPlayer);
                break;
            default:
                // Temporary Log a message for unimplemented field types but do nothing
                logger.log(Level.INFO, "Unhandled field type: {0}", field.getType());
                break;
        }
    }

    void payTax(Player currentPlayer, FieldType fieldType, Game game) {
        // Check if field income or luxury tax, deduct amount from player account
        int taxAmount = (fieldType == FieldType.INCOME_TAX) ? INCOME_TAX_AMOUNT : LUXURY_TAX_AMOUNT;
        currentPlayer.pay(taxAmount);

        // Log the action
        String taxType = (fieldType == FieldType.INCOME_TAX) ? "Income Tax" : "Luxury Tax";
        String message = currentPlayer.getName() + " landed on " + taxType + " and paid " + taxAmount + "$.";
        game.getLogger().logMessage(message);
    }

    public void goToJail(Player currentPlayer, Game game) {
        // Logic for handling the "Go to Jail" action
        currentPlayer.goToJail();
        currentPlayer.setCurrentFieldIndex(10);
        // Log the action
        String message = currentPlayer.getName() + " landed on the 'Go to Jail' field and is sent to Jail. " +
                "To get out roll doubles next round or use a 'Get Out Of Jail free card'.";
        game.getLogger().logMessage(message);
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
