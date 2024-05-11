package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import at.gammastrahlung.monopoly_server.game.gameboard.GameBoard;
import at.gammastrahlung.monopoly_server.game.gameboard.Property;
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

        currentPlayer.moveAvatar(currentFieldIndex, diceValue);

        // Check if player is entitled to bonus salary
        awardBonusMoney(currentFieldIndex, nextFieldIndex, currentPlayer);

        // Handle available actions according to the field the player lands on
        handleFieldAction(currentPlayer.getCurrentFieldIndex());
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

            return true;
        } else {
            // Player is re-joining -> update old player object
            players.get(players.indexOf(player)).update(player);
            return true;
        }
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
     * Handles a request to mortgage a property by a player.
     * Verifies if the property can be mortgaged and processes the mortgage if valid.
     *
     * @param propertyId The unique identifier of the property to be mortgaged.
     * @param playerId The unique identifier of the player requesting the mortgage.
     * @return true if the mortgage was successfully processed, false otherwise.
     */
    public boolean processMortgage(int propertyId, UUID playerId) {
        Player player = getPlayerById(playerId);
        Property property = gameBoard.getPropertyById(propertyId);

        if (property == null || !property.getOwner().equals(player) || property.isMortgaged()) {
            return false; // Property does not exist, is not owned by the player, or is already mortgaged
        }

        if (property.hasBuildings()) {
            return false; // Cannot mortgage properties with buildings
        }

        // Mortgage the property
        property.setMortgaged(true);
        int mortgageValue = property.getMortgageValue();
        player.addBalance(mortgageValue);
        return true;
    }

    /**
     * Handles a request to repay a mortgage on a property by a player.
     * Verifies if the property mortgage can be repaid and processes the repayment if valid.
     *
     * @param propertyId The unique identifier of the property for which the mortgage is to be repaid.
     * @param playerId The unique identifier of the player requesting to repay the mortgage.
     * @return true if the mortgage repayment was successful, false otherwise.
     */
    public boolean repayMortgage(int propertyId, UUID playerId) {
        Player player = getPlayerById(playerId);
        Property property = gameBoard.getPropertyById(propertyId);

        if (property == null || !property.getOwner().equals(player) || !property.isMortgaged()) {
            return false; // Property does not exist, is not owned by the player, or is not mortgaged
        }

        int repaymentAmount = (int) (property.getMortgageValue() * 1.1); // 10% interest on the mortgage value
        if (player.getBalance() < repaymentAmount) {
            return false; // Player does not have enough balance to repay the mortgage
        }

        // Repay the mortgage
        player.subtractBalance(repaymentAmount);
        property.setMortgaged(false);
        return true;
    }

    /**
     * Retrieves a player based on their UUID.
     *
     * @param playerId The UUID of the player to retrieve.
     * @return The Player object if found, null otherwise.
     */
    private Player getPlayerById(UUID playerId) {
        return players.stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElse(null);
    }
    /**
     * Attempts to mortgage a property owned by the current player.
     * @param propertyId The ID of the property the current player wants to mortgage.
     * @return True if the mortgage operation was successful, false otherwise.
     */
    public boolean mortgageProperty(int propertyId) {
        Player currentPlayer = getCurrentPlayer();
        MortgageManager mortgageManager = new MortgageManager(gameBoard);
        boolean success = mortgageManager.mortgageProperty(propertyId, currentPlayer);
        if (success) {
            // Log or notify the successful mortgage operation
            System.out.println("Property mortgaged successfully.");
        } else {
            // Log or notify the failure
            System.out.println("Failed to mortgage property.");
        }
        return success;
    }
    /**
     * Repays the mortgage on a property owned by the current player.
     * @param propertyId The ID of the property the current player wants to unmortgage.
     * @return True if the mortgage repayment was successful, false otherwise.
     */
    public boolean repayMortgage(int propertyId) {
        Player currentPlayer = getCurrentPlayer();
        MortgageManager mortgageManager = new MortgageManager(gameBoard);
        boolean success = mortgageManager.repayMortgage(propertyId, currentPlayer);
        if (success) {
            // Log or notify about the successful mortgage repayment
            System.out.println("Mortgage repaid successfully.");
        } else {
            // Log or notify about the failure to repay the mortgage
            System.out.println("Failed to repay mortgage.");
        }
        return success;
    }


    public void addPlayer(Player player) {
        this.players.add(player);
    }


}