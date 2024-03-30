package at.gammastrahlung.monopoly_server.game;

import java.util.UUID;

public class Player {
    /**
     * The unique ID of the player, this can be used by the player to allow for re-joining the ga
     */
    private UUID ID;

    /**
     * The name of the player (this is shown to other players)
     */
    private String name;

    public Player() {}

    public Player(UUID ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    // Getters and Setters below:
    public UUID getID() {
        return ID;
    }

    public void setID(UUID ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
