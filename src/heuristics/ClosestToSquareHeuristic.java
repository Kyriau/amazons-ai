package heuristics;

import game.Board;

public class ClosestToSquareHeuristic implements IBoardValue{

    @Override
    public int getBoardValueAsInt(Board b) {
        return (int) getBoardValueAsDouble(b);
    }

    @Override
    public double getBoardValueAsDouble(Board b) {
        return 0;
    }
}
