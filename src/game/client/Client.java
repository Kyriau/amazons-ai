package game.client;

import game.agents.*;

import java.util.Map;

public abstract class Client implements Runnable {

    public abstract void handleGameMessage(String messageType, Map<String,Object> msgDetails);

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

        return sb.toString();

    }

    public static java.lang.Class parseAgent(String input) {

        if(input.equalsIgnoreCase(TextAgent.class.getSimpleName()))
            return TextAgent.class;
        else
            throw new IllegalArgumentException("Unknown input Agent class");

    }

    /**
     * Entry point for program.
     * @param args
     */
    public static void main(String[] args) {

        TextClient client = new TextClient();
        client.run();

    }

}