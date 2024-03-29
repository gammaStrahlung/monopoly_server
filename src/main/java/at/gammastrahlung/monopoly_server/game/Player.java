package at.gammastrahlung.monopoly_server.game;

import java.util.UUID;

public abstract class Player {
    private UUID ID;

    public UUID getID() {
        return ID;
    }
}
