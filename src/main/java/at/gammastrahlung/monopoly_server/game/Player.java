package at.gammastrahlung.monopoly_server.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class Player {
    /**
     * The unique ID of the player, this can be used by the player to allow for re-joining the ga
     */
    protected UUID ID;

    /**
     * The name of the player (this is shown to other players)
     */
    protected String name;

    /**
     * The game the player is currently playing
     */
    protected Game currentGame;

    /**
     * Updates the player Object with needed properties depending on the Player Implementation
     * @param player The player object with changed properties
     */
    public abstract void update(Player player);
}
