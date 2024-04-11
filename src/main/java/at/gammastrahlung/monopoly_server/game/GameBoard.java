package at.gammastrahlung.monopoly_server.game;

public class GameBoard {
    private Field[] gameBoard;


    void inializeGameBoard(){
        gameBoard[0] = new Field(0, "LOS!");
        gameBoard[1] = Field.builder().build();

    }

}
