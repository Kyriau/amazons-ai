package game.client;

import game.agents.*;
import game.datastructures.Move;

import java.util.Map;

public abstract class Client implements Runnable {

    public abstract void handleGameMessage(String messageType, Map<String, Object> msgDetails);

    public abstract void sendMove(Move move);

    /**
     * Get a string that lists all currently available Agents.
     * This method will need to be updated whenever we add new Agents.
     * @return A String listing all currently available Agents.
     */
    public static String getAgentList() {

        StringBuilder sb = new StringBuilder();

        sb.append("List of Agents\n");

        // List one Agent per line, tabbed once.
        sb.append("\t" + TextAgent.class.getSimpleName() + "\n");
        sb.append("\t" + CopylessAlphaBetaPlayer.class.getSimpleName() + "\n");
        sb.append("\t" + DumbAgent.class.getSimpleName() + "\n");

        return sb.toString();

    }

    /**
     * Get a subclass of Agent based on a String name.
     * @param name The name of the subclass of Agent to try to find.
     * @return The subclass of Agent found.
     */
    public static java.lang.Class parseAgent(String name) {

        if(name.equalsIgnoreCase(TextAgent.class.getSimpleName()))
            return TextAgent.class;
        else if(name.equalsIgnoreCase(CopylessAlphaBetaPlayer.class.getSimpleName()))
            return CopylessAlphaBetaPlayer.class;
        else if(name.equalsIgnoreCase(DumbAgent.class.getSimpleName()))
            return DumbAgent.class;
        else
            throw new IllegalArgumentException("Unknown input Agent subclass.");

    }

    /**
     * Entry point for program.
     * @param args Arguments. Currently unused.
     */
    public static void main(String[] args) {

        TextClient client = new TextClient();
        Thread main = new Thread(client);
        main.start();

    }

}
