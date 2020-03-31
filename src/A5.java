import java.util.Scanner;

public class A5 {
    static Reversi game = new Reversi();
    public static void main(String[] arg){
        boolean endGame = false;
        do {
            if(!game.checkFull()) {
                if (game.hasPossibleMove()) {
                    askMovement();
                } else {
                    System.out.print(" " + (game.isTurn() ? "Player" : "AI") + " has no possible move\n Chance passes back to " + (!game.isTurn() ? "Player" : "AI"));
                    game.setTurn(!game.isTurn());
                    game.generatePossible();
                    if (game.hasPossibleMove()) {
                        askMovement();
                    } else {
                        endGame = true;
                    }
                }
            }
            else
                endGame = true;
        } while(!endGame);

        game.showGameBoard();
        if(game.getFirstCount() > game.getSecondCount())
            System.out.println("\tPlayer Wins !!!");
        else if(game.getFirstCount() < game.getSecondCount())
            System.out.println("\tAI Wins !!!");
        else
            System.out.println("\tIt is a draw.\n");
        System.out.println("Thanks for playing!!!");
    }

    public static void askMovement(){
        Scanner input = new Scanner(System.in);
        String position;
        int row;
        int col;
        game.showGameBoard();
        do {
            if (game.isTurn())
                System.out.println(" Select a square which is a possible move(*)");
            System.out.print(" " + (game.isTurn() ? "Player" : "AI") + " place disk (e.g. E3): ");
            position = input.next();
            char[] pos = position.toUpperCase().toCharArray();
            col = BoardCol.valueOf(String.valueOf(pos[0])).ordinal();
            row = pos[1] - 49;
            if (!game.isPossibleMove(row, col))
                System.out.println(" Invalid move!!\n ");
        } while (!game.isPossibleMove(row, col));
        game.placeDisks(row, col);
    }
}
