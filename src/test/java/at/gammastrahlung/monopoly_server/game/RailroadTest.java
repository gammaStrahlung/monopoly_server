package at.gammastrahlung.monopoly_server.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import at.gammastrahlung.monopoly_server.game.gameboard.Railroad;

public class RailroadTest {

    Game game;

    @Test
    public void testBuyAndSellRailroad() {
        game = new Game();
        Player initialOwner = new Player(UUID.randomUUID(), "InitialOwner", game, 1000);
        Player buyer = new Player(UUID.randomUUID(), "Buyer", game, 1000);
        Railroad railroad = Railroad.builder()
                .price(200)
                .owner(initialOwner)
                .build();

        railroad.buyAndSellRailroad(buyer);

        assertEquals(buyer, railroad.getOwner());
        assertEquals(800, buyer.getBalance());
        assertEquals(1200, initialOwner.getBalance());
    }


}
