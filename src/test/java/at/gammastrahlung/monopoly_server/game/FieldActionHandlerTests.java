package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.EventCard;
import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.FieldType;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FieldActionHandlerTests {

    Player mockPlayer;
    Game game;
    FieldActionHandler fieldActionHandler;
    Field mockField;
    GameLogger gameLogger;

    @BeforeEach
    void initialize(){
        mockPlayer = mock(Player.class);
        when(mockPlayer.getId()).thenReturn(UUID.randomUUID());
        when(mockPlayer.getName()).thenReturn("Player");

        // Create an instance of FieldActionHandler
        fieldActionHandler = new FieldActionHandler();
        game = mock(Game.class);
        mockField = mock(Field.class);

        gameLogger = mock(GameLogger.class);

        // Configure the game mock to return the mock GameLogger
        when(game.getLogger()).thenReturn(gameLogger);
    }

    @Test
    void goToJailCase() {
        // Set up the field to return GO_TO_JAIL type
        when(mockField.getType()).thenReturn(FieldType.GO_TO_JAIL);

        // Call the method with the mock field
        fieldActionHandler.handleFieldAction(mockField, mockPlayer, game);

        // Verify the goToJail method was called on the player
        verify(mockPlayer).goToJail();

        // Verify the player's field index is set to 10 (Jail index)
        verify(mockPlayer).setCurrentFieldIndex(10);

        // Verify a log message is generated
        verify(game.getLogger()).logMessage(contains("landed on the 'Go to Jail' field and is sent to Jail"));
    }


    @Test
    void freeParkingCase() {
        // Set up the field to return GO_TO_JAIL type
        when(mockField.getType()).thenReturn(FieldType.FREE_PARKING);

        // Call the method with the mock field
        fieldActionHandler.handleFieldAction(mockField, mockPlayer, game);

        // Verify a log message is generated
        verify(game.getLogger()).logMessage(contains("No action needed."));

    }

    @Test
    void communityChestCardCase() {
        FieldActionHandler spyHandler = spy(new FieldActionHandler());

        ArrayList<EventCard> deck = new ArrayList<>();
        EventCard card1 = mock(EventCard.class);
        EventCard card2 = mock(EventCard.class);
        deck.add(card1);
        deck.add(card2);

        GameBoard gameBoard = mock(GameBoard.class);
        when(game.getGameBoard()).thenReturn(gameBoard);

        // Set up the game with the mocked community chest deck
        when(game.getGameBoard().getCommunityChestDeck()).thenReturn(deck);

        // Set up the field to return GO_TO_JAIL type
        when(mockField.getType()).thenReturn(FieldType.COMMUNITY_CHEST);

        // Call the method to handle the community chest action
        spyHandler.handleFieldAction(mockField, mockPlayer, game);

        // Verify that drawCard was called with the community chest deck
        verify(spyHandler).drawCard(deck);
    }

    @Test
    void chanceCardCase() {
        // Create a spy of the object under test
        FieldActionHandler spyHandler = spy(new FieldActionHandler());

        // Mock the deck
        ArrayList<EventCard> deck = new ArrayList<>();
        EventCard card1 = mock(EventCard.class);
        EventCard card2 = mock(EventCard.class);
        deck.add(card1);
        deck.add(card2);

        GameBoard gameBoard = mock(GameBoard.class);
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(gameBoard.getChanceDeck()).thenReturn(deck);

        // Set up the field to return CHANCE
        when(mockField.getType()).thenReturn(FieldType.CHANCE);

        // Call the method to handle the chance action
        spyHandler.handleFieldAction(mockField, mockPlayer, game);

        // Verify that drawCard was called with the chance deck
        verify(spyHandler).drawCard(deck);
    }

    @Test
    void drawCard() {

        // Mock deck
        List<EventCard> deck = new ArrayList<>();
        EventCard card1 = mock(EventCard.class);
        EventCard card2 = mock(EventCard.class);
        deck.add(card1);
        deck.add(card2);

        // Call the method
        EventCard drawnCard = fieldActionHandler.drawCard(deck);

        // Verify that a card is returned
        assertNotNull(drawnCard);

        // Verify that the returned card is from the deck
        assertTrue(deck.contains(drawnCard));


    }

    @Test
    void drawCardWithEmptyDeck() {
        // Create an empty deck
        List<EventCard> emptyDeck = new ArrayList<>();

        // Call the method with the empty deck
        EventCard drawnCard = fieldActionHandler.drawCard(emptyDeck);

        // Verify that null is returned when the deck is empty
        assertNull(drawnCard);
    }

    @Test
    void incomeTaxCase() {
        // Create a spy of the object under test
        FieldActionHandler spyHandler = spy(new FieldActionHandler());

        // Mock the player
        Player currentPlayer = mock(Player.class);


        when(mockField.getType()).thenReturn(FieldType.INCOME_TAX);
        // Call the method to handle the income tax action
        spyHandler.handleFieldAction(mockField, currentPlayer, game);

        // Verify that payTax was called with the correct parameters
        verify(spyHandler).payTax(currentPlayer, mockField.getType(), game);
    }

    @Test
    void luxuryTaxCase() {
        // Create a spy of the object under test
        FieldActionHandler spyHandler = spy(new FieldActionHandler());

        // Mock the player
        Player currentPlayer = mock(Player.class);

        when(mockField.getType()).thenReturn(FieldType.LUXURY_TAX);
        // Call the method to handle the luxury tax action
        spyHandler.handleFieldAction(mockField, currentPlayer, game);

        // Verify that payTax was called with the correct parameters
        verify(spyHandler).payTax(currentPlayer, mockField.getType(), game);
    }


    @Test
    void payTax() {
        Player player = new Player(UUID.randomUUID(), "Test Player", null, 500);

        when(mockField.getType()).thenReturn(FieldType.INCOME_TAX);
        fieldActionHandler.payTax(player, mockField.getType(), game);
        assertEquals(300, player.getBalance());

        when(mockField.getType()).thenReturn(FieldType.LUXURY_TAX);
        fieldActionHandler.payTax(player, mockField.getType(), game);
        assertEquals(200, player.getBalance());

        when(mockField.getType()).thenReturn(FieldType.INCOME_TAX);
        player.setBalance(10);
        fieldActionHandler.payTax(player, mockField.getType(), game);

        // assert no change since the logic for else branch is not yet impl
        assertEquals(10, player.getBalance());
    }


}
