package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Player;
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

        gameBoard[2] = Field.builder()
                .fieldId(2)
                .name("Community Chest")
                .type(FieldType.COMMUNITY_CHEST)
                .build();

        gameBoard [3] = Property.builder()
                .fieldId(3)
                .name("Baltic Avenue")
                .type(FieldType.PROPERTY)
                .price(60)
                .owner(bank)
                .color(PropertyColor.BROWN)
                .build();

        gameBoard [4] = Field.builder()
                .fieldId(4)
                .name("Income Tax")
                .type(FieldType.INCOME_TAX)
                .build();

        gameBoard [5] = Utility.builder()
                .fieldId(5)
                .name("Reading Railroad")
                .type(FieldType.RAILROAD)
                .owner(bank)
                .build();

        gameBoard [6] = Property.builder()
                .fieldId(6)
                .name("Oriental Avenue")
                .type(FieldType.PROPERTY)
                .price(100)
                .owner(bank)
                .color(PropertyColor.LIGHT_BLUE)
                .build();

        gameBoard [7] = Field.builder()
                .fieldId(7)
                .name("Chance")
                .type(FieldType.CHANCE)
                .build();

        gameBoard [8] = Property.builder()
                .fieldId(8)
                .name("Vermont Avenue")
                .type(FieldType.PROPERTY)
                .price(100)
                .owner(bank)
                .color(PropertyColor.LIGHT_BLUE)
                .build();

        gameBoard [9] = Property.builder()
                .fieldId(9)
                .name("Connecticut Avenue")
                .type(FieldType.PROPERTY)
                .price(120)
                .owner(bank)
                .color(PropertyColor.LIGHT_BLUE)
                .build();

        gameBoard [10] = Field.builder()
                .fieldId(10)
                .name("Jail")
                .type(FieldType.JAIL)
                .build();

        gameBoard [11] = Property.builder()
                .fieldId(11)
                .name("St Charles Place")
                .type(FieldType.PROPERTY)
                .price(140)
                .owner(bank)
                .color(PropertyColor.PINK)
                .build();

        gameBoard [12] = Utility.builder()
                .fieldId(12)
                .name("Electricity Company")
                .type(FieldType.UTILITY)
                .owner(bank)
                .toPay(150)
                .build();

        gameBoard [13] = Property.builder()
                .fieldId(13)
                .name("States Avenue")
                .type(FieldType.PROPERTY)
                .price(140)
                .owner(bank)
                .color(PropertyColor.PINK)
                .build();

        gameBoard [14] = Property.builder()
                .fieldId(14)
                .name("Virginia Avenue")
                .type(FieldType.PROPERTY)
                .price(160)
                .owner(bank)
                .color(PropertyColor.PINK)
                .build();

        gameBoard [15] = Utility.builder()
                .fieldId(15)
                .name("Pennsylvania Railroad")
                .type(FieldType.RAILROAD)
                .owner(bank)
                .build();

        gameBoard [16] = Property.builder()
                .fieldId(16)
                .name("St. James Place")
                .type(FieldType.PROPERTY)
                .price(180)
                .owner(bank)
                .color(PropertyColor.ORANGE)
                .build();

        gameBoard [17] = Field.builder()
                .fieldId(17)
                .name("Community Chest")
                .type(FieldType.COMMUNITY_CHEST)
                .build();

        gameBoard [18] = Property.builder()
                .fieldId(18)
                .name("Tennessee Avenue")
                .type(FieldType.PROPERTY)
                .price(180)
                .owner(bank)
                .color(PropertyColor.ORANGE)
                .build();

        gameBoard [19] = Property.builder()
                .fieldId(19)
                .name("New York Avenue")
                .type(FieldType.PROPERTY)
                .price(200)
                .owner(bank)
                .color(PropertyColor.ORANGE)
                .build();

        gameBoard [20] = Field.builder()
                .fieldId(20)
                .name("Free Parking")
                .type(FieldType.FREE_PARKING)
                .build();

        gameBoard [21] = Property.builder()
                .fieldId(21)
                .name("Kentucky Avenue")
                .type(FieldType.PROPERTY)
                .price(220)
                .owner(bank)
                .color(PropertyColor.RED)
                .build();

        gameBoard [22] = Field.builder()
                .fieldId(22)
                .name("Chance")
                .type(FieldType.CHANCE)
                .build();

        gameBoard [23] = Property.builder()
                .fieldId(23)
                .name("Indiana Avenue")
                .type(FieldType.PROPERTY)
                .price(220)
                .owner(bank)
                .color(PropertyColor.RED)
                .build();

        gameBoard [24] = Property.builder()
                .fieldId(24)
                .name("Illinois Avenue")
                .type(FieldType.PROPERTY)
                .price(240)
                .owner(bank)
                .color(PropertyColor.RED)
                .build();

        gameBoard [25] = Utility.builder()
                .fieldId(25)
                .name("B. & O. Railroad")
                .type(FieldType.RAILROAD)
                .owner(bank)
                .build();

        gameBoard [26] = Property.builder()
                .fieldId(26)
                .name("Atlantic Avenue")
                .type(FieldType.PROPERTY)
                .price(260)
                .owner(bank)
                .color(PropertyColor.YELLOW)
                .build();

        gameBoard [27] = Property.builder()
                .fieldId(27)
                .name("Ventnor Avenue")
                .type(FieldType.PROPERTY)
                .price(260)
                .owner(bank)
                .color(PropertyColor.YELLOW)
                .build();

        gameBoard [28] = Utility.builder()
                .fieldId(28)
                .name("Water Works")
                .type(FieldType.UTILITY)
                .owner(bank)
                .toPay(150)
                .build();

        gameBoard [29] = Property.builder()
                .fieldId(29)
                .name("Marvin Gardens")
                .type(FieldType.PROPERTY)
                .price(280)
                .owner(bank)
                .color(PropertyColor.YELLOW)
                .build();

        gameBoard [30] = Field.builder()
                .fieldId(30)
                .name("Jail")
                .type(FieldType.JAIL)
                .build();

        gameBoard [31] = Property.builder()
                .fieldId(31)
                .name("Pacific Avenue")
                .type(FieldType.PROPERTY)
                .price(300)
                .owner(bank)
                .color(PropertyColor.GREEN)
                .build();

        gameBoard [32] = Property.builder()
                .fieldId(32)
                .name("North Carolina Avenue")
                .type(FieldType.PROPERTY)
                .price(300)
                .owner(bank)
                .color(PropertyColor.GREEN)
                .build();

        gameBoard [33] = Field.builder()
                .fieldId(33)
                .name("Community Chest")
                .type(FieldType.COMMUNITY_CHEST)
                .build();

        gameBoard [34] = Property.builder()
                .fieldId(34)
                .name("Pennsylvania Avenue")
                .type(FieldType.PROPERTY)
                .price(320)
                .owner(bank)
                .color(PropertyColor.GREEN)
                .build();

        gameBoard[35] = Utility.builder()
                .fieldId(35)
                .name("Short Line")
                .type(FieldType.RAILROAD)
                .owner(bank)
                .build();

        gameBoard [36] = Field.builder()
                .fieldId(36)
                .name("Chance")
                .build();

        gameBoard [37] = Property.builder()
                .fieldId(37)
                .name("Park Place")
                .type(FieldType.PROPERTY)
                .price(350)
                .owner(bank)
                .color(PropertyColor.DARK_BLUE)
                .build();

        gameBoard [38] = Field.builder()
                .fieldId(38)
                .name("Luxury Tax")
                .type(FieldType.LUXURY_TAX)
                .build();

        gameBoard [39] = Property.builder()
                .fieldId(39)
                .name("Boardwalk")
                .type(FieldType.PROPERTY)
                .price(400)
                .owner(bank)
                .color(PropertyColor.DARK_BLUE)
                .build();
    }
    public void cleanUpBoard() {
        // Reset all properties and fields to their initial state
        for (Field field : gameBoard) {
            if (field instanceof Property) {
                Property property = (Property) field;
                property.setOwner(null);

            }
        }
    }
}
