package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;
import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public class Game {
    // Game configuration
    private static final int MIN_GAME_ID = 100000;
    private static final int MAX_GAME_ID = 999999;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 8;

    // bonus money the player gets for passing the start
    private static final int BONUS_MONEY = 200;
    private static final int GET_OUT_OF_JAIL_FINE = 50;

    // Initial balance
    private static final int INITIAL_BALANCE = 1500;

    // Contains all games that are currently being played, that allows us to find a specific game
    private static final ConcurrentHashMap<Integer, Game> games = new ConcurrentHashMap<>();

    // Getters and Setters below:
    // The gameId is used by users to connect to the current game, it can not be changed after the game is started
    @Getter
    @Expose
    private int gameId;

    // Current state of the game
    @Getter
    @Expose
    private GameState state = GameState.STARTED;

    // The owner of the game. This is the only player that can start and end the game
    @Getter
    @Expose
    private Player gameOwner = null;

    // The game board
    @Getter
    @Expose
    GameBoard gameBoard = new GameBoard();

    @Getter
    @Expose
    @Setter
    Dice dice = new Dice();

    @Getter
    private int currentPlayerIndex = 0;


    // Contains all players connected to the game
    @Expose
    private final List<Player> players = new ArrayList<>();


    /**
     * Creates a new game and sets the gameId
     */
    public Game() {
        Random rand = new Random();
        // Ensure the gameId is unique.
        do {
            gameId = rand.nextInt(MIN_GAME_ID, MAX_GAME_ID);
        } while (games.containsKey(gameId));

        // Add this game to all currently played games
        games.put(gameId, this);
        initializeGameBoard();
    }

    /**
     * Starts the game, if all prerequisites are met and the player requesting the start is the gameOwner.
     *
     * @param player The player that wants to start the game.
     * @return If the game was successfully started.
     */
    public boolean startGame(Player player) {
        if (player == null) // Player can not be null
            return false;
        if (!player.equals(gameOwner)) // Only the gameOwner can start the game
            return false;
        if (state != GameState.STARTED)
            return false; // Can't start an already playing game or an ended game
        if (players.size() < MIN_PLAYERS)
            return false; // Not enough players

        // Generate a random index within the range of 0 to player list length
        SecureRandom random = new SecureRandom();
        currentPlayerIndex = random.nextInt(players.size());


        state = GameState.PLAYING;
        return true;
    }

    public void handleFieldAction(int fieldId) {
        Field field = gameBoard.getGameBoard()[fieldId];
        FieldActionHandler handler = new FieldActionHandler();
        if (field != null) {
            handler.handleFieldAction(field.getType(), getCurrentPlayer(), this);
        }
    }

    public void rollDiceAndMoveCurrentPlayer(){
        Player currentPlayer = getCurrentPlayer();
        int diceValue = dice.roll();
        int currentFieldIndex = currentPlayer.getCurrentFieldIndex();
        int nextFieldIndex = (currentFieldIndex + diceValue) % 40;


        // Check if player is in jail
        if (currentPlayer.isInJail() && !currentPlayer.hasGetOutOfJailFreeCard) {
            // Player is in Jail and they don't throw doubles
            if (dice.getValue1() != dice.getValue2()) {
                playerInJailNoDoubles(currentPlayer, currentFieldIndex, diceValue, nextFieldIndex);
            } else {
                playerInJailThrowsDoubles(currentPlayer, currentFieldIndex, diceValue, nextFieldIndex);
            }
        } else {
            movePlayerNotInJail(currentPlayer, currentFieldIndex, diceValue, nextFieldIndex);
        }

    }

    private void movePlayerNotInJail(Player currentPlayer, int currentFieldIndex, int diceValue, int nextFieldIndex) {
        // Player is not in jail, proceed with moving them
        currentPlayer.moveAvatar(currentFieldIndex, diceValue);

        // Check if player is entitled to bonus salary
        awardBonusMoney(currentFieldIndex, nextFieldIndex, currentPlayer);

        // Handle available actions according to the field the player lands on
        handleFieldAction(currentPlayer.getCurrentFieldIndex());
    }

    private void playerInJailThrowsDoubles(Player currentPlayer, int currentFieldIndex, int diceValue, int nextFieldIndex) {
        // Player throws doubles, release them from jail and proceed with moving them
        currentPlayer.releaseFromJail();
        currentPlayer.moveAvatar(currentFieldIndex, diceValue);

        // Check if player is entitled to bonus salary
        awardBonusMoney(currentFieldIndex, nextFieldIndex, currentPlayer);

        // Handle available actions according to the field the player lands on
        handleFieldAction(currentPlayer.getCurrentFieldIndex());
    }

    private void playerInJailNoDoubles(Player currentPlayer, int currentFieldIndex, int diceValue, int nextFieldIndex) {
        if(currentPlayer.getRoundsInJail() < 3){
            currentPlayer.incrementRoundsInJail();
        }
        else {
            // max stay in prison is 3 rounds, if they don't dice doubles on the third try, they have to pay
            currentPlayer.pay(GET_OUT_OF_JAIL_FINE);
            currentPlayer.releaseFromJail();
            currentPlayer.moveAvatar(currentFieldIndex, diceValue);

            // Check if player is entitled to bonus salary
            awardBonusMoney(currentFieldIndex, nextFieldIndex, currentPlayer);

            // Handle available actions according to the field the player lands on
            handleFieldAction(currentPlayer.getCurrentFieldIndex());
        }
    }

    public void awardBonusMoney(int currentFieldIndex, int nextFieldIndex, Player currentPlayer){
        if(nextFieldIndex < currentFieldIndex && nextFieldIndex > 0){
            currentPlayer.addBalance(BONUS_MONEY);
        }
    }

    public void endCurrentPlayerTurn(){
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % players.size();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex % getPlayers().size();
    }

    /**
     * Ends the game, if the player requesting the end is the gameOwner
     *
     * @param player The player that wants to end the game.
     * @return If ending the game was successful
     */
    public boolean endGame(Player player) {
        if (player == null) // Player can not be null
            return false;
        if (!player.equals(gameOwner)) // Only the gameOwner can end the game
            return false;

        state = GameState.ENDED;

        // Remove this game from the games to free the gameId
        games.remove(gameId);

        gameId = 0;

        // Change current game of all players to null
        for (Player p : players) {
            p.setCurrentGame(null);
        }

        // Clear players
        players.clear();

        return true;
    }

    public static Game joinByGameId(int gameId, Player player) {
        Game g = games.get(gameId);

        if (g != null) {
            boolean ok = g.join(player);

            if (!ok)
                return null; // Join was not successful
        }

        return g;
    }

    /**
     * Allows a player to join a game if the prerequisites are met.
     *
     * @param player The player that should join this game.
     * @return If the join was successful.
     */
    public boolean join(Player player) {
        // Set initial player balance
        player.setBalance(INITIAL_BALANCE);

        // Check if player is new and does not re-join the game.
        // If the player re-joins the game, skip the join checks.
        if (!players.contains(player)) {
            if (state != GameState.STARTED)
                return false; // Can't join when already playing

            if (players.size() >= MAX_PLAYERS)
                return false; // Can't join when game is full

            // Add player to list of players.
            player.currentGame = this;
            players.add(player);

            // First joining player is the gameOwner
            if (gameOwner == null)
                gameOwner = player;

        } else {
            // Player is re-joining -> update old player object
            players.get(players.indexOf(player)).update(player);
        }

        return true;
    }

     private void initializeGameBoard() {
        // Initialize the game board
        gameBoard.initializeGameBoard();
        gameBoard.initializeChanceDeck();
        gameBoard.initializeCommunityChestDeck();
    }
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public enum GameState {
        /**
         * Game was started but playing has not yet begun.
         * In this state, the game will accept joining players.
         */
        STARTED,
        /**
         * The game has started and players are playing the game.
         * It is not possible for new players to join the game,
         * players that disconnected are still able to re-join the game.
         */
        PLAYING,
        /**
         * The game has ended, playing and joining is not possible anymore.
         */
        ENDED
    }

    /**
     * Processes a payment for the given field
     * @param player
     * @return
     */
    public boolean processPayment(Player player) {
        Field f = gameBoard.getGameBoard()[player.getCurrentFieldIndex()];

        if (f instanceof Railroad)
            return processRailroadPayment(player, (Railroad) f);
        if (f instanceof Property)
            return processPropertyPayment(player, (Property) f);
        if (f instanceof Utility)
            return processUtilityPayment(player, (Utility) f);

        return false;
    }

    public boolean processRailroadPayment(Player player, Railroad railroad) {
        if (railroad.getOwner() != gameBoard.getBank() && // Don't need to pay the bank
                !railroad.getOwner().equals(player)) // Player doesn't need to pay if he owns the Railroad
            return false;

        int ownedRailroads = countOwnedRailroads(railroad.getOwner());
        String key = ownedRailroads + "RR";
        Integer rentAmount = railroad.getRentPrices().get(key);
        if (rentAmount != null) {
            return makePayment(player, railroad.getOwner(), rentAmount);
        }

        return false;
    }

    public boolean processPropertyPayment(Player player, Property property) {
        if (property.getOwner() != gameBoard.getBank() && // Don't need to pay the bank
                !property.getOwner().equals(player))  // Player doesn't need to pay if he owns the Property
            return false;

        int houseCount = property.getHouseCount();
        Object rentKey = (houseCount == 5) ? gameBoard.getHOTEL() : houseCount;
        int rentAmount = property.getRentPrices().getOrDefault(rentKey, 0);
        return makePayment(player, property.getOwner(), rentAmount);
    }

    public boolean processUtilityPayment(Player player, Utility utility) {
        if (utility.getOwner() != gameBoard.getBank() && // Don't need to pay the bank
                !utility.getOwner().equals(player)) // Player doesn't need to pay if he owns the Utility
            return false;

        int rentAmount = utility.getToPay(); // Assumes getToPay() gives the correct amount due
        return makePayment(player, utility.getOwner(), rentAmount);
    }

    private int countOwnedRailroads(Player owner) {
        return (int) java.util.Arrays.stream(gameBoard.getGameBoard())
                .filter(field -> field instanceof Railroad && ((Railroad) field).getOwner().equals(owner))
                .count();
    }

    public boolean makePayment(Player from, Player to, int amount) {
        if (from.getBalance() >= amount) {
            from.subtractBalance(amount);
            to.addBalance(amount);
            return true;
        } else {
            return handleInsufficientFunds(from);
        }
    }

    private boolean handleInsufficientFunds(Player player) {
        // Implement logic for insufficient funds here
        // For example: Sell houses, take out mortgage, or declare bankruptcy
        return false;
    }
}