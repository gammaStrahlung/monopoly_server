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
                int amountToPay = card.getPayOrGetMoney();
                player.pay(amountToPay);
                break;
            case GET_MONEY_CARD:
                int amount = card.getPayOrGetMoney();
                player.addBalance(amount);
                break;
            case MOVE_TO_FIELD:
                int fieldIndex = card.getMoveToField();
                player.setCurrentFieldIndex(fieldIndex);
                break;
            case MOVE_TO_RAILROAD:
                int currentPosition = player.getCurrentFieldIndex();
                int nextRailroadIndex = findNextRailroadIndex(currentPosition);
                player.setCurrentFieldIndex(nextRailroadIndex);
                break;
            case STREET_REPAIRS:
                break;

            case GET_OUT_OF_JAIL:
                break;

            case MOVE_TO_UTILITY:
                break;

            case MOVE_SPACES:
                break;
            default:
                break;
        }
    }

    public int findNextRailroadIndex(int currentPosition) {
        int[] railroadIndexes = {5, 15, 25, 35};

        // Iterate through the game board array
        for (int index : railroadIndexes) {
            // If the current position is less than the current railroad index
            if (currentPosition < index) {
                return index; // Return the next railroad index
            }
        }

        // If the current position is beyond the last railroad index, return the first one
        return railroadIndexes[0];
    }




}
