package game;
import java.util.Stack;

public class BoardState {

    private Stack<Move> moveSequence;
    private Board board;
    private BoardPieces playerTurn;


    /**
     * creates a default 10 x 10 board
     * White is the first to move
     * White's row index is lower than Black's
     */
    public BoardState(){
        board = new Board(10, 10);
        moveSequence = new Stack<Move>();

        //White is first to move
        playerTurn  = BoardPieces.WHITE;
        // White's positioning
        board.setPieceAt(3,0,BoardPieces.WHITE);
        board.setPieceAt(0,3,BoardPieces.WHITE);
        board.setPieceAt(0,6,BoardPieces.WHITE);
        board.setPieceAt(3,9,BoardPieces.WHITE);
        // Black's positioning
        board.setPieceAt(6,0,BoardPieces.BLACK);
        board.setPieceAt(9,3,BoardPieces.BLACK);
        board.setPieceAt(9,6,BoardPieces.BLACK);
        board.setPieceAt(6,9,BoardPieces.BLACK);
    }

    /**
     * this function assumes the move is valid, it then applies the move to the board state, and pushes the move on to the stack
     * @param move A valid move from the current board state
     */
    public void playMove(Move move){
        //Comment this check out for more speed
        if(board.getPieceAt(move.startRow, move.startCol) != playerTurn){
            throw new IllegalArgumentException("Trying to move a boards piece that is not the current players turn");
        }

        board.setPieceAt(move.startRow, move.startCol, BoardPieces.BLANK);
        board.setPieceAt(move.endRow, move.endCol, playerTurn);
        board.setPieceAt(move.arrowRow, move.arrowCol, BoardPieces.ARROW);
        togglePlayerTurn();
        moveSequence.push(move);
    }

    /**
     * the board state will be reverted to one state prior to the current state
     * @return True if there is a previous board state and the board is reverted
     */
    public boolean revertMove(){
        if(!moveSequence.isEmpty()) {
            Move move = moveSequence.pop();
            togglePlayerTurn();
            board.setPieceAt(move.arrowRow, move.arrowCol, BoardPieces.BLANK);
            board.setPieceAt(move.endRow, move.endCol, BoardPieces.BLANK);
            board.setPieceAt(move.startRow, move.startCol, playerTurn);
            return true;
        }else{
            return false;
        }
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

    /**
     *
     * @param boardState The other board to compare against
     * @return true if player turns are the same, board dimensions are the same, and all board elements are the same
     */
    public boolean equals(BoardState boardState) {
        boolean areEqual = playerTurn == boardState.playerTurn;
        areEqual = areEqual && board.numRows == boardState.board.numRows;
        areEqual = areEqual && board.numCols == boardState.board.numCols;

        if(!areEqual)
            return false;

        for(int i = 0; i < board.numRows; i++){
            for(int j = 0; j < board.numCols; j++){
                if(board.getPieceAt(i,j) != boardState.board.getPieceAt(i,j))
                    return false;
            }
        }
        return true;
    }
}
