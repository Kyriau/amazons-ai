package heuristics;
import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MobilityOrderingHeuristicTest {

    public static final double THRESHOLD = 0.0000000000001;

    private boolean threshEquals(double v1, double v2){
        return Math.abs(v1-v2) < THRESHOLD;
    }

    @Test
    public void getMoveValueTest(){
        MobilityOrderingHeuristic h = new MobilityOrderingHeuristic();
        Board b = new Board();

        assertTrue(h.getBoardValue(b, BoardPieces.WHITE) == h.getBoardValue(b, BoardPieces.BLACK));
        assertTrue(h.getBoardValue(b, BoardPieces.WHITE) == 1);



        Move m = new Move(3,0, 6,3, 3,0);
        assertTrue(threshEquals(h.getMoveValue(b,m), 66.0/51));


    }
}
