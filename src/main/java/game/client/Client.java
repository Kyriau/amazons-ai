package game.client;

import game.datastructures.Move;

import java.util.Map;

public abstract class Client implements Runnable {

    public abstract void handleGameMessage(String messageType, Map<String, Object> msgDetails);

    public abstract void sendMove(Move move);

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
