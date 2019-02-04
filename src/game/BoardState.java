package game;
import java.util.Stack;

/**
 * This class allows diving and coming back up at a low cost, meant for fast, efficient traversal.
 */
public class BoardState {

    private Stack<Move> moveSequence;
    private Board board;


    /**
     * creates a default 10 x 10 board
     * White is the first to move
     * White's row index is lower than Black's
     */
    public BoardState(){
        board = new Board(); // Default 10x10 board, white's move
        moveSequence = new Stack<Move>();
    }

    /**
     * Copies the current board, but has a move stack that is empty
     * @param b The board who's state will be copied
     */
    public BoardState(Board b){
        this.board = Board.copyBoard(b);
        moveSequence = new Stack<Move>();
    }

    /**
     * this function assumes the move is valid and is played from the correct players state
     * @param move A valid move from the current board state
     */
    public void playMove(Move move){
        board.playMove(move);
        moveSequence.push(move);
    }

    /**
     * the board state will be reverted to one state prior to the current state
     * @return True if there is a previous board state and the board is reverted
     */
    public boolean revertMove(){
        if(!moveSequence.isEmpty()) {
            Move move = moveSequence.pop();
            board.revertMove(move);
            return true;
        }else{
            return false;
        }
    }


    /**
     *
     * @param boardState The other board to compare against
     * @return true if player turns are the same, board dimensions are the same, and all board elements are the same
     */
    public boolean equals(BoardState boardState) {
        return this.board.equals(boardState.board);
    }
}
