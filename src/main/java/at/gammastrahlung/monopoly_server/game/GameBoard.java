package at.gammastrahlung.monopoly_server.game;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameBoard {
    private Player bank = new Player();
    private Field[] gameBoard;

    @Builder
    public GameBoard(int size) {
        gameBoard = new Field[size];
    }

    public void initializeGameBoard() {
        gameBoard[0] = Field.builder()
                .fieldId(0)
                .name("Go")
                .type(FieldType.GO)
                .build();

        gameBoard[1] = Property.builder()
                .fieldId(1)
                .name("Mediterranean Avenue")
                .type(FieldType.PROPERTY)
                .price(60)
                .owner(bank)
                .color(PropertyColor.BROWN)
                .build();

        Property.builder()
                .fieldId(3)
                .name("Baltic Avenue")
                .type(FieldType.PROPERTY)
                .price(60)
                .color(PropertyColor.BROWN)
                .build();

        Property.builder()
                .fieldId(6)
                .name("Oriental Avenue")
                .type(FieldType.PROPERTY)
                .price(100)
                .color(PropertyColor.LIGHT_BLUE)
                .build();

        Property.builder()
                .fieldId(8)
                .name("Vermont Avenue")
                .type(FieldType.PROPERTY)
                .price(100)
                .color(PropertyColor.LIGHT_BLUE)
                .build();

        Property.builder()
                .fieldId(9)
                .name("Connecticut Avenue")
                .type(FieldType.PROPERTY)
                .price(120)
                .color(PropertyColor.LIGHT_BLUE)
                .build();

        Property.builder()
                .fieldId(11)
                .name("St Charles Place")
                .type(FieldType.PROPERTY)
                .price(140)
                .color(PropertyColor.PINK)
                .build();

        Property.builder()
                .fieldId(13)
                .name("States Avenue")
                .type(FieldType.PROPERTY)
                .price(140)
                .color(PropertyColor.PINK)
                .build();

        Property.builder()
                .fieldId(14)
                .name("Virginia Avenue")
                .type(FieldType.PROPERTY)
                .price(160)
                .color(PropertyColor.PINK)
                .build();

        Property.builder()
                .fieldId(16)
                .name("St. James Place")
                .type(FieldType.PROPERTY)
                .price(180)
                .color(PropertyColor.ORANGE)
                .build();

        Property.builder()
                .fieldId(18)
                .name("Tennessee Avenue")
                .type(FieldType.PROPERTY)
                .price(180)
                .color(PropertyColor.ORANGE)
                .build();

        Property.builder()
                .fieldId(19)
                .name("New York Avenue")
                .type(FieldType.PROPERTY)
                .price(200)
                .color(PropertyColor.ORANGE)
                .build();

        Property.builder()
                .fieldId(21)
                .name("Kentucky Avenue")
                .type(FieldType.PROPERTY)
                .price(220)
                .color(PropertyColor.RED)
                .build();

        Property.builder()
                .fieldId(23)
                .name("Indiana Avenue")
                .type(FieldType.PROPERTY)
                .price(220)
                .color(PropertyColor.RED)
                .build();

        Property.builder()
                .fieldId(24)
                .name("Illinois Avenue")
                .type(FieldType.PROPERTY)
                .price(240)
                .color(PropertyColor.RED)
                .build();

        Property.builder()
                .fieldId(26)
                .name("Atlantic Avenue")
                .type(FieldType.PROPERTY)
                .price(260)
                .color(PropertyColor.YELLOW)
                .build();

        Property.builder()
                .fieldId(27)
                .name("Ventnor Avenue")
                .type(FieldType.PROPERTY)
                .price(260)
                .color(PropertyColor.YELLOW)
                .build();

        Property.builder()
                .fieldId(29)
                .name("Marvin Gardens")
                .type(FieldType.PROPERTY)
                .price(280)
                .color(PropertyColor.YELLOW)
                .build();

        Property.builder()
                .fieldId(31)
                .name("Pacific Avenue")
                .type(FieldType.PROPERTY)
                .price(300)
                .color(PropertyColor.GREEN)
                .build();

        Property.builder()
                .fieldId(32)
                .name("North Carolina Avenue")
                .type(FieldType.PROPERTY)
                .price(300)
                .color(PropertyColor.GREEN)
                .build();

        Property.builder()
                .fieldId(34)
                .name("Pennsylvania Avenue")
                .type(FieldType.PROPERTY)
                .price(320)
                .color(PropertyColor.GREEN)
                .build();

        Property.builder()
                .fieldId(37)
                .name("Park Place")
                .type(FieldType.PROPERTY)
                .price(350)
                .color(PropertyColor.DARK_BLUE)
                .build();

        Property.builder()
                .fieldId(39)
                .name("Boardwalk")
                .type(FieldType.PROPERTY)
                .price(400)
                .color(PropertyColor.DARK_BLUE)
                .build();







    }
}
