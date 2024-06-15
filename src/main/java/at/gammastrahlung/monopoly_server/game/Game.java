package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.*;
import at.gammastrahlung.monopoly_server.network.websocket.WebSocketGameLogger;
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
    private static final String ROLLED = " rolled a ";

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
@Getter
    private static List<Player> playerListForId = new ArrayList<>();
    // Contains all players connected to the game
    @Expose
    private final List<Player> players = new ArrayList<>();

    @Getter
    @Setter
    private GameLogger logger = new WebSocketGameLogger(this);

    @Getter
    @Setter
    private DisconnectNotifier disconnectNotifier;

    private boolean isFirstRound = true;
    private int turnNumber = 0;

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
        Field field = gameBoard.getFields()[fieldId];
        FieldActionHandler handler = new FieldActionHandler();
        if (field != null) {
            handler.handleFieldAction(field, getCurrentPlayer(), this);
        }
    }

    public void rollDice(){
        Player currentPlayer = getCurrentPlayer();
        int diceValue = dice.roll();
        currentPlayer.setLastDicedValue(diceValue);

        // Automatically move the player if it is a computer player
        if (currentPlayer.isComputerPlayer())
            movePlayer();
    }

    public void movePlayer(){
        turnNumber++;
        Player currentPlayer = getCurrentPlayer();

        int diceValue = currentPlayer.getLastDicedValue();


        if(!currentPlayer.isInJail){
            this.getLogger().logMessage(currentPlayer.getName() + ROLLED + diceValue + ".");
        }

        int currentFieldIndex = currentPlayer.getCurrentFieldIndex();
        int nextFieldIndex = (currentFieldIndex + diceValue) % 40;

        if (currentPlayer.isInJail() && currentPlayer.hasGetOutOfJailFreeCard){
            this.getLogger().logMessage(currentPlayer.getName() + " released from Jail because of their 'Get Out Of Jail Free Card'.");
        }

        // Check if player is in jail
        if (currentPlayer.isInJail() && !currentPlayer.hasGetOutOfJailFreeCard) {
            this.getLogger().logMessage(currentPlayer.getName() + " is in Jail, they need doubles to get out.");
            this.getLogger().logMessage(currentPlayer.getName() + ROLLED + diceValue + ".");
            // Player is in Jail and they don't throw doubles
            if (dice.getValue1() != dice.getValue2()) {
                this.getLogger().logMessage("No doubles!");
                playerInJailNoDoubles(currentPlayer, currentFieldIndex, diceValue, nextFieldIndex);
            } else {
                this.getLogger().logMessage("Congrats on doubles! " + currentPlayer.getName() + " is released from Jail and moves for rolled value.");
                playerInJailThrowsDoubles(currentPlayer, currentFieldIndex, diceValue, nextFieldIndex);
            }
        } else {
            movePlayerNotInJail(currentPlayer, currentFieldIndex, diceValue, nextFieldIndex);
        }

        // Automatically end turn if player is computer player
        if (currentPlayer.isComputerPlayer())
            endCurrentPlayerTurn(currentPlayer);
    }

    public void cheating(){
        Player currentPlayer = getCurrentPlayer();
        currentPlayer.setCheating(true);
    }

    public void moveCheatingPlayer(){
        Player currentPlayer = getCurrentPlayer();
        Dice dice = currentPlayer.getCurrentGame().getDice();

        int currentFieldIndex = currentPlayer.getCurrentFieldIndex();
        int nextFieldIndex = (currentFieldIndex + dice.getValue1() + dice.getValue2()) % 40;
        int dicedValue = dice.getValue1() + dice.getValue2();

        this.getLogger().logMessage(currentPlayer.getName() + ROLLED + dicedValue + ".");

        movePlayerNotInJail(currentPlayer, currentFieldIndex, dice.getValue1() + dice.getValue2(), nextFieldIndex);
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
            this.getLogger().logMessage("Rounds spent in Jail: " + currentPlayer.getRoundsInJail() + ". Maximal stay in Jail is 3 rounds." );
        }
        else {
            // max stay in prison is 3 rounds, if they don't dice doubles on the third try, they have to pay
            currentPlayer.pay(GET_OUT_OF_JAIL_FINE);
            currentPlayer.releaseFromJail();
            currentPlayer.moveAvatar(currentFieldIndex, diceValue);

            // Check if player is entitled to bonus salary
            awardBonusMoney(currentFieldIndex, nextFieldIndex, currentPlayer);

            this.getLogger().logMessage(currentPlayer.getName() + " is released from Jail after paying the 'Get out of Jail Fine' of " + GET_OUT_OF_JAIL_FINE + "$." );

            // Handle available actions according to the field the player lands on
            handleFieldAction(currentPlayer.getCurrentFieldIndex());
        }
    }

    public void awardBonusMoney(int currentFieldIndex, int nextFieldIndex, Player currentPlayer){
        if ((nextFieldIndex < currentFieldIndex && nextFieldIndex != 0) || (!isFirstRound && currentFieldIndex == 0 && nextFieldIndex > 0)) {
            currentPlayer.addBalance(BONUS_MONEY);
            this.getLogger().logMessage(currentPlayer.getName() + " has been awarded " + BONUS_MONEY + "$ of bonus money for passing GO");
        }
    }

    public void endCurrentPlayerTurn(Player currentPlayer){
        if(turnNumber > players.size()){
            isFirstRound = false;
        }
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % players.size();
        this.getLogger().logMessage(currentPlayer.getName() + " ended their turn.");

        // Automatically roll dice if player is computer player
        if (getCurrentPlayer().isComputerPlayer())
            rollDice();
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
            // Set initial player balance
            player.setBalance(INITIAL_BALANCE);

            if (state != GameState.STARTED)
                return false; // Can't join when already playing

            if (players.size() >= MAX_PLAYERS)
                return false; // Can't join when game is full

            // Add player to list of players.
            player.currentGame = this;
            players.add(player);
            playerListForId.add(player);

            // First joining player is the gameOwner
            if (gameOwner == null)
                gameOwner = player;

        } else {
            // Player is re-joining -> update old player object
            players.get(players.indexOf(player)).update(player);
            logger.logMessage(player.getName() + " has reconnected.");

            // Check if playerListForId already contains the player
            if (!playerListForId.contains(player)) {
                // If not, add the player to the list
                playerListForId.add(player);
            }
        }

        return true;
    }

    /**
     * Returns the GameState of the game with the given ID
     * @param gameId The id of the game
     * @return the gameState of the game or null if game does not exist
     */
    public static GameState getGameState(int gameId, Player player) {
        Game game = games.get(gameId);

        if (game == null || !game.getPlayers().contains(player))
            return null;
        else
            return game.getState();

    }

    /**
     * Handles when a player disconnects
     * @param player The player that disconnected
     */
    public void playerDisconnected(Player player) {
        // Player will be replaced until reconnect
        player.setComputerPlayer(true);

        // Remove player from playerListForId if they are present
        playerListForId.removeIf(p -> p.getId().equals(player.getId()));


        try {
            // Wait 10 seconds after disconnect
            Thread.sleep(10000);

            // Player has reconnected in the meantime
            if (!player.isComputerPlayer()) {
                // Add player back to playerListForId if they are not present
                if (!playerListForId.contains(player)) {
                    playerListForId.add(player);
                }
                return;
            }

            logger.logMessage(player.getName() + " has disconnected, they will be replaced until they reconnect.");

            // Check if player is game owner
            if (player.equals(gameOwner) && gameOwner.isComputerPlayer()) {
                gameOwner = players.stream().filter(player1 -> !player1.equals(gameOwner)).findFirst().orElseThrow();
                logger.logMessage(gameOwner.getName() + " is now the game owner.");
            }

            // Automatically execute actions when current player disconnected
            if (getCurrentPlayer().equals(player)) {
                rollDice();
            }

            // Notify players of disconnect
            if (disconnectNotifier != null)
                disconnectNotifier.notifyPlayers(player);

        } catch (Exception ignored) {
            Thread.currentThread().interrupt();
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
     * Processes a payment for the given field
     * @param player the player that has to pay
     * @return If the player paid or not
     */
    public boolean processPayment(Player player) {
        Field f = gameBoard.getFields()[player.getCurrentFieldIndex()];

        if (f instanceof Railroad railroad)
            return processRailroadPayment(player, railroad);
        if (f instanceof Property property)
            return processPropertyPayment(player, property);
        if (f instanceof Utility utility)
            return processUtilityPayment(player, utility);

        return false;
    }

    public boolean processRailroadPayment(Player player, Railroad railroad) {
        if (railroad.getOwner() == gameBoard.getBank() || // Don't need to pay the bank
                railroad.getOwner().equals(player)) // Player doesn't need to pay if he owns the Railroad
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
        if (property.getOwner() == gameBoard.getBank() || // Don't need to pay the bank
                property.getOwner().equals(player))  // Player doesn't need to pay if he owns the Property
            return false;

        int houseCount = property.getHouseCount();
        Object rentKey = (houseCount == 5) ? gameBoard.getHotel() : houseCount;
        int rentAmount = property.getRentPrices().getOrDefault(rentKey, 0);
        this.getLogger().logMessage(player + " has paid " + rentAmount + " to " + property.getOwner());
        return makePayment(player, property.getOwner(), rentAmount);
    }

    public boolean processUtilityPayment(Player player, Utility utility) {
        if (utility.getOwner() == gameBoard.getBank() || // Don't need to pay the bank
                utility.getOwner().equals(player)) // Player doesn't need to pay if he owns the Utility
            return false;

        int rentAmount = utility.getToPay(); // Assumes getToPay() gives the correct amount due
        this.getLogger().logMessage(player + " has paid " + rentAmount + " to " + utility.getOwner());
        return makePayment(player, utility.getOwner(), rentAmount);
    }

    private int countOwnedRailroads(Player owner) {
        return (int) java.util.Arrays.stream(gameBoard.getFields())
                .filter(field -> field instanceof Railroad railroad && railroad.getOwner().equals(owner))
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

    public Player getPlayerById(UUID playerId) {

        for (Player player : playerListForId) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }
        return null; // Return null if no player with the given ID is found
    }



}