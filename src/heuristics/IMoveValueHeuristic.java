package heuristics;

import game.datastructures.Board;
import game.datastructures.Move;

public interface IMoveValueHeuristic {

    /**
     *
     * @param b The Board that the move will be applied to
     * @param m The move to be applied
     * @return A rating of how good the move is in the context of the board b. Larger positive values are better.
     */
    public double getMoveValue(Board b, Move m);
}
