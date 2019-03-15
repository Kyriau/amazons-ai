package heuristics;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import org.junit.Test;

public class ClosestToSquareHeuristicTest {

    @Test
    public void testBoardValues(){
        Board b = new Board();
        IBoardValue bv = new ClosestToSquareHeuristic(b);
        System.out.println(bv.getBoardValueAsDouble(b, BoardPieces.WHITE));

        Move toCenter = new Move(0,3,4,3,8,3);
        b.playMove(toCenter);
        System.out.println(bv.getBoardValueAsDouble(b, BoardPieces.WHITE));
        System.out.println(b);

    }
}
