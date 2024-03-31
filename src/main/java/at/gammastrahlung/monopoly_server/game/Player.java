package at.gammastrahlung.monopoly_server.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Player {
    /**
     * The unique ID of the player, this can be used by the player to allow for re-joining the ga
     */
    private UUID ID;

    /**
     * The name of the player (this is shown to other players)
     */
    private String name;
}
