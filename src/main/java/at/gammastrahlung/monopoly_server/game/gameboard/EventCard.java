package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class EventCard {
    private String description;
    private CardType cardType;
    private int payOrGetMoney;
    private int moveToField;

    public void drawnCard(Player player) {
        switch (cardType) {
            case GO_TO_JAIL:
                break;

            case PAY_MONEY_CARD:
                break;

            case GET_MONEY_CARD:
                break;

            case MOVE_TO_FIELD:
                break;

            case MOVE_TO_RAILROAD:
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
}
