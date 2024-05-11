package at.gammastrahlung.monopoly_server.game;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Player {
    /**
     * The unique ID of the player, this can be used by the player to allow for re-joining the game
     */
    @Expose
    protected UUID id;

    /**
     * The name of the player (this is shown to other players)
     */
    @Expose
    protected String name;

    @Expose
    protected int balance;

    /**
     * The field on which avatar is currently placed
     */
    @Expose
    protected int currentFieldIndex;

    /**
     * Field is true if player is in jail, false if free
     */
    protected boolean isInJail;

    /**
     * The field keeps track for how many rounds the player has consecutively spent in jail (max. 3)
     */
    protected int roundsInJail;

    /**
     * The game the player is currently playing
     */
    protected Game currentGame;

    public Player(UUID id, String name, Game currentGame, int startingBalance) {
        this.id = id;
        this.name = name;
        this.currentGame = currentGame;
        this.balance = startingBalance; //balance gets initialized with a starting balance
        this.currentFieldIndex = 0;
        this.isInJail = false;
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
     *
     * @param player The player object with changed properties
     */
    public void update(Player player) {
        // will get implemented in next sprint
    }
  
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Player))
            return false;
        return id.equals(((Player) obj).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    /**
     * Updates currentFieldIndex by the diced value
     * @param currentFieldIndex field on which the player is currently positioned
     * @param value how far the player may move forward
     *
     */
    public void moveAvatar(int currentFieldIndex, int value){
        this.currentFieldIndex = (currentFieldIndex + value) % 40;
    }

    /**
     * Updates isInJail to true
     */
    public void goToJail(){
        this.isInJail = true;
        this.roundsInJail = 0; // Reset rounds spent in jail when entering jail
    }

    public void pay(int amountToPay) {
        if (amountToPay <= balance) {
            this.balance -= amountToPay;
        } else {
            //TODO
            // handle insufficient funds, trade, sell, mortgage, declare bankruptcy
        }
    }

    public void releaseFromJail() {
        this.isInJail = false;
    }

    public void incrementRoundsInJail() {
        this.roundsInJail++;
    }
}
