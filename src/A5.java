import java.util.Scanner;

public class A5 {
    static Reversi game = new Reversi();
    public static void main(String[] arg){
        boolean endGame = false;
        do {
            if(!game.checkFull()) {
                if (game.hasPossibleMove()) {
                    if (game.isTurn())
                        //TODO player for now
                        //askPMCTSMovement();
                        askPlayerMovement();
                    else
                        //TODO player for now
                        //askHMCTSMovement();
                        askPlayerMovement();
                } else {
                    System.out.print(" " + (game.isTurn() ? "pMCTS" : "hMCTS") + " has no possible move\n Chance passes back to " + (!game.isTurn() ? "pMCTS" : "hMCTS"));
                    game.setTurn(!game.isTurn());
                    game.generatePossible();
                    if (game.hasPossibleMove()) {
                        if (game.isTurn())
                            //TODO player for now
                            //askPMCTSMovement();
                            askPlayerMovement();
                        else
                            //TODO player for now
                            //askHMCTSMovement();
                            askPlayerMovement();
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
            System.out.println("\tpMCTS Wins !!!");
        else if(game.getFirstCount() < game.getSecondCount())
            System.out.println("\thMCTS Wins !!!");
        else
            System.out.println("\tIt is a draw.\n");
        System.out.println("Thanks for playing!!!");
    }


    public static void askPMCTSMovement(){
        game.showGameBoard();
        System.out.print(" pMCTS place disk : ");
        int pos[] = game.runPMCTS();
        System.out.println(pos[0]+pos[1]);
        game.placeDisks(pos[0], pos[1]);
    }


    public static void askHMCTSMovement(){
        game.showGameBoard();
        System.out.print(" hMCTS place disk : ");
        int pos[] = game.runHMCTS();
        System.out.println(pos[0]+pos[1]);
        game.placeDisks(pos[0], pos[1]);

    }


    public static void askPlayerMovement(){
        Scanner input = new Scanner(System.in);
        String position;
        int row;
        int col;
        game.showGameBoard();
        do {
            System.out.println(" Select a square which is a possible move(*)");
            System.out.print(" Player place disk (e.g. E3): ");
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

