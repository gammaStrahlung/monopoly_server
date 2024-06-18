package at.gammastrahlung.monopoly_server.game;

import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter

@AllArgsConstructor
public class Bid {
    @Expose
    private UUID playerId; // The ID of the player who made the bid
    @Expose
    private int amount; // The amount of the bid
    @Expose
    private int fieldIndex; // The field the bid is made on
    @Expose
    private Player playerForBid; // The player who made the bid


}