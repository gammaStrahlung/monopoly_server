package at.gammastrahlung.monopoly_server.game;

import at.gammastrahlung.monopoly_server.game.gameboard.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FieldTest {

    private Field f;

    @BeforeEach
    public void initialize() {
        f = new Field();
    }

    @Test
    void boardName() {
        f.setBoardName("boardName");
        assertEquals("boardName", f.getBoardName());
    }
}
