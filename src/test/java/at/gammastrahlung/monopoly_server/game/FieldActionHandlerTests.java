package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.EventCard;
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

    @BeforeEach
    void initialize(){
        mockPlayer = mock(Player.class);
        when(mockPlayer.getId()).thenReturn(UUID.randomUUID());
        when(mockPlayer.getName()).thenReturn("Player");

        // Create an instance of FieldActionHandler
        fieldActionHandler = new FieldActionHandler();
        game = mock(Game.class);
    }

    @Test
    void goToJail(){
        fieldActionHandler.handleFieldAction(FieldType.GO_TO_JAIL, mockPlayer, game);

        verify(mockPlayer).goToJail();
    }

    @Test
    void freeParking() {
        fieldActionHandler.handleFieldAction(FieldType.FREE_PARKING, mockPlayer, game);

        // Verify that nothing has changed
        verifyNoInteractions(mockPlayer);
    }

    @Test
    void drawCommunityChestCard() {
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

        // Call the method to handle the community chest action
        spyHandler.handleFieldAction(FieldType.COMMUNITY_CHEST, mockPlayer, game);

        // Verify that drawCard was called with the community chest deck
        verify(spyHandler).drawCard(deck);
    }

    @Test
    void drawChanceCard() {
        // Create a spy of the object under test
        FieldActionHandler spyHandler = spy(new FieldActionHandler());

        // Mock the deck
        ArrayList<EventCard> deck = new ArrayList<>();
        EventCard card1 = mock(EventCard.class);
        EventCard card2 = mock(EventCard.class);
        deck.add(card1);
        deck.add(card2);

        // Mock the game and game board
        Game game = mock(Game.class);
        GameBoard gameBoard = mock(GameBoard.class);
        when(game.getGameBoard()).thenReturn(gameBoard);
        when(gameBoard.getChanceDeck()).thenReturn(deck);

        // Call the method to handle the chance action
        spyHandler.handleFieldAction(FieldType.CHANCE, mockPlayer, game);

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

}
