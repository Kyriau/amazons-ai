package game;

/**
 * Row column indexing is the same as for matrices.
 */
public class Board {

    public static final int DEFAULT_NUM_ROWS = 10;
    public static final int DEFAULT_NUM_COLS = 10;

    public final int numRows;
    public final int numCols;
    private BoardPieces[][] board;
    private BoardPieces playerTurn;

    /**
     * Default 10x10 board with white going first
     */
    public Board(){
        this(DEFAULT_NUM_ROWS,DEFAULT_NUM_COLS);

        //White is first to move
        playerTurn  = BoardPieces.WHITE;

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
        playerTurn = BoardPieces.WHITE;
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

    public int getNumRows(){
        return numRows;
    }

    public int getNumCols(){
        return numCols;
    }

    public BoardPieces getPlayerTurn(){
        return playerTurn;
    }

    /**
     * this function assumes the move is valid, it then applies the move to the board state, and pushes the move on to the stack
     * @param move A valid move from the current board state
     */
    public void playMove(Move move){
        //Comment this check out for more speed
        if(this.getPieceAt(move.startRow, move.startCol) != playerTurn){
            throw new IllegalArgumentException("Trying to move a boards piece that is not the current players turn");
        }

        this.setPieceAt(move.startRow, move.startCol, BoardPieces.BLANK);
        this.setPieceAt(move.endRow, move.endCol, playerTurn);
        this.setPieceAt(move.arrowRow, move.arrowCol, BoardPieces.ARROW);
        togglePlayerTurn();
    }

    /**
     *
     * @param move Undoes what this move would have done. ASSUMES THE MOVE WAS THE LAST MOVE PLAYED TO REACH THIS STATE
     */
    public void revertMove(Move move){
            togglePlayerTurn();
            this.setPieceAt(move.arrowRow, move.arrowCol, BoardPieces.BLANK);
            this.setPieceAt(move.endRow, move.endCol, BoardPieces.BLANK);
            this.setPieceAt(move.startRow, move.startCol, playerTurn);
    }

    /**
     * Changes the board state from the current players turn to the other players turn
     */
    public void togglePlayerTurn(){
        if(playerTurn == BoardPieces.WHITE){
            playerTurn = BoardPieces.BLACK;
        }else{
            playerTurn = BoardPieces.WHITE;
        }
    }

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
        boolean areEqual = this.playerTurn == b.playerTurn;
        areEqual = areEqual && this.numRows == b.numRows;
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
        StringBuilder s = new StringBuilder("playerTurn = " + playerTurn.toString() + "\n");
        s.append("Board Dimensions = " + numRows + "x" + numCols + "\n");
        for (int i = 0; i < numRows; i++) {
            for(int j = 0; j< numCols; j++) {
                if (j == numCols - 1) {
                    s.append((board[i][j]).toString() + "\n");
                } else {
                    s.append((board[i][j]).toString() + ", ");
                }
            }
        }
        return s.toString();
    }
}
