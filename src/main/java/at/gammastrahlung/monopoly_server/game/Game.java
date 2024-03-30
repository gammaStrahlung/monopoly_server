package at.gammastrahlung.monopoly_server.game;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Game {
    // Game configuration
    private static final int minGameId = 100000;
    private static final int maxGameId = 999999;
    private static final int minPlayers = 2;
    private static final int maxPlayers = 8;


    // Contains all games that are currently being played, that allows us to find a specific game
    private static final ConcurrentHashMap<Integer, Game> games = new ConcurrentHashMap<>();

    // The gameId is used by users to connect to the current game, it can not be changed after the game is started
    private int gameId = -1;
    // Current state of the game
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
            gameId = rand.nextInt(minGameId, maxGameId);
        } while (!games.containsKey(gameId));

        // Add this game to all currently played games
        games.put(gameId, this);
    }

    /**
     * Starts the game, if all prerequisites are met.
     *
     * @return If the game was successfully started.
     */
    private boolean startGame() {
        if (state != GameState.STARTED)
            return false; // Can't start an already playing game or an ended game
        if (players.size() < minPlayers)
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
        gameId = -1;
    }

    public static boolean joinByGameId(int gameId, Player player) {
        Game g = games.get(gameId);

        if (g == null)
            return false; // Game with id does not exist
        else
            return g.join(player);
    }

    /**
     * Allows a player to join a game if the prerequisites are met.
     *
     * @param player The player that should join this game.
     * @return If the join was successful.
     */
    public boolean join(Player player) {
        if (state != GameState.STARTED)
            return false; // Can't join when already playing
        if (players.size() > maxPlayers)
            return false; // Can't join when game is full

        players.put(player.getID(), player);
        return true;
    }

    // Getters and Setters below:
    public int getGameId() {
        return gameId;
    }

    public GameState getState() {
        return state;
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
}