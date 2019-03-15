package strategies;
import game.agents.CopylessAlphaBetaPlayer;
import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import heuristics.ClosestToSquareHeuristic;
import heuristics.EqualityMoveHeuristic;
import junit.framework.TestCase;
import org.junit.*;


public class CopylessAlphaBetaTest extends TestCase {

    @Test
    public void testPlayMoveAndRevertMove(){

    }

    @Test
    public void testDepth1(){

        CopylessAlphaBeta whitePlayer = new CopylessAlphaBeta(
                new Board(),
                new ClosestToSquareHeuristic(new Board()),
                null,
                false,
                1,
                BoardPieces.WHITE,
                null
        );

        Move whiteMove = whitePlayer.performSearch();

        CopylessAlphaBeta blackPlayer = new CopylessAlphaBeta(
                new Board(),
                new ClosestToSquareHeuristic(new Board()),
                null,
                false,
                1,
                BoardPieces.BLACK,
                null
        );

        Move blackMove = blackPlayer.performSearch();

        System.out.println(whiteMove);
        System.out.println(blackMove);

    }

    @Test
    public void testDepth2(){

        CopylessAlphaBeta whitePlayer = new CopylessAlphaBeta(
                new Board(),
                new ClosestToSquareHeuristic(new Board()),
                null,
                false,
                2,
                BoardPieces.WHITE,
                null
        );

        Move whiteMove = whitePlayer.performSearch();

        CopylessAlphaBeta blackPlayer = new CopylessAlphaBeta(
                new Board(),
                new ClosestToSquareHeuristic(new Board()),
                null,
                false,
                2,
                BoardPieces.BLACK,
                null
        );

        Move blackMove = blackPlayer.performSearch();

        System.out.println(whiteMove);
        System.out.println(blackMove);

    }
}
