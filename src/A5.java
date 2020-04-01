import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class A5 {

    static int n = 30000;

    static Reversi game = new Reversi();
    public static void main(String[] arg) throws CloneNotSupportedException {
        boolean endGame = false;
        do {
            if(!game.checkFull()) {
                if (game.hasPossibleMove()) {
                    if (game.isTurn())
                        //TODO : player for now should be pMCTS
                        askPMCTSMovement();
                        //askPlayerMovement();
                    else
                        //TODO : player for now should be hMCTS
                        //askHMCTSMovement();
//                        askPlayerMovement();
                        askPMCTSMovement();
                } else {
                    game.showGameBoard();
                    System.out.println(" " + (game.isTurn() ? "pMCTS" : "hMCTS") + " has no possible move\n Chance passes back to " + (!game.isTurn() ? "pMCTS" : "hMCTS"));
                    game.setTurn(!game.isTurn());
                    game.generatePossible();
                    if (game.hasPossibleMove()) {
                        if (game.isTurn())
                            //TODO : player for now should be pMCTS
                            askPMCTSMovement();
                            //askPlayerMovement();
                        else
                            //TODO : player for now should be hMCTS
                            //askHMCTSMovement();
//                            askPlayerMovement();
                            askPMCTSMovement();
                    } else {
                        game.showGameBoard();
                        System.out.println(" " + (game.isTurn() ? "pMCTS" : "hMCTS") + " has no possible move also");
                        endGame = true;
                    }
                }
            }
            else
                endGame = true;
        } while(!endGame);

        game.showGameBoard();
        if(game.getPMCTSCount() > game.getHMCTSCount())
            System.out.println("\tpMCTS Wins !!!");
        else if(game.getPMCTSCount() < game.getHMCTSCount())
            System.out.println("\thMCTS Wins !!!");
        else
            System.out.println("\tIt is a draw.\n");
        System.out.println("Thanks for playing!!!");
    }


    public static void askPMCTSMovement() throws CloneNotSupportedException{
        game.showGameBoard();
        System.out.print(" pMCTS place disk : ");
        int pos[] = runPMCTS();
        BoardCol col = BoardCol.values()[pos[1]];
        System.out.println(col.toString() + (pos[0]+1));
        game.placeDisks(pos[0], pos[1]);
    }


    public static void askHMCTSMovement(){
        game.showGameBoard();
        System.out.print(" hMCTS place disk : ");
        int pos[] = game.runHMCTS();
        BoardCol col = BoardCol.values()[pos[0]];
        System.out.println(col.toString() + pos[1]);
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

    public static int[] runPMCTS() throws CloneNotSupportedException{
        Random rand = new Random();
        List<int[]> possibleList = game.getPossibleList();
        List<int[]> possibleScore = new ArrayList<>();

        for (int[] possible : possibleList) {
            Reversi temp_game = (Reversi) game.clone();
            int score = 0;
            int x = possible[0], y = possible[1];

            temp_game.placeDisks(x,y);
            Reversi temp_gameMove = (Reversi) temp_game.clone();
            List<int[]> temp_move;
            for (int i = 0; i < n ; i++){
                temp_move = temp_gameMove.getPossibleList();
                int[] move;
                while(!temp_move.isEmpty()){
                    move = temp_move.get(rand.nextInt(temp_move.size()));
                    temp_gameMove.placeDisks(move[0], move[1]);
                    temp_move = temp_gameMove.getPossibleList();
                }

                if (game.isTurn()) {
                    if (temp_game.getPMCTSCount() > 32)
                        score += 3;
                    else if (temp_game.getPMCTSCount() == 32)
                        score += 1;
                }
                else{
                    if (temp_game.getHMCTSCount() > 32)
                        score += 3;
                    else if (temp_game.getHMCTSCount() == 32)
                        score += 1;
                }

                temp_gameMove = (Reversi) temp_game.clone();
            }

            possibleScore.add(new int[]{score, x, y});
        }

        int highest = possibleScore.get(rand.nextInt(possibleScore.size()))[0];
        int row = possibleScore.get(rand.nextInt(possibleScore.size()))[1];;
        int col = possibleScore.get(rand.nextInt(possibleScore.size()))[2];;

        for (int[] score : possibleScore){
            if(score[0] > highest) {
                highest = score[0];
                row = score[1];
                col = score[2];
            }
            //System.out.println("score : " + score[0] + " \trow : " + score[1] + " \tcol : " + score[2] + " \thighest : " + highest);
        }
        return new int[]{row,col};
    }
}

