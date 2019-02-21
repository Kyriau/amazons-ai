package game.Players;

import game.datastructures.Board;
import game.datastructures.Move;

public interface IPlayer {


    /**
     *
     * @return The currently best move available as determined by the player
     */
    public Move getCurrentBestMove();
}
