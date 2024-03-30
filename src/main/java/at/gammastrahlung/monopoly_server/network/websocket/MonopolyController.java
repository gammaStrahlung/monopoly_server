package at.gammastrahlung.monopoly_server.network.websocket;

import at.gammastrahlung.monopoly_server.game.Game;
import at.gammastrahlung.monopoly_server.game.Player;
import at.gammastrahlung.monopoly_server.network.dtos.ServerMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class MonopolyController {

    /**
     * Called by the client to create a new game.
     * @param player The player creating the game
     * @return ServerMessage that contains the GameId, that can be used by other clients to join the game.
     */
    @MessageMapping("/monopoly/create")
    @SendToUser(value = "/monopoly/created", broadcast = false) // Only send answer to the calling client
    public ServerMessage createGame(Player player) {

        // Create a new game
        Game g = new Game();

        // Player that creates the game should also join the game
        g.join(player);

        return new ServerMessage(ServerMessage.MessageType.SUCCESS, String.valueOf(g.getGameId()), player);
    }

    /**
     * Called by the client to join a new game or to re-join after connection loss.
     * @param player The player that wants to join the game.
     * @return ServerMessage with MessageType SUCCESS containing the GameId as the Message if joining was successful,
     * else ServerMessage has MessageType ERROR.
     */
    @MessageMapping("/monopoly/{gameId}/join")
    @SendTo(value = "/monopoly/{gameId}/joined") // Send join message to all Players
    public ServerMessage joinGame(@DestinationVariable("gameId") int gameId, Player player) {

        // Try to join the game
        Game g = Game.joinByGameId(gameId, player);

        // Joining the game was unsuccessful
        if (g == null)
            return new ServerMessage(ServerMessage.MessageType.ERROR, "", player);

        return new ServerMessage(ServerMessage.MessageType.SUCCESS, String.valueOf(g.getGameId()), player);
    }
}
