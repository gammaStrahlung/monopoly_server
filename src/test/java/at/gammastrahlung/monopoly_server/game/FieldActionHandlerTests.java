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

    @BeforeEach
    void initialize(){
        mockPlayer = mock(Player.class);
        when(mockPlayer.getId()).thenReturn(UUID.randomUUID());
        when(mockPlayer.getName()).thenReturn("Player");
    }

    @Test
    void goToJail(){
        FieldActionHandler.handleFieldAction(FieldType.GO_TO_JAIL, mockPlayer);

        verify(mockPlayer).goToJail();
    }

    @Test
    void freeParking() {
        FieldActionHandler.handleFieldAction(FieldType.FREE_PARKING, mockPlayer);

        // Verify that nothing has changed
        verifyNoInteractions(mockPlayer);
    }

}
