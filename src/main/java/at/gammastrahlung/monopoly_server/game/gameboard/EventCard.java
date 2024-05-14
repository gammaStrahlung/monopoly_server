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
        switch (card.getCardType()) {
            case GO_TO_JAIL:
                player.goToJail();
                break;
            case PAY_MONEY_CARD:
                player.pay(card.getPayOrGetMoney());
                break;
            case GET_MONEY_CARD:
                player.addBalance(card.getPayOrGetMoney());
                break;
            case MOVE_TO_FIELD:
                player.setCurrentFieldIndex(card.getMoveToField());

                // Handle actions on the field the player gets moved to
                handleFieldAction(player, game);
                break;
            case MOVE_TO_RAILROAD:
                int nextRailroadIndex = findNextRailroadIndex(player.getCurrentFieldIndex());
                player.setCurrentFieldIndex(nextRailroadIndex);

                // Handle actions on the field the player gets moved to
                handleFieldAction(player, game);
                break;
            case STREET_REPAIRS:
                int repairAmount = calculateStreetRepairs(player, game);
                player.pay(repairAmount);
                break;
            case GET_OUT_OF_JAIL:
                player.setHasGetOutOfJailFreeCard(true);
                break;
            case MOVE_TO_UTILITY:
                int nextUtilityIndex = findNextUtilityIndex(player.getCurrentFieldIndex());
                player.setCurrentFieldIndex(nextUtilityIndex);

                // Handle actions on the field the player gets moved to
                handleFieldAction(player, game);
                break;
            case MOVE_SPACES:
                player.moveAvatar(player.getCurrentFieldIndex(), -3);

                // Handle actions on the field the player gets moved to
                handleFieldAction(player, game);
                break;
            default:
                break;
        }
    }


    public int calculateStreetRepairs(Player player, Game game) {
        int totalPayment = 0;
        int houseCount = 0;

        GameBoard gameBoard = game.getGameBoard();

        for(Field field : gameBoard.getGameBoard()){
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
