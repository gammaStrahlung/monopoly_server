package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

public class PlayerTest {

    @Test
    public void constructorTest() {
        UUID id = UUID.randomUUID();
        String name = "Test Player";
        int balance = 100;
        Player player = new Player(id, name, null, balance); // Angenommen, der Konstruktor wird entsprechend angepasst

        //assertEquals(id, player.getID());
        //assertEquals(name, player.getName());
        assertEquals(balance, player.getBalance());
        //assertNull(player.getCurrentGame());
    }

    @Test
    public void addBalanceTest() {
        Player player = new Player(UUID.randomUUID(), "Test Player", 100, null);
        player.addBalance(50);
        assertEquals(150, player.getBalance());

        player.addBalance(-20); // Test für negatives Hinzufügen
        assertEquals(130, player.getBalance());
    }

    @Test
    public void subtractBalanceTest() {
        Player player = new Player(UUID.randomUUID(), "Test Player", 100, null);
        player.subtractBalance(50);
        assertEquals(50, player.getBalance());

        player.subtractBalance(60); // Test, der zu negativem Guthaben führt
        assertEquals(-10, player.getBalance());
    }

    @Test
    public void updateTest() {
        // Dieser Test könnte leer sein oder einfach nur die Methode aufrufen, da `update` keine Funktionalität hat
        Player player1 = new Player(UUID.randomUUID(), "Test Player", 100, null);
        Player player2 = new Player(UUID.randomUUID(), "Another Player", 200, null);

        player1.update(player2); // Keine Assertion möglich, da `update` leer ist
    }

        // Create Mock players
       /* players = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            Player mockPlayer = Mockito.mock(Player.class);
            //Mockito.when(mockPlayer.getID()).thenReturn(UUID.randomUUID());
            //Mockito.when(mockPlayer.getName()).thenReturn("Player " + i);

            players.add(mockPlayer);
        }
    }*/
}
