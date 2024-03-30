package at.gammastrahlung.monopoly_server.game;

import java.util.UUID;

public abstract class Player {
    /**
     * The unique ID of the player
     */
    private UUID ID;

    /**
     * The name of the player (this is shown to other players)
     */
    private String name;

    // Getters and Setters below:
    public UUID getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
