package game;
import junit.framework.TestCase;
import org.junit.*;


public class BoardStateTest extends TestCase {

    @Test
    public void testPlayMoveAndRevertMove(){
        BoardState boardState1 = new BoardState();
        BoardState boardState2 = new BoardState();
        Move move = new Move(3,0, 5,0,5,9);

        //Initial states are the same
        assertTrue(boardState1.equals(boardState2));

        //Moving actually changes the board state
        boardState1.playMove(move);
        assertFalse(boardState1.equals(boardState2));

        //Doing the exact same move to board 2 produces the same result
        boardState2.playMove(move);
        assertTrue(boardState1.equals(boardState2));

        //Stepping back board 2 a state causes the state to become unequal
        assertTrue(boardState2.revertMove()); //There is a state to revert to
        assertFalse(boardState1.equals(boardState2));

        //Stepping back board 1 a state causes them to become equal again
        assertTrue(boardState1.revertMove()); //There is a state to revert to
        assertTrue(boardState1.equals(boardState2));

        //Ensuring that the boards are in the initial state
        assertTrue(boardState1.equals(new BoardState()));

        //Ensuring that we cannot revert back anymore moves
        assertFalse(boardState1.revertMove());
    }
}
