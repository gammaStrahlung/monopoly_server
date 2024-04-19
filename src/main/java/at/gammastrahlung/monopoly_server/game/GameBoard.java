package at.gammastrahlung.monopoly_server.game;

public class GameBoard {

    private Player Bank = new Player();
    private Field[] gameBoard;


    void inializeGameBoard(){
        gameBoard[0] = new Field(0, "LOS!");
        gameBoard[1] = new Property(1, "Minimundus", Bank, 100, 5, 10, 15, 20, 30);

    }

}
