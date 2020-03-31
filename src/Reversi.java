import java.util.Scanner;

/**
 *     turn : true as Player and false as AI
 *     gameBoard : 0 as empty, 1 as AI and 2 as Player
 */

enum BoardCol {A, B, C, D, E, F, G, H}

public class Reversi {
    private boolean turn;
    private int[][] gameBoard;
    private boolean[][] possibleMove;
    private int firstCount;
    private int secondCount;

    public Reversi(){
        this.turn = true;
        this.gameBoard = new int[8][8];
        this.possibleMove = new boolean[8][8];
        this.firstCount = 0;
        this.secondCount = 0;
        clearBoard();
        initBoard();
    }


    public boolean isTurn() {
        return turn;
    }
    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void placeDisks(int row, int col){
        this.gameBoard[row][col] = turn ? 2 : 1;
        redrawBoard(row, col);
        countScore();
        this.turn = !turn;
        generatePossible();
    }

    public void setPossibleMove(boolean[][] possibleMove) {
        this.possibleMove = possibleMove;
    }
    public boolean isPossibleMove(int row, int col){
        return this.possibleMove[row][col];
    }

    public int getFirstCount() {
        return firstCount;
    }
    public int getSecondCount() {
        return secondCount;
    }

    private void clearBoard() {
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++) {
                this.gameBoard[row][col] = 0;
                this.possibleMove[row][col] = false;
            }
    }

    private void initBoard(){
        placeDisks(3,4);
        placeDisks(3,3);
        placeDisks(4,3);
        placeDisks(4,4);
        countScore();
        generatePossible();
    }

    private void countScore() {
        int scoreX = 0;
        int scoreO = 0;
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++){
                if (gameBoard[row][col] == 1)
                    scoreO++;
                if (gameBoard[row][col] == 2)
                    scoreX++;
            }
        this.firstCount = scoreX;
        this.secondCount = scoreO;
    }

    private void redrawBoard(int row, int col){
        redrawRow(row, col);
        redrawCol(row, col);
        redrawDiagonal(row, col);
    }

    private void redrawRow(int row, int col){
        int play = turn ? 2 : 1;
        for (int i = col-1 ; i >= 0 ; i--) {
            if (gameBoard[row][i] == 0)
                break;
            if (gameBoard[row][i] == play) {
                for (int k = i; k < col; k++) {
                    gameBoard[row][k] = play;
                }
                break;
            }
        }
        for (int i = col+1 ; i < 8 ; i++) {
            if (gameBoard[row][i] == 0)
                break;
            if (gameBoard[row][i] == play) {
                for (int k = i; k > col; k--) {
                    gameBoard[row][k] = play;
                }
                break;
            }
        }
    }

    private void redrawCol(int row, int col){
        int play = turn ? 2 : 1;
        for (int i = row-1 ; i >= 0 ; i--) {
            if (gameBoard[i][col] == 0)
                break;
            if (gameBoard[i][col] == play) {
                for (int k = i; k < row; k++) {
                    gameBoard[k][col] = play;
                }
                break;
            }
        }
        for (int i = row+1 ; i < 8 ; i++) {
            if (gameBoard[i][col] == 0)
                break;
            if (gameBoard[i][col] == play) {
                for (int k = i; k > row; k--) {
                    gameBoard[k][col] = play;
                }
                break;
            }
        }
    }

    private void redrawDiagonal(int row, int col){
        int play = turn ? 2 : 1;

        // diagonal \
        for (int i = row-1 , j = col-1 ; i >= 0 && j >= 0 ; i--, j--) {
            if (gameBoard[i][j] == 0)
                break;
            if (gameBoard[i][j] == play) {
                for (int m = i, n = j; m < row && n < col; m++, n++) {
                    gameBoard[m][n] = play;
                }
                break;
            }
        }
        for (int i = row+1 , j = col+1 ; i < 8 && j < 8 ; i++, j++) {
            if (gameBoard[i][j] == 0)
                break;
            if (gameBoard[i][j] == play) {
                for (int m = i, n = j; m > row && n > col; m--, n--) {
                    gameBoard[m][n] = play;
                }
                break;
            }
        }

        // diagonal /
        for (int i = row-1 , j = col+1 ; i >= 0 && j < 8 ; i--, j++) {
            if (gameBoard[i][j] == 0)
                break;
            if (gameBoard[i][j] == play) {
                for (int m = i, n = j; m < row && n > col; m++, n--) {
                    gameBoard[m][n] = play;
                }
                break;
            }
        }
        for (int i = row+1 , j = col-1 ; i < 8 && j >= 0 ; i++, j--) {
            if (gameBoard[i][j] == 0)
                break;
            if (gameBoard[i][j] == play) {
                for (int m = i, n = j; m > row && n < col; m--, n++) {
                    gameBoard[m][n] = play;
                }
                break;
            }
        }
    }

    private boolean valid_move(int row, int col, int rowMove, int colMove){
        int notPlay = !turn ? 2 : 1;

        if(gameBoard[row][col] != 0)
            return false;

        // check is border?
        int newRow = row + rowMove;
        int newCol = col + colMove;
        if ((newRow < 0) || (newRow > 7)) {
            return false;
        }
        if ((newCol < 0) || (newCol > 7)) {
            return false;
        }

        // check next disk same?
        if(gameBoard[newRow][newCol] != notPlay) {
            return false;
        }

        // check next disk is border?
        newRow = newRow + rowMove;
        newCol = newCol + colMove;
        if (newRow < 0 || newRow > 7) {
            return false;
        }
        if (newCol < 0 || newCol > 7) {
            return false;
        }
        // check is there same in further disk ?
        return findSameDisk(newRow, newCol, rowMove, colMove);
    }

    private boolean findSameDisk(int newRow, int newCol, int rowMove, int colMove) {
        int play = turn ? 2 : 1;

        if(gameBoard[newRow][newCol] == play)
            return true;
        newRow += rowMove;
        newCol += colMove;
        if (newRow < 0 || newRow > 7) {
            return false;
        }
        if (newCol < 0 || newCol > 7) {
            return false;
        }

        return findSameDisk(newRow, newCol, rowMove, colMove);
    }

    public void generatePossible(){
        boolean[][] move = new boolean[8][8];
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++)
                move[row][col] = false;
        boolean valid;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++){
                valid = valid_move(i,j,-1,-1);
                if (!valid) valid = valid_move(i,j,-1,0);
                if (!valid) valid = valid_move(i,j,-1,1);
                if (!valid) valid = valid_move(i,j,0,1);
                if (!valid) valid = valid_move(i,j,1,1);
                if (!valid) valid = valid_move(i,j,1,0);
                if (!valid) valid = valid_move(i,j,1,-1);
                if (!valid) valid = valid_move(i,j,0,-1);

                if(valid)
                    move[i][j] = true;
            }
        setPossibleMove(move);
    }

    public boolean hasPossibleMove(){
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++)
                if (this.possibleMove[row][col])
                    return true;
        return false;
    }

    public boolean checkFull(){
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++)
                if (this.gameBoard[row][col] == 0)
                    return false;
        return true;
    }

    public void showGameBoard() {
        System.out.println("-----------------------------------------\n");
        //System.out.println("\t\t\t   Round " + round);
        System.out.println("\t\t Player : X   AI : O \t\t");
        System.out.println("\t\t\tCurrent Score\t\t\t");
        System.out.println("\tPlayer : " + firstCount + "\t\t\tAI : " + secondCount + "\n");

        System.out.print("\t    ");
        for (BoardCol col : BoardCol.values())
            System.out.print(col + " ");
        System.out.println();
        for (int i = 0; i<8; i++) {
            System.out.print("\t  " + (i+1) + " ");
            for (int j = 0; j < 8; j++) {
                if(gameBoard[i][j] == 1)
                    System.out.print("O ");
                else if(gameBoard[i][j] == 2)
                    System.out.print("X ");
                else if(possibleMove[i][j])
                    System.out.print("* ");
                else
                    System.out.print("_ ");
            }
            System.out.print(i+1 + "\n");
        }
        System.out.print("\t    ");
        for (BoardCol col : BoardCol.values())
            System.out.print(col + " ");
        System.out.println("\n\n-----------------------------------------\n");
    }

}

class A5 {
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

        if(game.getFirstCount() > game.getSecondCount())
            System.out.println("\t" + (!game.isTurn()?"Player":"AI") + " Wins !!!");
        else if(game.getFirstCount() < game.getSecondCount())
            System.out.println("\t" + (game.isTurn()?"Player":"AI") + " Wins !!!");
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

