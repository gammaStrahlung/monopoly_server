package at.gammastrahlung.monopoly_server.network.dtos;

import at.gammastrahlung.monopoly_server.game.Player;

public class ServerMessage {

    private MessageType type;
    private String message;
    private Player player;

    public ServerMessage() {}

    public ServerMessage(MessageType type, String message, Player player) {
        this.type = type;
        this.message = message;
        this.player = player;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



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
