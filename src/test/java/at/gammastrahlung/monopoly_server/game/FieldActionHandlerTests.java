package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.FieldType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.mockito.Mockito.*;

@SpringBootTest
class FieldActionHandlerTests {

    @Test
    void goToJail(){
        Player mockPlayer = mock(Player.class);
        when(mockPlayer.getId()).thenReturn(UUID.randomUUID());
        when(mockPlayer.getName()).thenReturn("Player");

        Game game = mock(Game.class);

        FieldActionHandler.handleFieldAction(FieldType.GO_TO_JAIL, mockPlayer, game);

        verify(mockPlayer).goToJail();
    }

}
