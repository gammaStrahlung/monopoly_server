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

    protected int balance;

    /**
     * The game the player is currently playing
     */
    @Expose(serialize = false, deserialize = false) // Should not be sent to the client
    protected Game currentGame;

    public Player(UUID ID, String name, Game currentGame, int startingBalance) {
        this.ID = ID;
        this.name = name;
        this.currentGame = currentGame;
        this.balance = startingBalance; //balance gets initialized with a starting balance
    }

    // increases player balance
    public synchronized void addBalance(int amount) {
        this.balance += amount;
    }

    // decreases player balance - balance can also gio into the negatives
    public synchronized void subtractBalance(int amount) {
            this.balance -= amount;
    }

    /**
     * Updates the player Object with needed properties depending on the Player Implementation
     * @param player The player object with changed properties
     */
    public abstract void update(Player player);
}
