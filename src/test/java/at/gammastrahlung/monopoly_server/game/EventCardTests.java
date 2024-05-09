package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.CardType;
import at.gammastrahlung.monopoly_server.game.gameboard.EventCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest

public class EventCardTests {

    EventCard eventCard;
    Player mockPlayer;
    Game game;

    @BeforeEach
    void initialize(){
        eventCard = new EventCard();
        mockPlayer = mock(Player.class);
        game = mock(Game.class);
    }

    @Test
    void payMoneyCardCase(){
        // Create an event card representing a PAY_MONEY_CARD
        EventCard payMoneyCard = EventCard.builder()
                .description("Pay money card")
                .cardType(CardType.PAY_MONEY_CARD)
                .payOrGetMoney(100) // Example amount to pay
                .build();

        // Call the applyAction method with the PAY_MONEY_CARD
        eventCard.applyAction(mockPlayer, payMoneyCard, game);

        // Verify that the player's pay method was called with the correct amount
        verify(mockPlayer).pay(100);
    }

    @Test
    void goToJailCase(){
        // Create an event card representing a GO_TO_JAIL card
        EventCard goToJailCard = EventCard.builder()
                .description("Go to Jail")
                .cardType(CardType.GO_TO_JAIL)
                .moveToField(30) // move to jail field
                .build();

        // Call the applyAction method with the go to jail card
        eventCard.applyAction(mockPlayer, goToJailCard, game);

        // Verify goToJail has been called on player
        verify(mockPlayer).goToJail();
    }


    @Test
    void moveToRailroadCase() {
        // Create a spy of the Player object
        Player player = spy(new Player());

        // Create an instance of EventCard with the cardType MOVE_TO_RAILROAD
        EventCard moveToRailroad = EventCard.builder()
                .description("Move to next Railroad")
                .cardType(CardType.MOVE_TO_RAILROAD)
                .build();

        // Set the current field index of the player
        int currentPosition = 0;
        player.setCurrentFieldIndex(currentPosition);

        eventCard.applyAction(player, moveToRailroad, game);

        // Verify that setCurrentFieldIndex is called with the expected next railroad index
        int expectedNextRailroadIndex = 5; // Assuming the next railroad index is 5
        verify(player).setCurrentFieldIndex(expectedNextRailroadIndex);
    }

    @Test
    void findNextRailroadIndex(){
        // Test when the current position is before the first railroad
        assertEquals(5, eventCard.findNextRailroadIndex(0));

        // Test when the current position is between the first and second railroad
        assertEquals(15, eventCard.findNextRailroadIndex(10));

        // Test when the current position is between the second and third railroad
        assertEquals(25, eventCard.findNextRailroadIndex(20));

        // Test when the current position is between the third and fourth railroad
        assertEquals(35, eventCard.findNextRailroadIndex(30));

        // Test when the current position is after the last railroad
        assertEquals(5, eventCard.findNextRailroadIndex(40));
    }

    @Test
    void findNextUtilityIndex() {
        // Test when current position is before the first utility
        assertEquals(12, eventCard.findNextUtilityIndex(0));

        // Test when current position is between the utilities
        assertEquals(12, eventCard.findNextUtilityIndex(10));

        // Test when current position is between the utilities
        assertEquals(28, eventCard.findNextUtilityIndex(20));

        // Test when current position is after the last utility
        assertEquals(12, eventCard.findNextUtilityIndex(30));
    }
}
