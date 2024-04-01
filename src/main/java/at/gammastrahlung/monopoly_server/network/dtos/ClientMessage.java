package at.gammastrahlung.monopoly_server.network.dtos;

import at.gammastrahlung.monopoly_server.game.Player;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClientMessage<T> {

    /**
     * Used for matching the message to the message handler
     */
    private String messagePath;

    private T message;
    private Player player;
}
