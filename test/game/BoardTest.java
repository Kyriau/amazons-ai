package game;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import junit.framework.TestCase;
import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;

public class BoardTest extends TestCase {

    @Test
    public void testConstructors(){
        //Dimension constructor
        Board b2 = new Board(5,10);
        assertTrue(b2.numRows == 5);
        assertTrue(b2.numCols == 10);
        for(int i = 0; i < b2.numRows; i++){
            for(int j = 0; j < b2.numCols; j++){
                assertTrue(b2.getPieceAt(i,j) == BoardPieces.EMPTY);
            }
        }

        //Blank Constructor
        Board b1 = new Board();
        assertTrue(b1.numRows == Board.DEFAULT_NUM_ROWS);
        assertTrue(b1.numCols == Board.DEFAULT_NUM_COLS);

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
        //Change state
        b1.playMove(move);
        assertFalse(b1.equals(b2));

        //Move b2 into the same state
        b2.playMove(move);
        assertTrue(b1.equals(b2));

        //Go back 1 move for b1
        b1.revertMove(move);
        assertFalse(b1.equals(b2));

        //Go back 1 move for b2
        b2.revertMove(move);
        assertTrue(b1.equals(b2));

        //Ensuring in initial game state
        assertTrue(b1.equals(new Board()));


    }

    @Test
    public void testGetPieceLocations(){
        int[][] expectedWhite = {
                {3,0},
                {0,3},
                {0,6},
                {3,9}
        };

        int[][] expectedBlack = {
                {6,0},
                {9,3},
                {9,6},
                {6,9}
        };

        Board b = new Board();

        int[][] resultWhite = b.getPieceLocations(BoardPieces.WHITE);
        int[][] resultBlack = b.getPieceLocations(BoardPieces.BLACK);

        assertTrue(resultWhite.length == expectedWhite.length);
        assertTrue(resultBlack.length == expectedBlack.length);

        //Ensure that the two sets are identical
        for(int i = 0; i < resultWhite.length; i++){
            assertTrue(resultWhite[i].length == 2);
            assertTrue(resultBlack[i].length == 2);

            for(int j = 0; j < expectedWhite.length; j++){
                if(Arrays.equals(resultWhite[i], expectedWhite[j])){
                    break;
                }else if (j == 4){
                    fail();
                }
            }

            for(int j = 0; j < expectedWhite.length; j++){
                if(Arrays.equals(resultBlack[i], expectedBlack[i])){
                    break;
                }else if (j == 4){
                    fail();
                }
            }
        }
    }

    @Test
    public void testGetMovesForPiece(){
        Board b = new Board();
        //System.out.println(b.getPieceAt(3,0));
        ArrayList<Move> moves = b.getMovesForPiece(3,0);
        assertTrue(moves.size() == 544);

        moves = b.getMovesForPiece(3,9);
        assertTrue(moves.size() == 544);

        moves = b.getMovesForPiece(0,3);
        assertTrue(moves.size() == 544);

        moves = b.getMovesForPiece(0,6);
        assertTrue(moves.size() == 544);

        moves = b.getMovesForPiece(6,0);
        assertTrue(moves.size() == 544);

        moves = b.getMovesForPiece(6,9);
        assertTrue(moves.size() == 544);

        moves = b.getMovesForPiece(9,3);
        assertTrue(moves.size() == 544);

        moves = b.getMovesForPiece(9,6);
        assertTrue(moves.size() == 544);
    }

    @Test
    public void testGetAllMoves() {
        Board b = new Board();

        ArrayList<Move> moves = b.getAllMoves(BoardPieces.WHITE);
        assertTrue(moves.size() == 2176);

        //EVERY MOVE SHOULD BE UNIQUE
        for (int i = 0; i < moves.size(); i++) {
            for (int j = 0; j < moves.size(); j++) {
                if (i != j) {
                    if (moves.get(i).equals(moves.get(j))) {
                        fail();
                    }
                }
            }

        }


        moves = b.getAllMoves(BoardPieces.BLACK);
        assertTrue(moves.size() == 2176);

        //EVERY MOVE SHOULD BE UNIQUE
        for (int i = 0; i < moves.size(); i++) {
            for (int j = 0; j < moves.size(); j++) {
                if (i != j) {
                    if (moves.get(i).equals(moves.get(j))) {
                        fail();
                    }
                }
            }

        }
    }
}
