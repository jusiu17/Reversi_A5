import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class A5 {

    static int n = 6500;

    private int[][] weights = {
            {300, -100, 100,  50,  50, 100, -100,  300},
            {-100, -200, -50, -50, -50, -50, -200, -100},
            {100,  -50, 100,   0,   0, 100,  -50,  100},
            {50,  -50,   0,   0,   0,   0,  -50,   50},
            {50,  -50,   0,   0,   0,   0,  -50,   50},
            {100,  -50, 100,   0,   0, 100,  -50,  100},
            {-100, -200, -50, -50, -50, -50, -200, -100},
            {300, -100, 100,  50,  50, 100, -100,  300}};

    private static int[][]weight_scores = {{99, -8, 8, 6, 6, 8, -8, 99},
            {-8, -24, -4, -3, -3, -4, -24, -8},
            {8, -4, 7, 4, 4, 7, -4, 8},
            {6, -3, 4, 0, 0, 4, -3, 6},
                {6, -3, 4, 0, 0, 4, -3, 6},
                    {8, -4, 7, 4, 4, 7, -4, 8},
                        {-8, -24, -4, -3, -3, -4, -24, -8},
                            {99, -8, 8, 6, 6, 8, -8, 99}};

    static Reversi game = new Reversi();

    public static void main(String[] arg) throws CloneNotSupportedException {
        boolean endGame = false;
        do {
            if (!game.checkFull()) {
                if (game.hasPossibleMove()) {
                    if (game.isTurn())
                        //TODO : player for now should be pMCTS
                        askPMCTSMovement();
                        //askPlayerMovement();
                    else
                        //TODO : player for now should be hMCTS
                        askHMCTSMovement();
//                        askPlayerMovement();
//                        askPMCTSMovement();
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
                            askHMCTSMovement();
//                            askPlayerMovement();
//                            askPMCTSMovement();
                    } else {
                        game.showGameBoard();
                        System.out.println(" " + (game.isTurn() ? "pMCTS" : "hMCTS") + " has no possible move also");
                        endGame = true;
                    }
                }
            } else
                endGame = true;
        } while (!endGame);

        game.showGameBoard();
        if (game.getPMCTSCount() > game.getHMCTSCount()){
            System.out.println("\tpMCTS Wins !!!");
        }
        else if (game.getPMCTSCount() < game.getHMCTSCount()) {
            System.out.println("\thMCTS Wins !!!");
        }
        else {
            System.out.println("\tIt is a draw.\n");
        }
        //System.out.println("Thanks for playing!!!");
    }


    public static void askPMCTSMovement() throws CloneNotSupportedException{
        game.showGameBoard();
        int pos[] = runPMCTS();
        BoardCol col = BoardCol.values()[pos[1]];
        System.out.println(" pMCTS place disk : " + col.toString() + (pos[0]+1));
        game.placeDisks(pos[0], pos[1]);
    }


    public static void askHMCTSMovement()throws CloneNotSupportedException{
        game.showGameBoard();
        int pos[] = runHMCTS();
        BoardCol col = BoardCol.values()[pos[1]];
        System.out.println(" hMCTS place disk : " + col.toString() + (pos[0]+1));
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
        int playout = 0;
        double startTime = System.nanoTime();
        Random rand = new Random();
        List<int[]> possibleList = game.getPossibleList();
        List<int[]> possibleScore = new ArrayList<>();
        double totalTime = (System.nanoTime() - startTime)/(double)1000000000;

        findPossible:
        for (int[] possible : possibleList) {
            Reversi temp_game = (Reversi) game.clone();
            int score = 0;
            int x = possible[0];
            int y = possible[1];

            temp_game.placeDisks(x,y);
            Reversi temp_gameMove = (Reversi) temp_game.clone();
            List<int[]> temp_move;


            for (int i = 0; i < n ; i++){
                temp_move = temp_gameMove.getPossibleList();
                int[] move;

                while(!temp_move.isEmpty()){
                    score += temp_move.size();
                    move = temp_move.get(rand.nextInt(temp_move.size()));
                    temp_gameMove.placeDisks(move[0], move[1]);
                    temp_move = temp_gameMove.getPossibleList();
                    totalTime = (double) (System.nanoTime() - startTime)/(double)1000000000;
                    if(totalTime > 4.9995)
                        break findPossible;
                }
                score /= 10;
                if (game.isTurn()) {
                    if (temp_game.getPMCTSCount() > 32)
                        score += 100;
                    else if (temp_game.getPMCTSCount() == 32)
                        score += 30;
                }
                else{
                    if (temp_game.getHMCTSCount() > 32)
                        score += 100;
                    else if (temp_game.getHMCTSCount() == 32)
                        score += 30;
                }

                temp_gameMove = (Reversi) temp_game.clone();
                playout++;
            }
            possibleScore.add(new int[]{score, x, y});
        }

        int random = rand.nextInt(possibleScore.size());
        int highest = possibleScore.get(random)[0];
        int row = possibleScore.get(random)[1];;
        int col = possibleScore.get(random)[2];;

        for (int[] score : possibleScore){
            if(score[0] > highest) {
                highest = score[0];
                row = score[1];
                col = score[2];
            }
            //System.out.println("score : " + score[0] + " \trow : " + (score[1]+1) + " \tcol : " + BoardCol.values()[score[2]] + " \thighest : " + highest);
        }
        totalTime = (double) (System.nanoTime() - startTime)/(double)1000000000;
        //System.out.println(" Run Time : " + totalTime + "s");
        //System.out.println(" Total playouts : "+ playout + " \t and playouts in 1 second : " + ( (totalTime > 1) ? playout/totalTime : playout ) );
/*        if(totalTime > 5){
            System.exit(0);
        }*/
        return new int[]{row,col};
    }

    public static int[] runHMCTS()throws CloneNotSupportedException{
        Reversi temp_game = (Reversi) game.clone();
        int[] tempReuslt =  minimax(temp_game,false, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        int x=tempReuslt[1];
        int y=tempReuslt[2];
        return new int[]{x,y};
    }

    public static int[] minimax(Reversi broad,boolean turn,int depth,int alpha ,int beta) throws CloneNotSupportedException{
        if (depth==5){
            return new int[]{};
        }
        int best_utility = 1000;
        Random rand = new Random();
        List<int[]> possibleList = game.getPossibleList();
        List<int[]> possibleScore = new ArrayList<>();
        int maxScore=Integer.MIN_VALUE, minScore = Integer.MAX_VALUE;
        int [] best_action;
        for (int[] possible : possibleList) {
            Reversi temp_game = (Reversi) broad.clone();                    //copy a new one
            int currentScore = 0;                                           //count the score
            int x = possible[0];                                            //two placement
            int y = possible[1];
            List<int[]> temp_move=temp_game.getPossibleList();                                          //Assume movement
            int[] nextMove;
            temp_game.placeDisks(x,y);                                      //put the assume in to the broad
            //trough into the recursive function to run next levels children
            nextMove= minimax(temp_game,!turn, depth+1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            currentScore += weight_scores[x][y];
            //----------------------------- n  --- this get the HIGHTEST score

            if(game.isTurn()){
                if (currentScore>maxScore){
                    maxScore=currentScore;
                    possibleScore.add(new int[]{currentScore, x, y});
                }
                if (maxScore>beta){
                    return new int[]{maxScore, x, y};
                }
                alpha = Integer.max(alpha, currentScore);
            }
            else{
                if (currentScore<minScore){
                    minScore=currentScore;
                    possibleScore.add(new int[]{currentScore, x, y});
                }
                if (minScore<=alpha){
                    return new int[]{minScore, x, y};
                }
                beta = Integer.min(alpha, currentScore);
            }
        }
        int random = rand.nextInt(possibleScore.size());
        int highest = possibleScore.get(random)[0];
        int row = possibleScore.get(random)[1];
        int col = possibleScore.get(random)[2];
        for (int[] score : possibleScore){
            if(score[0] > highest) {
                highest = score[0];
                row = score[1];
                col = score[2];
            }
        }

        return new int[]{highest,row, col};
    }
}