package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Player;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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
                .rentPrices(new HashMap<>() {{
                    put(0, 2);  // Rent with 0 houses
                    put("full_set", 4); // Rent with color set
                    put(1, 10); // Rent with 1 house
                    put(2, 30); // Rent with 2 houses
                    put(3, 90); // Rent with 3 houses
                    put(4, 160);
                    put("hotel", 250); //Rent with hotel
                }})
                .mortgageValue(30)
                .houseCost(50)
                .hotelCost(50)
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
                .rentPrices(new HashMap<>() {{
                    put(0, 4);  // Rent with 0 houses
                    put("full_set", 8); // Rent with color set
                    put(1,20); // Rent with 1 house
                    put(2, 60); // Rent with 2 houses
                    put(3, 180); // Rent with 3 houses
                    put(4, 320); //Rent with 4 houses
                    put("hotel", 450); //Rent with hotel
                }})
                .mortgageValue(30)
                .houseCost(50)
                .hotelCost(50)
                .build();

        gameBoard [4] = TaxField.builder()
                .fieldId(4)
                .name("Income Tax")
                .type(FieldType.INCOME_TAX)
                .toPay(200)
                .build();

        gameBoard [5] = Railroad.builder()
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
                .rentPrices(new HashMap<>() {{
                    put(0, 6);  // Rent with 0 houses
                    put("full_set", 12); // Rent with color set
                    put(1,30); // Rent with 1 house
                    put(2, 90); // Rent with 2 houses
                    put(3, 270); // Rent with 3 houses
                    put(4, 400); //Rent with 4 houses
                    put("hotel", 550); //Rent with hotel
                }})
                .mortgageValue(50)
                .houseCost(50)
                .hotelCost(50)
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
                .rentPrices(new HashMap<>() {{
                    put(0, 6);  // Rent with 0 houses
                    put("full_set", 12); // Rent with color set
                    put(1,30); // Rent with 1 house
                    put(2, 90); // Rent with 2 houses
                    put(3, 270); // Rent with 3 houses
                    put(4, 400); //Rent with 4 houses
                    put("hotel", 550); //Rent with hotel
                }})
                .mortgageValue(50)
                .houseCost(50)
                .hotelCost(50)
                .build();

        gameBoard [9] = Property.builder()
                .fieldId(9)
                .name("Connecticut Avenue")
                .type(FieldType.PROPERTY)
                .price(120)
                .owner(bank)
                .color(PropertyColor.LIGHT_BLUE)
                .rentPrices(new HashMap<>() {{
                    put(0, 8);  // Rent with 0 houses
                    put("full_set", 16); // Rent with color set
                    put(1,40); // Rent with 1 house
                    put(2, 100); // Rent with 2 houses
                    put(3, 300); // Rent with 3 houses
                    put(4, 450); //Rent with 4 houses
                    put("hotel", 600); //Rent with hotel
                }})
                .mortgageValue(60)
                .houseCost(50)
                .hotelCost(50)
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
                .rentPrices(new HashMap<>() {{
                    put(0, 10);  // Rent with 0 houses
                    put("full_set", 20); // Rent with color set
                    put(1,50); // Rent with 1 house
                    put(2, 150); // Rent with 2 houses
                    put(3, 450); // Rent with 3 houses
                    put(4, 625); //Rent with 4 houses
                    put("hotel", 750); //Rent with hotel
                }})
                .mortgageValue(70)
                .houseCost(100)
                .hotelCost(100)
                .build();

        gameBoard [12] = Utility.builder()
                .fieldId(12)
                .name("Electricity Company")
                .type(FieldType.UTILITY)
                .owner(bank)
                .price(150)
                .build();

        gameBoard [13] = Property.builder()
                .fieldId(13)
                .name("States Avenue")
                .type(FieldType.PROPERTY)
                .price(140)
                .owner(bank)
                .color(PropertyColor.PINK)
                .rentPrices(new HashMap<>() {{
                    put(0, 10);  // Rent with 0 houses
                    put("full_set", 20); // Rent with color set
                    put(1,50); // Rent with 1 house
                    put(2, 150); // Rent with 2 houses
                    put(3, 450); // Rent with 3 houses
                    put(4, 625); //Rent with 4 houses
                    put("hotel", 750); //Rent with hotel
                }})
                .mortgageValue(70)
                .houseCost(100)
                .hotelCost(100)
                .build();

        gameBoard [14] = Property.builder()
                .fieldId(14)
                .name("Virginia Avenue")
                .type(FieldType.PROPERTY)
                .price(160)
                .owner(bank)
                .color(PropertyColor.PINK)
                .rentPrices(new HashMap<>() {{
                    put(0, 12);  // Rent with 0 houses
                    put("full_set", 24); // Rent with color set
                    put(1,60); // Rent with 1 house
                    put(2, 180); // Rent with 2 houses
                    put(3, 500); // Rent with 3 houses
                    put(4, 700); //Rent with 4 houses
                    put("hotel", 900); //Rent with hotel
                }})
                .mortgageValue(80)
                .houseCost(100)
                .hotelCost(100)
                .build();

        gameBoard [15] = Railroad.builder()
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
                .rentPrices(new HashMap<>() {{
                    put(0, 14);  // Rent with 0 houses
                    put("full_set", 28); // Rent with color set
                    put(1,70); // Rent with 1 house
                    put(2, 200); // Rent with 2 houses
                    put(3, 550); // Rent with 3 houses
                    put(4, 750); //Rent with 4 houses
                    put("hotel", 950); //Rent with hotel
                }})
                .mortgageValue(90)
                .houseCost(100)
                .hotelCost(100)
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
                .rentPrices(new HashMap<>() {{
                    put(0, 14);  // Rent with 0 houses
                    put("full_set", 28); // Rent with color set
                    put(1,70); // Rent with 1 house
                    put(2, 200); // Rent with 2 houses
                    put(3, 550); // Rent with 3 houses
                    put(4, 750); //Rent with 4 houses
                    put("hotel", 950); //Rent with hotel
                }})
                .mortgageValue(90)
                .houseCost(100)
                .hotelCost(100)
                .build();

        gameBoard [19] = Property.builder()
                .fieldId(19)
                .name("New York Avenue")
                .type(FieldType.PROPERTY)
                .price(200)
                .owner(bank)
                .color(PropertyColor.ORANGE)
                .rentPrices(new HashMap<>() {{
                    put(0, 16);  // Rent with 0 houses
                    put("full_set", 32); // Rent with color set
                    put(1,80); // Rent with 1 house
                    put(2, 220); // Rent with 2 houses
                    put(3, 600); // Rent with 3 houses
                    put(4, 800); //Rent with 4 houses
                    put("hotel", 1000); //Rent with hotel
                }})
                .mortgageValue(100)
                .houseCost(100)
                .hotelCost(100)
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
                .rentPrices(new HashMap<>() {{
                    put(0, 18);  // Rent with 0 houses
                    put("full_set", 36); // Rent with color set
                    put(1,90); // Rent with 1 house
                    put(2, 250); // Rent with 2 houses
                    put(3, 700); // Rent with 3 houses
                    put(4, 875); //Rent with 4 houses
                    put("hotel", 1050); //Rent with hotel
                }})
                .mortgageValue(110)
                .houseCost(150)
                .hotelCost(150)
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
                .rentPrices(new HashMap<>() {{
                    put(0, 18);  // Rent with 0 houses
                    put("full_set", 36); // Rent with color set
                    put(1,90); // Rent with 1 house
                    put(2, 250); // Rent with 2 houses
                    put(3, 700); // Rent with 3 houses
                    put(4, 875); //Rent with 4 houses
                    put("hotel", 1050); //Rent with hotel
                }})
                .mortgageValue(110)
                .houseCost(150)
                .hotelCost(150)
                .build();

        gameBoard [24] = Property.builder()
                .fieldId(24)
                .name("Illinois Avenue")
                .type(FieldType.PROPERTY)
                .price(240)
                .owner(bank)
                .color(PropertyColor.RED)
                .rentPrices(new HashMap<>() {{
                    put(0, 20);  // Rent with 0 houses
                    put("full_set", 40); // Rent with color set
                    put(1,100); // Rent with 1 house
                    put(2, 300); // Rent with 2 houses
                    put(3, 750); // Rent with 3 houses
                    put(4, 925); //Rent with 4 houses
                    put("hotel", 1100); //Rent with hotel
                }})
                .mortgageValue(120)
                .houseCost(150)
                .hotelCost(150)
                .build();

        gameBoard [25] = Railroad.builder()
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
                .rentPrices(new HashMap<>() {{
                    put(0, 22);  // Rent with 0 houses
                    put("full_set", 44); // Rent with color set
                    put(1,110); // Rent with 1 house
                    put(2, 330); // Rent with 2 houses
                    put(3, 800); // Rent with 3 houses
                    put(4, 975); //Rent with 4 houses
                    put("hotel", 1150); //Rent with hotel
                }})
                .mortgageValue(130)
                .houseCost(150)
                .hotelCost(150)
                .build();

        gameBoard [27] = Property.builder()
                .fieldId(27)
                .name("Ventnor Avenue")
                .type(FieldType.PROPERTY)
                .price(260)
                .owner(bank)
                .color(PropertyColor.YELLOW)
                .rentPrices(new HashMap<>() {{
                    put(0, 22);  // Rent with 0 houses
                    put("full_set", 44); // Rent with color set
                    put(1,110); // Rent with 1 house
                    put(2, 330); // Rent with 2 houses
                    put(3, 800); // Rent with 3 houses
                    put(4, 975); //Rent with 4 houses
                    put("hotel", 1150); //Rent with hotel
                }})
                .mortgageValue(130)
                .houseCost(150)
                .hotelCost(150)
                .build();

        gameBoard [28] = Utility.builder()
                .fieldId(28)
                .name("Water Works")
                .type(FieldType.UTILITY)
                .owner(bank)
                .price(150)
                .build();

        gameBoard [29] = Property.builder()
                .fieldId(29)
                .name("Marvin Gardens")
                .type(FieldType.PROPERTY)
                .price(280)
                .owner(bank)
                .color(PropertyColor.YELLOW)
                .rentPrices(new HashMap<>() {{
                    put(0, 24);  // Rent with 0 houses
                    put("full_set", 48); // Rent with color set
                    put(1,120); // Rent with 1 house
                    put(2, 360); // Rent with 2 houses
                    put(3, 850); // Rent with 3 houses
                    put(4, 1025); //Rent with 4 houses
                    put("hotel", 1200); //Rent with hotel
                }})
                .mortgageValue(140)
                .houseCost(150)
                .hotelCost(150)
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
                .rentPrices(new HashMap<>() {{
                    put(0, 26);  // Rent with 0 houses
                    put("full_set", 52); // Rent with color set
                    put(1,130); // Rent with 1 house
                    put(2, 390); // Rent with 2 houses
                    put(3, 900); // Rent with 3 houses
                    put(4, 1100); //Rent with 4 houses
                    put("hotel", 1275); //Rent with hotel
                }})
                .mortgageValue(150)
                .houseCost(200)
                .hotelCost(200)
                .build();

        gameBoard [32] = Property.builder()
                .fieldId(32)
                .name("North Carolina Avenue")
                .type(FieldType.PROPERTY)
                .price(300)
                .owner(bank)
                .color(PropertyColor.GREEN)
                .rentPrices(new HashMap<>() {{
                    put(0, 26);  // Rent with 0 houses
                    put("full_set", 52); // Rent with color set
                    put(1,130); // Rent with 1 house
                    put(2, 390); // Rent with 2 houses
                    put(3, 900); // Rent with 3 houses
                    put(4, 1100); //Rent with 4 houses
                    put("hotel", 1275); //Rent with hotel
                }})
                .mortgageValue(150)
                .houseCost(200)
                .hotelCost(200)
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
                .rentPrices(new HashMap<>() {{
                    put(0, 28);  // Rent with 0 houses
                    put("full_set", 56); // Rent with color set
                    put(1,150); // Rent with 1 house
                    put(2, 450); // Rent with 2 houses
                    put(3, 1000); // Rent with 3 houses
                    put(4, 1200); //Rent with 4 houses
                    put("hotel", 1400); //Rent with hotel
                }})
                .mortgageValue(160)
                .houseCost(200)
                .hotelCost(200)
                .build();

        gameBoard[35] = Railroad.builder()
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
                .rentPrices(new HashMap<>() {{
                    put(0, 35);  // Rent with 0 houses
                    put("full_set", 70); // Rent with color set
                    put(1,175); // Rent with 1 house
                    put(2, 500); // Rent with 2 houses
                    put(3, 1100); // Rent with 3 houses
                    put(4, 1300); //Rent with 4 houses
                    put("hotel", 1500); //Rent with hotel
                }})
                .mortgageValue(175)
                .houseCost(200)
                .hotelCost(200)
                .build();

        gameBoard [38] = TaxField.builder()
                .fieldId(38)
                .name("Luxury Tax")
                .type(FieldType.LUXURY_TAX)
                .toPay(100)
                .build();

        gameBoard [39] = Property.builder()
                .fieldId(39)
                .name("Boardwalk")
                .type(FieldType.PROPERTY)
                .price(400)
                .owner(bank)
                .color(PropertyColor.DARK_BLUE)
                .rentPrices(new HashMap<>() {{
                    put(0, 50);  // Rent with 0 houses
                    put("full_set", 100); // Rent with color set
                    put(1, 200); // Rent with 1 house
                    put(2, 600); // Rent with 2 houses
                    put(3, 1400); // Rent with 3 houses
                    put(4, 1700); //Rent with 4 houses
                    put("hotel", 2000); //Rent with hotel
                }})
                .mortgageValue(200)
                .houseCost(200)
                .hotelCost(200)
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
