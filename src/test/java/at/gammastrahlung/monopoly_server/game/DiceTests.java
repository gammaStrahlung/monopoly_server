package at.gammastrahlung.monopoly_server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DiceTests {
    private Dice dice;

    @BeforeEach
    public void setUp() {
        dice = new Dice();
    }

    @Test
    public void constructorTest(){
        assertEquals(1, dice.getValue1());
        assertEquals(5, dice.getValue2());
    }

    @Test
    public void initializeDiceTest(){
        dice.initializeDice();

        assertTrue(dice.getValue1() >= 1 && dice.getValue1() <= 6);
        assertTrue(dice.getValue2() >= 1 && dice.getValue2() <= 6);
    }

    @Test
    public void setDiceTest(){
        dice.setValue1(1);
        dice.setValue2(3);

        assertEquals(1, dice.getValue1());
        assertEquals(3,dice.getValue2());
    }
}
