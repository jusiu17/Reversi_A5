import java.util.ArrayList;
import java.util.List;

/**
 *     turn : true as pMCTS and false as hMCTS
 *     gameBoard : 0 as empty, 1(O) as hMCTS and 2(X) as pMCTS
 */

enum BoardCol {A, B, C, D, E, F, G, H}



public class Reversi implements Cloneable {
    private boolean turn;
    private int[][] gameBoard;
    private boolean[][] possibleMove;
    private int pMCTSCount;
    private int hMCTSCount;

    public Reversi(){
        this.turn = true;
        this.gameBoard = new int[8][8];
        this.possibleMove = new boolean[8][8];
        this.pMCTSCount = 0;
        this.hMCTSCount = 0;
        clearBoard();
        initBoard();
    }

    public boolean isLegalMove(int column){
        return gameBoard[0][column]==0;
    }
    public boolean placeMove(int column, int player){
        if(!isLegalMove(column)) {System.out.println("Illegal move!"); return false;}
        for(int i=5;i>=0;--i){
            if(gameBoard[i][column] == 0) {
                gameBoard[i][column] = (byte)player;
                return true;
            }
        }
        return false;
    }

    public void undoMove(int column){
        for(int i=0;i<=5;++i){
            if(gameBoard[i][column] != 0) {
                gameBoard[i][column] = 0;
                break;
            }
        }
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

    public int getPMCTSCount() {
        return pMCTSCount;
    }
    public int getHMCTSCount() {
        return hMCTSCount;
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
        this.pMCTSCount = scoreX;
        this.hMCTSCount = scoreO;
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

    private boolean isPossible(int row, int col){
        int play = turn ? 2 : 1;

        if ( gameBoard[row][col] != 0 )
            return false;

        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && i == j)
                    continue;
                int x = row;
                int y = col;
                do {
                    x += i;
                    y += j;
                } while (inRange(x, y) && gameBoard[x][y] != 0 && gameBoard[x][y] != play);

                if (inRange(x, y) && gameBoard[x][y] != 0 && gameBoard[x][y] == play)
                    if ((x - row) != i || (y - col) != j) 
                        return true;
            }
        }
        return false;
    }

    private boolean inRange(int row, int col) {
        return (row > -1 && row < 8 && col > -1 && col < 8);
    }

    public void generatePossible(){
        boolean[][] move = new boolean[8][8];
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++)
                move[row][col] = false;

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                    move[i][j] = isPossible(i, j);

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
        System.out.println("\t\t pMCTS : X   hMCTS : O \t\t");
        System.out.println("\t\t\tCurrent Score\t\t\t");
        System.out.println("\tpMCTS : " + pMCTSCount + "\t\t\thMCTS : " + hMCTSCount + "\n");

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
        //printPossibleList();
    }

    public List<int[]> getPossibleList(){
        List<int[]> possList = new ArrayList<>();
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++)
                if (this.possibleMove[row][col])
                    possList.add(new int[]{row,col});
        return possList;
    }

    @Override
    public Reversi clone() throws CloneNotSupportedException {
        Reversi cloneGame = (Reversi) super.clone();
        cloneGame.gameBoard = new int[8][8];
        for(int row = 0; row < 8; row++)
            for(int col = 0; col < 8; col++)
                cloneGame.gameBoard[row][col] = this.gameBoard[row][col];
        cloneGame.possibleMove = new boolean[8][8];
        for(int row = 0; row < 8; row++)
            for(int col = 0; col < 8; col++)
                cloneGame.possibleMove[row][col] = this.possibleMove[row][col];

        return cloneGame;
    }

}