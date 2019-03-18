package heuristics;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ClosestToSquareHeuristicTest {
    public static final double THRESHOLD = 0.0000000000001;

    @Test
    public void testBoardValues(){
        Board b = new Board();
        IBoardValue bv = new ClosestToSquareHeuristic(b);
        assertTrue(threshEquals(bv.getBoardValue(b, BoardPieces.WHITE),bv.getBoardValue(b, BoardPieces.BLACK)));
       // System.out.println(bv.getBoardValue(b, BoardPieces.WHITE));

        Move toCenter = new Move(0,3,4,3,8,3);
        b.playMove(toCenter);
        assertTrue(threshEquals(bv.getBoardValue(b, BoardPieces.WHITE), 1.5));
        //System.out.println(bv.getBoardValue(b, BoardPieces.WHITE));

        assertTrue(threshEquals(bv.getBoardValue(b, BoardPieces.BLACK), 2.0/3.0));
       // System.out.println(bv.getBoardValue(b, BoardPieces.BLACK));
       // System.out.println(b);

    }

    private boolean threshEquals(double v1, double v2){
        return Math.abs(v1-v2) < THRESHOLD;
    }
}
