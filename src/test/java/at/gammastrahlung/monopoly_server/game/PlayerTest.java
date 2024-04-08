package at.gammastrahlung.monopoly_server.game;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

@SpringBootTest
class PlayerTest {

    @Test
     void constructorTest() {
        UUID id = UUID.randomUUID();
        String name = "Test Player";
        int balance = 100;
        Player player = new Player(id, name, null, balance);

        assertEquals(id, player.getId());
        assertEquals(name, player.getName());
        assertEquals(balance, player.getBalance());
        assertNull(player.getCurrentGame());
    }

    @Test
     void addBalanceTest() {
        Player player = new Player(UUID.randomUUID(), "Test Player", 100, null);
        player.addBalance(50);
        assertEquals(150, player.getBalance());

        player.addBalance(-20); // Test für negatives Hinzufügen
        assertEquals(130, player.getBalance());
    }

    @Test
     void subtractBalanceTest() {
        Player player = new Player(UUID.randomUUID(), "Test Player", 100, null);
        player.subtractBalance(50);
        assertEquals(50, player.getBalance());
        player.subtractBalance(60);
        assertEquals(-10, player.getBalance());
    }

    @Test
     void updateTest() {

        Player player1 = new Player(UUID.randomUUID(), "Test Player", 100, null);
        Player player2 = new Player(UUID.randomUUID(), "Another Player", 200, null);

        player1.update(player2);
    }

}
