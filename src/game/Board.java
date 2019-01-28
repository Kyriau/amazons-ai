package game;

/**
 * Row column indexing is the same as for matrices.
 */
public class Board {

    public final int numRows;
    public final int numCols;
    private BoardPieces[][] board;

    public Board(int numRows,int numCols){
        this.numRows = numRows;
        this.numCols = numCols;
        board = new BoardPieces[numRows][numCols];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                    board[i][j] = BoardPieces.BLANK;
            }
        }
    }

    /*
     * I do not bounced check here because the JVM does it anyways And the error will be the same
     */

    public BoardPieces getPieceAt(int row, int col){
        return board[row][col];
    }

    public void setPieceAt(int row, int col, BoardPieces piece){
        board[row][col] = piece;
    }

}
