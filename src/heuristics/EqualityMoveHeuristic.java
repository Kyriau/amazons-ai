package heuristics;

import game.datastructures.Board;
import game.datastructures.Move;

/**
 * this class is a move ordering heuristic that will do nothing, or at most, depending on implementation, result in a semi random ordering
 */
public class EqualityMoveHeuristic implements IMoveValueHeuristic{

    @Override
    public double getMoveValue(Board b, Move m) {
        return 0;
    }
}
