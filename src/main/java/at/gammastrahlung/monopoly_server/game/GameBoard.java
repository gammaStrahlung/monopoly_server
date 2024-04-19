package at.gammastrahlung.monopoly_server.game;

public class GameBoard {

    private Player Bank = new Player();
    private Field[] gameBoard;


    void inializeGameBoard(){
        gameBoard[0] = new Field(0, "LOS!");
        gameBoard[1] = Property.builder().fieldID(1).name("Minimundus").owner(Bank).price(100).baseRent(5).oneHouseRent(10).twoHousesRent(15).threeHousesRent(20).hotelRent(30).buildable(true).build();


    }

}
