package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest

class EventCardTests {

    EventCard eventCard;
    Player mockPlayer;
    Game game;
    GameLogger gameLogger;

    @BeforeEach
    void initialize(){
        eventCard = new EventCard();
        mockPlayer = mock(Player.class);
        game = mock(Game.class);

        gameLogger = mock(GameLogger.class);

        // Configure the game mock to return the mock GameLogger
        when(game.getLogger()).thenReturn(gameLogger);
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
    void getMoneyCardCase(){
        // Create an event card representing a GET_MONEY_CARD
        EventCard getMoneyCard = EventCard.builder()
                .description("Pay money card")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(100) // Example amount to pay
                .build();

        // Call the applyAction method with the PAY_MONEY_CARD
        eventCard.applyAction(mockPlayer, getMoneyCard, game);

        // Verify that the player's pay method was called with the correct amount
        verify(mockPlayer).addBalance(100);
    }

    @Test
    void moveToFieldCase(){
        EventCard moveToField = EventCard.builder()
                .description("Advance to GO")
                .cardType(CardType.MOVE_TO_FIELD)
                .moveToField(0)
                .build();

        eventCard.applyAction(mockPlayer, moveToField, game);

        verify(mockPlayer).setCurrentFieldIndex(0);
    }

    @Test
    void getOutOfJailCase(){
        EventCard getOutOfJail = EventCard.builder()
                .description("Get out of Jail")
                .cardType(CardType.GET_OUT_OF_JAIL)
                .build();

        eventCard.applyAction(mockPlayer, getOutOfJail, game);

        verify(mockPlayer).setHasGetOutOfJailFreeCard(true);

    }

    @Test
    void moveToUtilityCase() {
        // Create a spy of the Player object
        Player player = spy(new Player());

        // Create an instance of EventCard with the cardType MOVE_TO_UTILITY
        EventCard moveToUtility = EventCard.builder()
                .description("Move to next Utility")
                .cardType(CardType.MOVE_TO_UTILITY)
                .build();

        // Set the current field index of the player
        int currentPosition = 0;
        player.setCurrentFieldIndex(currentPosition);

        // Set up the expected next utility index
        int expectedNextUtilityIndex = 12; // Assuming the next utility index is 12

        // Call the applyAction method
        eventCard.applyAction(player, moveToUtility, game);

        // Verify that setCurrentFieldIndex is called with the expected next utility index
        verify(player).setCurrentFieldIndex(expectedNextUtilityIndex);
    }

    @Test
    void moveSpacesCase() {
        // Create a spy of the Player object
        Player player = spy(new Player());

        // Create an instance of EventCard with the cardType MOVE_SPACES
        EventCard moveSpacesCard = EventCard.builder()
                .description("Move back 3 spaces")
                .cardType(CardType.MOVE_SPACES)
                .build();

        // Set the current field index of the player
        int currentPosition = 5; // Assuming the player is currently on field 5
        player.setCurrentFieldIndex(currentPosition);

        // Call the applyAction method
        eventCard.applyAction(player, moveSpacesCard, game);

        // Verify that moveAvatar is called with the expected parameters
        verify(player).moveAvatar(currentPosition, -3);
    }

    @Test
    void streetRepairsCase() {
        // Create a spy of the Player object
        Player player = spy(new Player());

        // Create a mock GameBoard
        GameBoard gameBoard = mock(GameBoard.class);

        // Create a mock Property
        Property property = mock(Property.class);
        when(property.getOwner()).thenReturn(player);
        when(property.getHouseCount()).thenReturn(1);

        // Set up the behavior of the mock GameBoard
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(gameBoard.getFields()).thenReturn(new Field[]{property}); // Return a non-null array

        // Create an instance of EventCard with the cardType STREET_REPAIRS
        EventCard streetRepairsCard = EventCard.builder()
                .description("Pay for street repairs.")
                .cardType(CardType.STREET_REPAIRS)
                .build();

        // Call the applyAction method
        eventCard.applyAction(player, streetRepairsCard, game);

        // Verify that the player pays the correct amount
        int amountToPay = 40; // Assuming one property with one house
        verify(player).pay(amountToPay);
    }


    @Test
    void testCalculateStreetRepairs() {
        Player player = new Player();

        GameBoard gameBoard = mock(GameBoard.class);
        when(game.getGameBoard()).thenReturn(gameBoard);

        Property property = mock(Property.class);
        when(property.getOwner()).thenReturn(player);
        when(property.getHouseCount()).thenReturn(1);
        when(gameBoard.getFields()).thenReturn(new Field[]{property});

        int totalPayment = eventCard.calculateStreetRepairs(player, game);
        assertEquals(40, totalPayment);
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
