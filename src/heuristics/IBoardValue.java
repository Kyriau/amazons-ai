package heuristics;

import game.Board;

public interface IBoardValue {

    public int getBoardValueAsInt(Board b);

    public double getBoardValueAsDouble(Board b);
}
