package at.gammastrahlung.monopoly_server.game;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Bid {
    private UUID playerId; // The ID of the player who made the bid
    private int amount; // The amount of the bid
    private int fieldIndex; // The field the bid is made on


}