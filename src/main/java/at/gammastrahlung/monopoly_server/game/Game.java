package at.gammastrahlung.monopoly_server.game;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    // Game configuration
    private static final int MIN_GAME_ID = 100000;
    private static final int MAX_GAME_ID = 999999;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 8;


    // Contains all games that are currently being played, that allows us to find a specific game
    private static final ConcurrentHashMap<Integer, Game> games = new ConcurrentHashMap<>();

    // Getters and Setters below:
    // The gameId is used by users to connect to the current game, it can not be changed after the game is started
    @Getter
    private int gameId;
    // Current state of the game
    @Getter
    private GameState state = GameState.STARTED;

    // Contains all players connected to the game
    private final ConcurrentHashMap<UUID, Player> players = new ConcurrentHashMap<>();

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
    }

    /**
     * Starts the game, if all prerequisites are met.
     *
     * @return If the game was successfully started.
     */
    public boolean startGame() {
        if (state != GameState.STARTED)
            return false; // Can't start an already playing game or an ended game
        if (players.size() < MIN_PLAYERS)
            return false; // Not enough players

        state = GameState.PLAYING;
        return true;
    }

    /**
     * Ends the game
     */
    public void endGame() {
        state = GameState.ENDED;
        games.remove(gameId);
        gameId = 0;

        // Remove this game from the games to free the gameId
        games.remove(gameId);

        // Change current game of all players to null
        for (Map.Entry<UUID, Player> entry : players.entrySet()) {
            entry.getValue().setCurrentGame(null);
        }

        // Clear players
        players.clear();
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
        if (!players.containsKey(player.getId())) {
            if (state != GameState.STARTED)
                return false; // Can't join when already playing

            if (players.size() >= MAX_PLAYERS)
                return false; // Can't join when game is full

            // Add player to list of players.
            player.currentGame = this;
            players.put(player.getId(), player);
            return true;
        } else {
            // Player is re-joining -> update old player object
            players.get(player.getId()).update(player);
            return true;
        }
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(Collections.list(players.elements()));
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
     * Temporary diceRolling function will be replaced with the actual value
     * @value1 + @value2 the diced value
     */
    public static int diceRolling(){
        int value1 = new Random().nextInt(6) + 1;
        int value2 = new Random().nextInt(6) + 1;

        return value1 + value2;
    }
}