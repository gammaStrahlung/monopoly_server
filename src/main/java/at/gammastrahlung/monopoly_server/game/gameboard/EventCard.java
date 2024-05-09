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
                break;
            case MOVE_TO_RAILROAD:
                int nextRailroadIndex = findNextRailroadIndex(player.getCurrentFieldIndex());
                player.setCurrentFieldIndex(nextRailroadIndex);
                break;
            case STREET_REPAIRS:
                break;
            case GET_OUT_OF_JAIL:
                player.getOutOfJail();
                break;
            case MOVE_TO_UTILITY:
                int nextUtilityIndex = findNextUtilityIndex(player.getCurrentFieldIndex());
                player.setCurrentFieldIndex(nextUtilityIndex);
                break;
            case MOVE_SPACES:
                player.moveAvatar(player.getCurrentFieldIndex(), -3);
                break;
            default:
                break;
        }
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
