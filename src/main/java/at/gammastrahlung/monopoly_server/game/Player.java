package at.gammastrahlung.monopoly_server.game;

import com.google.gson.annotations.Expose;
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
    @Expose
    protected UUID ID;

    /**
     * The name of the player (this is shown to other players)
     */
    @Expose
    protected String name;

    /**
     * The game the player is currently playing
     */
    @Expose(serialize = false, deserialize = false) // Should not be sent to the client
    protected Game currentGame;

    /**
     * Updates the player Object with needed properties depending on the Player Implementation
     * @param player The player object with changed properties
     */
    public abstract void update(Player player);
}
