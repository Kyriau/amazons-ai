package game.client;

import java.util.Map;

import ygraphs.ai.smart_fox.games.GamePlayer;

/**
 * Wrapper class for Gao's GamePlayer. This should just receive game messages and pass them off.
 */
public class ClientPlayer extends GamePlayer {

    private String username;

    private Client client;

    public ClientPlayer(String username, Client client) {
        this.username = username;
        this.client = client;
    }

    public void onLogin() {

    }

    public boolean handleGameMessage(String messageType, Map<String,Object> msgDetails) {

        // Just pass data down to the client.
        client.handleGameMessage(messageType, msgDetails);

        // Assuming Gao expects that true means successfully handled.
        return true;

    }

    public String userName() {
        return username;
    }

}