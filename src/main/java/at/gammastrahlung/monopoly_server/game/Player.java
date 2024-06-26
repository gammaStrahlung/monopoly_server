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
    @Expose
    protected boolean isInJail;

    /**
     * The field keeps track for how many rounds the player has consecutively spent in jail (max. 3)
     */
    @Expose
    protected int roundsInJail;

    /**
     * Field is true if the player has a get out of jail free card
     */
    @Expose
    protected boolean hasGetOutOfJailFreeCard;

    @Expose
    protected boolean isCheating;

    protected int lastDicedValue;

    /**
     * When a player disconnects, they become a computer player
     */
    protected boolean isComputerPlayer;

    /**
     * The game the player is currently playing
     */
    protected Game currentGame;

    protected int turns;

    public Player(UUID id, String name, Game currentGame, int startingBalance) {
        this.id = id;
        this.name = name;
        this.currentGame = currentGame;
        this.balance = startingBalance; //balance gets initialized with a starting balance
        this.currentFieldIndex = 0;
        this.isInJail = false;
        this.roundsInJail = 0;
        this.hasGetOutOfJailFreeCard = false;
        this.isCheating = false;
        this.lastDicedValue = 0;
        this.turns = 0;
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
     * Updates the player Object on re-join with needed properties depending on the Player Implementation
     *
     * @param player The player object with changed properties
     */
    public void update(Player player) {
        // Player is a real player again
        setComputerPlayer(false);
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
        this.roundsInJail = 0;
    }

    public void incrementRoundsInJail() {
        this.roundsInJail++;
    }

    @Override
    public String toString() {
        return getName();
    }
}
