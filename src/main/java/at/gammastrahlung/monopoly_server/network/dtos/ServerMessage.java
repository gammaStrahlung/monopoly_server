package at.gammastrahlung.monopoly_server.network.dtos;

import at.gammastrahlung.monopoly_server.game.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServerMessage {

    private MessageType type;

    private String message;
    private Player player;

    public enum MessageType {
        /**
         * The operation from the client was successful
         */
        SUCCESS,

        /**
         * The operation from the client was not successful
         */
        ERROR,

        /**
         * Informational message for the client
         */
        INFO
    }
}
