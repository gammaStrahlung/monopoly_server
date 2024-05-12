package at.gammastrahlung.monopoly_server.network.dtos;

import at.gammastrahlung.monopoly_server.game.WebSocketPlayer;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClientMessage {

    /**
     * Used for matching the message to the message handler
     */
    private String messagePath;

    private String message;
    private WebSocketPlayer player;
    private String targetPlayerId;
}
