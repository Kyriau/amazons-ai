package game;

import java.util.ArrayList;

/**
 * Row column indexing is the same as for matrices.
 * This class is written to be fast
 */
public class Board {

    public static final int DEFAULT_NUM_ROWS = 10;
    public static final int DEFAULT_NUM_COLS = 10;

    private static final int[] rowDirSequence = {-1,-1,-1, 0, 1, 1,  1,  0};
    private static final int[] colDirSequence = {-1, 0, 1, 1, 1, 0, -1, -1};

    public final int numRows;
    public final int numCols;
    private int[][] board;

    /**
     * Default 10x10 board with white going first
     */
    public Board(){
        this(DEFAULT_NUM_ROWS,DEFAULT_NUM_COLS);

        // White's positioning
        board[3][0] = BoardPieces.WHITE;
        board[0][3] = BoardPieces.WHITE;
        board[0][6] = BoardPieces.WHITE;
        board[3][9] = BoardPieces.WHITE;
        // Black's positioning
        board[6][0] = BoardPieces.BLACK;
        board[9][3] = BoardPieces.BLACK;
        board[9][6] = BoardPieces.BLACK;
        board[6][9] = BoardPieces.BLACK;
    }

    /**
     * Blank board with white to move first of dimensions specified
     * @param numRows
     * @param numCols
     */
    public Board(int numRows,int numCols){
        this.numRows = numRows;
        this.numCols = numCols;
        board = new int[numRows][numCols];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                    board[i][j] = BoardPieces.EMPTY;
            }
        }
    }

    /*
     * I do not bounced check here because the JVM does it anyways And the error will be the same
     */

    public int getPieceAt(int row, int col){
        return board[row][col];
    }

    public void setPieceAt(int row, int col, int piece){
        board[row][col] = piece;
    }

    public int getNumRows(){
        return numRows;
    }

    public int getNumCols(){
        return numCols;
    }


    /**
     * this function assumes the move is valid, it then applies the move to the board state, and pushes the move on to the stack
     * @param move A valid move from the current board state
     */
    public void playMove(Move move){
        int pieceColor = board[move.startRow][move.startCol];
        board[move.startRow][move.startCol] = BoardPieces.EMPTY;
        board[move.endRow][move.endCol] = pieceColor;
        board[move.arrowRow][move.arrowCol] = BoardPieces.ARROW;
    }

    /**
     *
     * @param move Undoes what this move would have done. ASSUMES THE MOVE WAS THE LAST MOVE PLAYED TO REACH THIS STATE
     */
    public void revertMove(Move move){

        int pieceColor =  board[move.endRow][move.endCol];
        board[move.arrowRow][move.arrowCol] = BoardPieces.EMPTY;
        board[move.endRow][move.endCol] = BoardPieces.EMPTY;
        board[move.startRow][move.startCol] = pieceColor;

    }

    /**
     *
     * @param playerTurn The player whose valid move set from this state we want to know
     * @return An array list containing all possible moves for the player
     */
    public ArrayList<Move> getAllMoves(int playerTurn){
        if(playerTurn != BoardPieces.BLACK && playerTurn != BoardPieces.WHITE){
            throw new IllegalArgumentException("Invalid players specified: playerTurn = " + playerTurn);
        }
        ArrayList<Move> moves = new ArrayList<>(100);
        int[][] piecePositions = getPieceLocations(playerTurn);
        for(int i = 0; i < piecePositions.length; i++){
            moves.addAll(getMovesForPiece(piecePositions[i][0], piecePositions[i][1]));
        }
        return moves;
    }

    /**
     * I am sorry about this abomination
     * @param row The row position of the piece
     * @param col The column position of the piece
     * @return The set of all possible moves the Queen specified at (row,col)
     */
    public ArrayList<Move> getMovesForPiece(int row, int col){
        if(board[row][col] != BoardPieces.BLACK && board[row][col] != BoardPieces.WHITE){
            throw new IllegalArgumentException("Location does not contain a movable piece");
        }
        ArrayList<Move> moves = new ArrayList<>(100);
        //For every direction we can move a queen
        for(int dirIndex = 0; dirIndex < rowDirSequence.length; dirIndex++){
            int rowOffset = rowDirSequence[dirIndex];
            int colOffset = colDirSequence[dirIndex];
            int rowIndex = rowOffset + row;//move one position away from piece
            int colIndex = colOffset + col;
            //For every valid spot we can move a queen to
            while(0 <= rowIndex
                    && rowIndex < numRows
                    && 0 <= colIndex
                    && colIndex < numCols
                    && board[rowIndex][colIndex] == BoardPieces.EMPTY){
                //For every direction we can shoot an arrow
                for(int arrowDirIndex = 0; arrowDirIndex < rowDirSequence.length; arrowDirIndex++){
                    int aRowOffset = rowDirSequence[arrowDirIndex];
                    int aColOffset = colDirSequence[arrowDirIndex];
                    int aRowIndex = aRowOffset + rowIndex;
                    int aColIndex = aColOffset + colIndex;
                    //for each valid location for an arrow
                    while(0 <= aRowIndex
                            && aRowIndex < numRows
                            && 0 <= aColIndex
                            && aColIndex < numCols
                            && (board[aRowIndex][aColIndex] == BoardPieces.EMPTY || (aRowIndex == row && aColIndex == col))){

                        moves.add(new Move(row,col,rowIndex,colIndex, aRowIndex, aColIndex));
                        aRowIndex += aRowOffset;
                        aColIndex += aColOffset;
                    }
                }
                rowIndex += rowOffset;
                colIndex += colOffset;
            }
        }
        return moves;
    }

    /**
     *
     * @param player The integer representing the colour of the player whose pieces we wish to find
     * @return Indexed [0 <=piece < 4][0 = row, 1 = col], The locations for the four pieces of the opponent
     */
    public int[][] getPieceLocations(int player){
        int[][] piecePositions = new int[4][2];// Indexed [piece][0 = row, 1 = col]
        int pieceIndex = 0;
        outer: for(int i = 0; i < numRows; i++){
            if(pieceIndex == 4){
                break outer;
            }
            for(int j = 0; j < numCols; j++){
                if(board[i][j] == player){
                    piecePositions[pieceIndex][0] = i;
                    piecePositions[pieceIndex][1] = j;
                    pieceIndex += 1;
                }
            }
        }
        return piecePositions;
    }



    /**
     *
     * @param b The board to copy
     * @return A copy of the board
     */
    public static Board copyBoard(Board b){
        Board copy = new Board(b.numRows, b.numCols);
        for(int i = 0; i < b.numRows; i++){
            for(int j = 0; j < b.numCols; j++){
               copy.board[i][j] = b.board[i][j];
            }
        }
        return copy;
    }

    /**
     *
     * @param b The other board to compare against
     * @return true if player turns are the same, board dimensions are the same, and all board elements are the same
     */
    public boolean equals(Board b) {
        boolean areEqual = this.numRows == b.numRows;
        areEqual = areEqual && this.numCols == b.numCols;

        if(!areEqual){
            return false;
        }else {
            for (int i = 0; i < b.numRows; i++) {
                for (int j = 0; j < b.numCols; j++) {
                    if (board[i][j] != b.board[i][j])
                        return false;
                }
            }
            return true;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Board Dimensions = " + numRows + "x" + numCols + "\n");
        for (int i = 0; i < numRows; i++) {
            for(int j = 0; j< numCols; j++) {
                if (j == numCols - 1) {
                    s.append(BoardPieces.getPieceString(board[i][j]) + "\n");
                } else {
                    s.append(BoardPieces.getPieceString(board[i][j]) + ", ");
                }
            }
        }
        return s.toString();
    }
}
