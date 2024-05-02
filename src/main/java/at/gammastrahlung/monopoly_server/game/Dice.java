package at.gammastrahlung.monopoly_server.game;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;

public class Dice {
    @Getter
    @Setter
    @Expose
    protected int value1;
    @Getter
    @Setter
    @Expose
    protected int value2;
    public Dice(){
        this.value1 = 1;
        this.value2 = 5;
    }

    // Simulates two dices where each value is between 1 and 6
    public void initializeDice(){
        this.value1 = new SecureRandom().nextInt(6) + 1;
        this.value2 = new SecureRandom().nextInt(6) + 1;
    }
}
