package heuristics;

import game.datastructures.Board;

public interface IBoardValue {

    /**
     *
     * @param b The board to be evaluated
     * @return The board value from the perspective of White as defined by this heuristic
     */
    public int getBoardValueAsInt(Board b, int playerTurn);

    /**
     *
     * @param b The board to be evaluated
     * @return The board value from the perspective of white as defined by this heuristic
     */
    public double getBoardValueAsDouble(Board b, int playerTurn);
}
