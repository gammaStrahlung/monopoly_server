package at.gammastrahlung.monopoly_server.game;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import at.gammastrahlung.monopoly_server.game.gameboard.Utility;

class UtilityTest {
    Game currentGame;
    @Test
    void testBuyAndSellUtility() {
        currentGame = new Game();
        Player initialOwner = new Player(UUID.randomUUID(), "InitialOwner", currentGame, 1000);
        Player buyer = new Player(UUID.randomUUID(), "Buyer", currentGame, 1000);
        Utility utility = Utility.builder()
                .price(300)
                .owner(initialOwner)
                .build();

        utility.buyAndSellUtility(buyer);
        assertEquals(buyer, utility.getOwner());
        assertEquals(700, buyer.getBalance());
        assertEquals(1300, initialOwner.getBalance());
    }
}
