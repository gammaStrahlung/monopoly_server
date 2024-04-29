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
    void equalsTest() {
        UUID playerid = UUID.randomUUID();

        Player p1 = new Player();
        p1.setId(playerid);

        Player p2 = new Player();
        p2.setId(playerid);

        // Players with the same UUID are the same player
        assertEquals(p1, p2);

        // Other object is not equal
        Object a = 1;
        assertNotEquals(p1, a);
    }

    @Test
    void hashCodeTest() {
        Player p1 = new Player();
        p1.setId(UUID.randomUUID());

        Player p2 = new Player();
        p2.setId(UUID.randomUUID());

        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void moveAvatarTest(){
        Player player = new Player(UUID.randomUUID(), "Test Player", null, 100);
        assertEquals(0, player.getCurrentField());

        player.moveAvatar(player.getCurrentField(), 7);
        assertEquals(7,player.getCurrentField());

        player.moveAvatar(player.currentField,46);
        assertEquals(13, player.getCurrentField());

    }
}
