package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;
import static org.mockito.Mockito.*;

@SpringBootTest
class FieldActionHandlerTests {

    Player mockPlayer;
    Game game;

    @BeforeEach
    void initialize(){
        mockPlayer = mock(Player.class);
        when(mockPlayer.getId()).thenReturn(UUID.randomUUID());
        when(mockPlayer.getName()).thenReturn("Player");

        game = mock(Game.class);
    }

    @Test
    void goToJail(){
        FieldActionHandler.handleFieldAction(FieldType.GO_TO_JAIL, mockPlayer, game);

        verify(mockPlayer).goToJail();
    }

    @Test
    void freeParking() {
        FieldActionHandler.handleFieldAction(FieldType.FREE_PARKING, mockPlayer, game);

        // Verify that nothing has changed
        verifyNoInteractions(mockPlayer);
    }

}
