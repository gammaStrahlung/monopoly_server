package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Game;
import com.google.gson.annotations.Expose;

import at.gammastrahlung.monopoly_server.game.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@SuperBuilder
public class EventCard {

    @Expose
    private String description;

    @Expose
    private CardType cardType;

    @Expose
    private int payOrGetMoney;

    @Expose
    private int moveToField;


    public void applyAction(Player player, EventCard card, Game game) {
        // Log drawn card
        String logMessage = player.getName() + " drew a " + cardType + " card: " + description;
        game.getLogger().logMessage(logMessage);

        // Log the action effect
        String actionEffect = "";
        switch (card.getCardType()) {
            case GO_TO_JAIL:
                player.goToJail();
                actionEffect = player.getName() + " is sent to Jail.";
                break;
            case PAY_MONEY_CARD:
                player.pay(card.getPayOrGetMoney());
                actionEffect = player.getName() + " pays " + card.payOrGetMoney + ".";
                break;
            case GET_MONEY_CARD:
                player.addBalance(card.getPayOrGetMoney());
                actionEffect = player.getName() + " receives " + card.payOrGetMoney + ".";
                break;
            case MOVE_TO_FIELD:
                player.setCurrentFieldIndex(card.getMoveToField());
                actionEffect = player.getName() + " moves to field " + card.moveToField + ".";

                // Handle actions on the field the player gets moved to
                handleFieldAction(player, game);
                break;
            case MOVE_TO_RAILROAD:
                int nextRailroadIndex = findNextRailroadIndex(player.getCurrentFieldIndex());
                player.setCurrentFieldIndex(nextRailroadIndex);
                actionEffect = player.getName() + " moves to the next railroad at " + nextRailroadIndex + ".";

                // Handle actions on the field the player gets moved to
                handleFieldAction(player, game);
                break;
            case STREET_REPAIRS:
                int repairAmount = calculateStreetRepairs(player, game);
                player.pay(repairAmount);
                actionEffect = player.getName() + " pays " + repairAmount + " for street repairs.";
                break;
            case GET_OUT_OF_JAIL:
                player.setHasGetOutOfJailFreeCard(true);
                actionEffect = player.getName() + " receives a 'Get Out of Jail Free' card.";
                break;
            case MOVE_TO_UTILITY:
                int nextUtilityIndex = findNextUtilityIndex(player.getCurrentFieldIndex());
                player.setCurrentFieldIndex(nextUtilityIndex);
                actionEffect = player.getName() + " moves to the next utility at field" + nextUtilityIndex + ".";

                // Handle actions on the field the player gets moved to
                handleFieldAction(player, game);
                break;
            case MOVE_SPACES:
                player.moveAvatar(player.getCurrentFieldIndex(), -3);
                actionEffect = player.getName() + " moves back 3 spaces.";
                
                // Handle actions on the field the player gets moved to
                handleFieldAction(player, game);
                break;
            default:
                break;
        }

        // Log the action effect
        game.getLogger().logMessage(actionEffect);
    }


    public int calculateStreetRepairs(Player player, Game game) {
        int totalPayment = 0;
        int houseCount = 0;

        GameBoard gameBoard = game.getGameBoard();

        for(Field field : gameBoard.getFields()){
            if(field instanceof Property property && property.getOwner() == player){
                houseCount += ((Property) field).getHouseCount();
            }
        }

        totalPayment = (houseCount * 40);
        return totalPayment;
    }

    private static void handleFieldAction(Player player, Game game) {
        game.handleFieldAction(player.getCurrentFieldIndex());
    }

    public int findNextRailroadIndex(int currentPosition) {
        int[] railroadIndexes = {5, 15, 25, 35};

        for (int index : railroadIndexes) {
            // If the current position is less than the current railroad index
            if (currentPosition < index) {
                return index; // Return the next railroad index
            }
        }

        // If the current position is beyond the last railroad index, return the first one
        return railroadIndexes[0];
    }

    public int findNextUtilityIndex(int currentPosition) {
        int[] utilityIndexes = {12, 28};
        for (int index : utilityIndexes) {
            if (index > currentPosition) {
                return index;
            }
        }
        return utilityIndexes[0]; // If no next utility index is found, return the current position
    }



}
