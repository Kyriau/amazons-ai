package heuristics;

import game.datastructures.Board;

public interface IBoardValue {


    /**
     *
     * @param b The board to be evaluated
     * @return The board value from the perspective of white as defined by this heuristic
     */
    public double getBoardValue(Board b, int playerTurn);

    /**
     * Returns a deep copyMoveHeuristic that will work on the same board as the original
     */
    public IBoardValue copy();
}
