package at.gammastrahlung.monopoly_server.network.dtos;

import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import com.google.gson.annotations.Expose;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ServerMessage {

    /**
     * Used for matching the message to the message handler
     */
    @Expose
    private String messagePath;

    @Expose
    private MessageType type;

    @Expose
    private String message;

    @Expose
    private WebSocketPlayer player;

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
