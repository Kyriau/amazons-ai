package game.agents;

import game.datastructures.Move;

/**
 * An agent that plays the Amazons game.
 * Any processing of data (minmax, heuristics) should be done using this objects run method.
 * A new Thread object should be created, with an Agent in it's constructor, and started.
 */
public abstract class Agent implements Runnable {

    /**
     * Get the information for this Agent's next move.
     * This method will be called by the Client when time for making a move has elapsed.
     * This method could be called at any time, and must return a valid move.
     * @return The move that this Agent makes.
     */
    public abstract Move makeMove();

    /**
     * Handle receiving a move from the other player.
     * This method will be called whenever the Client receives an opponent's move from the server.
     * This method could be called at any time.
     * @param move The move that has been made by the other player.
     */
    public abstract void receiveMove(Move move);

}