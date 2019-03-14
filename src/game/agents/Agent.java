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

    /**
     * Get a string that lists all currently available Agents.
     * This method will need to be updated whenever we add new Agents.
     * @return A String listing all currently available Agents.
     */
    public static String getAgentList() {

        StringBuilder sb = new StringBuilder();

        sb.append("List of Agents\n");

        // List one Agent per line, tabbed once.
        //sb.append("\t" + TextAgent.class.getSimpleName() + "\n"); // Unused for now since it doesn't work
        //sb.append("\t" + CopylessAlphaBetaPlayer.class.getSimpleName() + "\n"); // Still needs work; see parseAgent()
        sb.append("\t" + DumbAgent.class.getSimpleName() + "\n");
        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();

    }

    /**
     * Get a subclass of Agent based on a String name.
     * @param name The name of the subclass of Agent to try to find.
     * @return The Agent found.
     */
    public static Agent parseAgent(String name) {

        if(name.equalsIgnoreCase(DumbAgent.class.getSimpleName()))
            return new DumbAgent();

        // Don't use, it doesn't work
        //if(name.equalsIgnoreCase(TextAgent.class.getSimpleName()))
            //return new TextAgent();

        //TODO: Instantiate CopylessAlphaBetaPlayer correctly
        //if(name.equalsIgnoreCase(CopylessAlphaBetaPlayer.class.getSimpleName()))
            //return new CopylessAlphaBetaPlayer();

        throw new IllegalArgumentException("Unknown Agent subclass.");

    }

}