package at.gammastrahlung.monopoly_server.game.gameboard;

import at.gammastrahlung.monopoly_server.game.Player;
import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class GameBoard {
    @Expose
    private Player bank = new Player();
    @Expose
    private Field[] gameBoard;

    @Expose
    private final int GAME_BOARD_SIZE = 40;
    @Expose
    private final String FULL_SET = "full_set";
    @Expose
    private final String HOTEL = "hotel";

    private ArrayList<EventCard> chanceDeck;
    private ArrayList<EventCard> communityChestDeck;

    @Builder
    public GameBoard() {
        gameBoard = new Field[GAME_BOARD_SIZE];
        // Needed for Player.equals to work
        bank.setId(UUID.randomUUID());
    }

    public void initializeGameBoard() {
        gameBoard[0] = Field.builder()
                .fieldId(0)
                .name("Go")
                .type(FieldType.GO)
                .build();

        Map<Object, Integer> rentPrices1 = new HashMap<>();
        rentPrices1.put(0, 2);
        rentPrices1.put(FULL_SET, 4);
        rentPrices1.put(1, 10);
        rentPrices1.put(2, 30);
        rentPrices1.put(3, 90);
        rentPrices1.put(4, 160);
        rentPrices1.put(HOTEL, 250);

        gameBoard[1] = Property.builder()
                .fieldId(1)
                .name("Mediterranean Avenue")
                .type(FieldType.PROPERTY)
                .price(60)
                .owner(bank)
                .color(PropertyColor.BROWN)
                .rentPrices(rentPrices1)
                .mortgageValue(30)
                .houseCost(50)
                .hotelCost(50)
                .build();

        gameBoard[2] = Field.builder()
                .fieldId(2)
                .name("Community Chest")
                .type(FieldType.COMMUNITY_CHEST)
                .build();

        Map<Object, Integer> rentPrices2 = new HashMap<>();
        rentPrices2.put(0, 4);  // Rent with 0 houses
        rentPrices2.put(FULL_SET, 8); // Rent with color set
        rentPrices2.put(1, 20); // Rent with 1 house
        rentPrices2.put(2, 60); // Rent with 2 houses
        rentPrices2.put(3, 180); // Rent with 3 houses
        rentPrices2.put(4, 320); //Rent with 4 houses
        rentPrices2.put(HOTEL, 450); //Rent with hotel

        gameBoard[3] = Property.builder()
                .fieldId(3)
                .name("Baltic Avenue")
                .type(FieldType.PROPERTY)
                .price(60)
                .owner(bank)
                .color(PropertyColor.BROWN)
                .rentPrices(rentPrices2)
                .mortgageValue(30)
                .houseCost(50)
                .hotelCost(50)
                .build();

        gameBoard[4] = TaxField.builder()
                .fieldId(4)
                .name("Income Tax")
                .type(FieldType.INCOME_TAX)
                .toPay(200)
                .build();

        gameBoard[5] = Railroad.builder()
                .fieldId(5)
                .name("Reading Railroad")
                .type(FieldType.RAILROAD)
                .owner(bank)
                .build();

        Map<Object, Integer> rentPrices3 = new HashMap<>();
        rentPrices3.put(0, 6);  // Rent with 0 houses
        rentPrices3.put(FULL_SET, 12); // Rent with color set
        rentPrices3.put(1, 30); // Rent with 1 house
        rentPrices3.put(2, 90); // Rent with 2 houses
        rentPrices3.put(3, 270); // Rent with 3 houses
        rentPrices3.put(4, 400); //Rent with 4 houses
        rentPrices3.put(HOTEL, 550); //Rent with hotel

        gameBoard[6] = Property.builder()
                .fieldId(6)
                .name("Oriental Avenue")
                .type(FieldType.PROPERTY)
                .price(100)
                .owner(bank)
                .color(PropertyColor.LIGHT_BLUE)
                .rentPrices(rentPrices3)
                .mortgageValue(50)
                .houseCost(50)
                .hotelCost(50)
                .build();

        gameBoard[7] = Field.builder()
                .fieldId(7)
                .name("Chance")
                .type(FieldType.CHANCE)
                .build();

        gameBoard[8] = Property.builder()
                .fieldId(8)
                .name("Vermont Avenue")
                .type(FieldType.PROPERTY)
                .price(100)
                .owner(bank)
                .color(PropertyColor.LIGHT_BLUE)
                .rentPrices(rentPrices3)
                .mortgageValue(50)
                .houseCost(50)
                .hotelCost(50)
                .build();

        Map<Object, Integer> rentPrices4 = new HashMap<>();
        rentPrices4.put(0, 8);  // Rent with 0 houses
        rentPrices4.put(FULL_SET, 16); // Rent with color set
        rentPrices4.put(1, 40); // Rent with 1 house
        rentPrices4.put(2, 100); // Rent with 2 houses
        rentPrices4.put(3, 300); // Rent with 3 houses
        rentPrices4.put(4, 450); //Rent with 4 houses
        rentPrices4.put(HOTEL, 600); //Rent with hotel

        gameBoard[9] = Property.builder()
                .fieldId(9)
                .name("Connecticut Avenue")
                .type(FieldType.PROPERTY)
                .price(120)
                .owner(bank)
                .color(PropertyColor.LIGHT_BLUE)
                .rentPrices(rentPrices4)
                .mortgageValue(60)
                .houseCost(50)
                .hotelCost(50)
                .build();

        gameBoard[10] = Field.builder()
                .fieldId(10)
                .name("Jail")
                .type(FieldType.JAIL)
                .build();

        Map<Object, Integer> rentPrices5 = new HashMap<>();
        rentPrices5.put(0, 10);  // Rent with 0 houses
        rentPrices5.put(FULL_SET, 20); // Rent with color set
        rentPrices5.put(1, 50); // Rent with 1 house
        rentPrices5.put(2, 150); // Rent with 2 houses
        rentPrices5.put(3, 450); // Rent with 3 houses
        rentPrices5.put(4, 625); //Rent with 4 houses
        rentPrices5.put(HOTEL, 750); //Rent with hotel

        gameBoard[11] = Property.builder()
                .fieldId(11)
                .name("St Charles Place")
                .type(FieldType.PROPERTY)
                .price(140)
                .owner(bank)
                .color(PropertyColor.PINK)
                .rentPrices(rentPrices5)
                .mortgageValue(70)
                .houseCost(100)
                .hotelCost(100)
                .build();

        gameBoard[12] = Utility.builder()
                .fieldId(12)
                .name("Electricity Company")
                .type(FieldType.UTILITY)
                .owner(bank)
                .price(150)
                .mortgage(75)
                .build();

        gameBoard[13] = Property.builder()
                .fieldId(13)
                .name("States Avenue")
                .type(FieldType.PROPERTY)
                .price(140)
                .owner(bank)
                .color(PropertyColor.PINK)
                .rentPrices(rentPrices5)
                .mortgageValue(70)
                .houseCost(100)
                .hotelCost(100)
                .build();

        Map<Object, Integer> rentPrices6 = new HashMap<>();
        rentPrices6.put(0, 12);  // Rent with 0 houses
        rentPrices6.put(FULL_SET, 24); // Rent with color set
        rentPrices6.put(1, 60); // Rent with 1 house
        rentPrices6.put(2, 180); // Rent with 2 houses
        rentPrices6.put(3, 500); // Rent with 3 houses
        rentPrices6.put(4, 700); //Rent with 4 houses
        rentPrices6.put(HOTEL, 900); //Rent with hotel

        gameBoard[14] = Property.builder()
                .fieldId(14)
                .name("Virginia Avenue")
                .type(FieldType.PROPERTY)
                .price(160)
                .owner(bank)
                .color(PropertyColor.PINK)
                .rentPrices(rentPrices6)
                .mortgageValue(80)
                .houseCost(100)
                .hotelCost(100)
                .build();

        gameBoard[15] = Railroad.builder()
                .fieldId(15)
                .name("Pennsylvania Railroad")
                .type(FieldType.RAILROAD)
                .owner(bank)
                .build();

        Map<Object, Integer> rentPrices7 = new HashMap<>();
        rentPrices7.put(0, 14);  // Rent with 0 houses
        rentPrices7.put(FULL_SET, 28); // Rent with color set
        rentPrices7.put(1, 70); // Rent with 1 house
        rentPrices7.put(2, 200); // Rent with 2 houses
        rentPrices7.put(3, 550); // Rent with 3 houses
        rentPrices7.put(4, 750); //Rent with 4 houses
        rentPrices7.put(HOTEL, 950); //Rent with hotel

        gameBoard[16] = Property.builder()
                .fieldId(16)
                .name("St. James Place")
                .type(FieldType.PROPERTY)
                .price(180)
                .owner(bank)
                .color(PropertyColor.ORANGE)
                .rentPrices(rentPrices7)
                .mortgageValue(90)
                .houseCost(100)
                .hotelCost(100)
                .build();

        gameBoard[17] = Field.builder()
                .fieldId(17)
                .name("Community Chest")
                .type(FieldType.COMMUNITY_CHEST)
                .build();

        gameBoard[18] = Property.builder()
                .fieldId(18)
                .name("Tennessee Avenue")
                .type(FieldType.PROPERTY)
                .price(180)
                .owner(bank)
                .color(PropertyColor.ORANGE)
                .rentPrices(rentPrices7)
                .mortgageValue(90)
                .houseCost(100)
                .hotelCost(100)
                .build();


        Map<Object, Integer> rentPrices8 = new HashMap<>();
        rentPrices8.put(0, 16);  // Rent with 0 houses
        rentPrices8.put(FULL_SET, 32); // Rent with color set
        rentPrices8.put(1, 80); // Rent with 1 house
        rentPrices8.put(2, 220); // Rent with 2 houses
        rentPrices8.put(3, 600); // Rent with 3 houses
        rentPrices8.put(4, 800); //Rent with 4 houses
        rentPrices8.put(HOTEL, 1000); //Rent with hotel

        gameBoard[19] = Property.builder()
                .fieldId(19)
                .name("New York Avenue")
                .type(FieldType.PROPERTY)
                .price(200)
                .owner(bank)
                .color(PropertyColor.ORANGE)
                .rentPrices(rentPrices8)
                .mortgageValue(100)
                .houseCost(100)
                .hotelCost(100)
                .build();

        gameBoard[20] = Field.builder()
                .fieldId(20)
                .name("Free Parking")
                .type(FieldType.FREE_PARKING)
                .build();

        Map<Object, Integer> rentPrices9 = new HashMap<>();
        rentPrices9.put(0, 18);  // Rent with 0 houses
        rentPrices9.put(FULL_SET, 36); // Rent with color set
        rentPrices9.put(1, 90); // Rent with 1 house
        rentPrices9.put(2, 250); // Rent with 2 houses
        rentPrices9.put(3, 700); // Rent with 3 houses
        rentPrices9.put(4, 875); //Rent with 4 houses
        rentPrices9.put(HOTEL, 1050); //Rent with hotel

        gameBoard[21] = Property.builder()
                .fieldId(21)
                .name("Kentucky Avenue")
                .type(FieldType.PROPERTY)
                .price(220)
                .owner(bank)
                .color(PropertyColor.RED)
                .rentPrices(rentPrices9)
                .mortgageValue(110)
                .houseCost(150)
                .hotelCost(150)
                .build();

        gameBoard[22] = Field.builder()
                .fieldId(22)
                .name("Chance")
                .type(FieldType.CHANCE)
                .build();

        gameBoard[23] = Property.builder()
                .fieldId(23)
                .name("Indiana Avenue")
                .type(FieldType.PROPERTY)
                .price(220)
                .owner(bank)
                .color(PropertyColor.RED)
                .rentPrices(rentPrices9)
                .mortgageValue(110)
                .houseCost(150)
                .hotelCost(150)
                .build();

        Map<Object, Integer> rentPrices10 = new HashMap<>();
        rentPrices10.put(0, 20);  // Rent with 0 houses
        rentPrices10.put(FULL_SET, 40); // Rent with color set
        rentPrices10.put(1, 100); // Rent with 1 house
        rentPrices10.put(2, 300); // Rent with 2 houses
        rentPrices10.put(3, 750); // Rent with 3 houses
        rentPrices10.put(4, 925); //Rent with 4 houses
        rentPrices10.put(HOTEL, 1100); //Rent with hotel

        gameBoard[24] = Property.builder()
                .fieldId(24)
                .name("Illinois Avenue")
                .type(FieldType.PROPERTY)
                .price(240)
                .owner(bank)
                .color(PropertyColor.RED)
                .rentPrices(rentPrices10)
                .mortgageValue(120)
                .houseCost(150)
                .hotelCost(150)
                .build();

        gameBoard[25] = Railroad.builder()
                .fieldId(25)
                .name("B. & O. Railroad")
                .type(FieldType.RAILROAD)
                .owner(bank)
                .build();

        Map<Object, Integer> rentPrices11 = new HashMap<>();
        rentPrices11.put(0, 22);  // Rent with 0 houses
        rentPrices11.put(FULL_SET, 44); // Rent with color set
        rentPrices11.put(1, 110); // Rent with 1 house
        rentPrices11.put(2, 330); // Rent with 2 houses
        rentPrices11.put(3, 800); // Rent with 3 houses
        rentPrices11.put(4, 975); //Rent with 4 houses
        rentPrices11.put(HOTEL, 1150); //Rent with hotel

        gameBoard[26] = Property.builder()
                .fieldId(26)
                .name("Atlantic Avenue")
                .type(FieldType.PROPERTY)
                .price(260)
                .owner(bank)
                .color(PropertyColor.YELLOW)
                .rentPrices(rentPrices11)
                .mortgageValue(130)
                .houseCost(150)
                .hotelCost(150)
                .build();

        gameBoard[27] = Property.builder()
                .fieldId(27)
                .name("Ventnor Avenue")
                .type(FieldType.PROPERTY)
                .price(260)
                .owner(bank)
                .color(PropertyColor.YELLOW)
                .rentPrices(rentPrices11)
                .mortgageValue(130)
                .houseCost(150)
                .hotelCost(150)
                .build();

        gameBoard[28] = Utility.builder()
                .fieldId(28)
                .name("Water Works")
                .type(FieldType.UTILITY)
                .owner(bank)
                .price(150)
                .mortgage(75)
                .build();

        Map<Object, Integer> rentPrices12 = new HashMap<>();
        rentPrices12.put(0, 24);  // Rent with 0 houses
        rentPrices12.put(FULL_SET, 48); // Rent with color set
        rentPrices12.put(1, 120); // Rent with 1 house
        rentPrices12.put(2, 360); // Rent with 2 houses
        rentPrices12.put(3, 850); // Rent with 3 houses
        rentPrices12.put(4, 1025); //Rent with 4 houses
        rentPrices12.put(HOTEL, 1200); //Rent with hotel

        gameBoard[29] = Property.builder()
                .fieldId(29)
                .name("Marvin Gardens")
                .type(FieldType.PROPERTY)
                .price(280)
                .owner(bank)
                .color(PropertyColor.YELLOW)
                .rentPrices(rentPrices12)
                .mortgageValue(140)
                .houseCost(150)
                .hotelCost(150)
                .build();

        gameBoard[30] = Field.builder()
                .fieldId(30)
                .name("Go to Jail")
                .type(FieldType.GO_TO_JAIL)
                .build();

        Map<Object, Integer> rentPrices13 = new HashMap<>();
        rentPrices13.put(0, 26);  // Rent with 0 houses
        rentPrices13.put(FULL_SET, 52); // Rent with color set
        rentPrices13.put(1, 130); // Rent with 1 house
        rentPrices13.put(2, 390); // Rent with 2 houses
        rentPrices13.put(3, 900); // Rent with 3 houses
        rentPrices13.put(4, 1100); //Rent with 4 houses
        rentPrices13.put(HOTEL, 1275); //Rent with hotel

        gameBoard[31] = Property.builder()
                .fieldId(31)
                .name("Pacific Avenue")
                .type(FieldType.PROPERTY)
                .price(300)
                .owner(bank)
                .color(PropertyColor.GREEN)
                .rentPrices(rentPrices13)
                .mortgageValue(150)
                .houseCost(200)
                .hotelCost(200)
                .build();

        gameBoard[32] = Property.builder()
                .fieldId(32)
                .name("North Carolina Avenue")
                .type(FieldType.PROPERTY)
                .price(300)
                .owner(bank)
                .color(PropertyColor.GREEN)
                .rentPrices(rentPrices13)
                .mortgageValue(150)
                .houseCost(200)
                .hotelCost(200)
                .build();

        gameBoard[33] = Field.builder()
                .fieldId(33)
                .name("Community Chest")
                .type(FieldType.COMMUNITY_CHEST)
                .build();

        Map<Object, Integer> rentPrices14 = new HashMap<>();
        rentPrices14.put(0, 28);  // Rent with 0 houses
        rentPrices14.put(FULL_SET, 56); // Rent with color set
        rentPrices14.put(1, 150); // Rent with 1 house
        rentPrices14.put(2, 450); // Rent with 2 houses
        rentPrices14.put(3, 1000); // Rent with 3 houses
        rentPrices14.put(4, 1200); //Rent with 4 houses
        rentPrices14.put(HOTEL, 1400); //Rent with hotel

        gameBoard[34] = Property.builder()
                .fieldId(34)
                .name("Pennsylvania Avenue")
                .type(FieldType.PROPERTY)
                .price(320)
                .owner(bank)
                .color(PropertyColor.GREEN)
                .rentPrices(rentPrices14)
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

        gameBoard[36] = Field.builder()
                .fieldId(36)
                .name("Chance")
                .build();

        Map<Object, Integer> rentPrices15 = new HashMap<>();
        rentPrices15.put(0, 35);  // Rent with 0 houses
        rentPrices15.put(FULL_SET, 70); // Rent with color set
        rentPrices15.put(1, 175); // Rent with 1 house
        rentPrices15.put(2, 500); // Rent with 2 houses
        rentPrices15.put(3, 1100); // Rent with 3 houses
        rentPrices15.put(4, 1300); //Rent with 4 houses
        rentPrices15.put(HOTEL, 1500); //Rent with hotel

        gameBoard[37] = Property.builder()
                .fieldId(37)
                .name("Park Place")
                .type(FieldType.PROPERTY)
                .price(350)
                .owner(bank)
                .color(PropertyColor.DARK_BLUE)
                .rentPrices(rentPrices15)
                .mortgageValue(175)
                .houseCost(200)
                .hotelCost(200)
                .build();

        gameBoard[38] = TaxField.builder()
                .fieldId(38)
                .name("Luxury Tax")
                .type(FieldType.LUXURY_TAX)
                .toPay(100)
                .build();

        Map<Object, Integer> rentPrices16 = new HashMap<>();
        rentPrices16.put(0, 50);  // Rent with 0 houses
        rentPrices16.put(FULL_SET, 100); // Rent with color set
        rentPrices16.put(1, 200); // Rent with 1 house
        rentPrices16.put(2, 600); // Rent with 2 houses
        rentPrices16.put(3, 1400); // Rent with 3 houses
        rentPrices16.put(4, 1700); // Rent with 4 houses
        rentPrices16.put(HOTEL, 2000); // Rent with hotel

        gameBoard[39] = Property.builder()
                .fieldId(39)
                .name("Boardwalk")
                .type(FieldType.PROPERTY)
                .price(400)
                .owner(bank)
                .color(PropertyColor.DARK_BLUE)
                .rentPrices(rentPrices16)
                .mortgageValue(200)
                .houseCost(200)
                .hotelCost(200)
                .build();
    }

    public void cleanUpBoard() {
        // Reset all properties and fields to their initial state
        for (Field field : gameBoard) {
            if (field instanceof Property property) {
                property.setOwner(null);

            }
        }
    }

    public void initializeCommunityChestDeck() {
        communityChestDeck = new ArrayList<>();

        communityChestDeck.add(EventCard.builder()
                .description("Go to Jail")
                .cardType(CardType.GO_TO_JAIL)
                .moveToField(30)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Birthday")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(20)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Beauty Contest")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(10)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Inheritance")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(100)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Hospital Fee")
                .cardType(CardType.PAY_MONEY_CARD)
                .payOrGetMoney(50)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Doctors Fee")
                .cardType(CardType.PAY_MONEY_CARD)
                .payOrGetMoney(50)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Income Tax Refund")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(20)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("School Fee")
                .cardType(CardType.PAY_MONEY_CARD)
                .payOrGetMoney(50)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Advance to GO")
                .cardType(CardType.MOVE_TO_FIELD)
                .moveToField(0)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Life Insurance Matures")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(100)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Bank Error in your Favor")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(200)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Sale of Stock")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(45)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Receive for Services")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(25)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("XMAS Fund Matures")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(100)
                .build());

        communityChestDeck.add(EventCard.builder()
                .description("Get out of Jail")
                .cardType(CardType.GET_OUT_OF_JAIL)
                .build());
    }

    public void initializeChanceDeck() {
        chanceDeck = new ArrayList<>();

        chanceDeck.add(EventCard.builder()
                .description("Go to Jail")
                .cardType(CardType.GO_TO_JAIL)
                .moveToField(30)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Advance to GO")
                .cardType(CardType.MOVE_TO_FIELD)
                .moveToField(0)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Advance to Boardwalk")
                .cardType(CardType.MOVE_TO_FIELD)
                .moveToField(39)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Advance to St Charles Place")
                .cardType(CardType.MOVE_TO_FIELD)
                .moveToField(11)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Move to next Railroad")
                .cardType(CardType.MOVE_TO_RAILROAD)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Street Repairs")
                .cardType(CardType.STREET_REPAIRS)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Advance to Illinois Ave")
                .cardType(CardType.MOVE_TO_FIELD)
                .moveToField(24)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Take a ride on Reading")
                .cardType(CardType.MOVE_TO_FIELD)
                .moveToField(5)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Move to next Utility")
                .cardType(CardType.MOVE_TO_UTILITY)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Go back 3 Spaces")
                .cardType(CardType.MOVE_SPACES)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Dividend")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(50)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Building Loan Matures")
                .cardType(CardType.GET_MONEY_CARD)
                .payOrGetMoney(150)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Poor Tax")
                .cardType(CardType.PAY_MONEY_CARD)
                .payOrGetMoney(15)
                .build());

        chanceDeck.add(EventCard.builder()
                .description("Get out of Jail")
                .cardType(CardType.GET_OUT_OF_JAIL)
                .build());
    }
}
