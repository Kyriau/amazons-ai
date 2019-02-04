package game;

import junit.framework.TestCase;
import org.junit.*;

public class BoardTest extends TestCase {

    @Test
    public void testConstructors(){
        //Dimension constructor
        Board b2 = new Board(5,10);
        assertTrue(b2.numRows == 5);
        assertTrue(b2.numCols == 10);
        assertTrue(b2.getPlayerTurn() == BoardPieces.WHITE);
        for(int i = 0; i < b2.numRows; i++){
            for(int j = 0; j < b2.numCols; j++){
                assertTrue(b2.getPieceAt(i,j) == BoardPieces.BLANK);
            }
        }

        //Blank Constructor
        Board b1 = new Board();
        assertTrue(b1.numRows == Board.DEFAULT_NUM_ROWS);
        assertTrue(b1.numCols == Board.DEFAULT_NUM_COLS);
        assertTrue(b1.getPlayerTurn() == BoardPieces.WHITE);

        assertTrue(b1.getPieceAt(3,0) == BoardPieces.WHITE
                && b1.getPieceAt(0,3) == BoardPieces.WHITE
                && b1.getPieceAt(0,6) == BoardPieces.WHITE
                && b1.getPieceAt(3,9) == BoardPieces.WHITE);

        assertTrue(b1.getPieceAt(6,0) == BoardPieces.BLACK
                && b1.getPieceAt(9,3) == BoardPieces.BLACK
                && b1.getPieceAt(9,6) == BoardPieces.BLACK
                && b1.getPieceAt(6,9) == BoardPieces.BLACK);


    }

    @Test
    public void testPlayMoveAndRevertMove(){
        Board b1 = new Board();
        Board b2 = new Board();
        Move move = new Move(3,0, 5,0,5,9);

        //Initially in the same state
        assertTrue(b1.equals(b2));
        b1.playMove(move);
        //Cycling turn correctly
        assertFalse(b1.getPlayerTurn() == b2.getPlayerTurn());
        assertFalse(b1.equals(b2));
        //Move b2 into the same state
        b2.playMove(move);
        assertTrue(b1.getPlayerTurn() == b2.getPlayerTurn());
        assertTrue(b1.equals(b2));

        //Go back 1 move for b1
        b1.revertMove(move);
        assertFalse(b1.getPlayerTurn() == b2.getPlayerTurn());
        assertFalse(b1.equals(b2));

        //Go back 1 move for b2
        b2.revertMove(move);
        assertTrue(b1.getPlayerTurn() == b2.getPlayerTurn());
        assertTrue(b1.equals(b2));

        //Ensuring in initial game state
        assertTrue(b1.equals(new Board()));


    }

}
