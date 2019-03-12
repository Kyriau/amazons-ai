package game.client;

import java.util.Map;

import ygraphs.ai.smart_fox.GameMessage;
import ygraphs.ai.smart_fox.games.GamePlayer;

/**
 * Wrapper class for Gao's GamePlayer. This should just receive game messages and pass them off.
 */
public class ClientPlayer extends GamePlayer {

    // These Strings are used as keys for msgDetails when messageType = GameMessage.GAME_ACTION_START
    public static final String GAME_STATE_STRING = "game-state";
    public static final String PLAYER_WHITE_STRING = "player-white";
    public static final String PLAYER_BLACK_STRING = "player-black";

    private String username;

    private Client client;

    public ClientPlayer(String username, Client client) {
        this.username = username;
        this.client = client;
    }

    public void onLogin() {

    }

    /**
     * IMPORTANT: This method must fully return before sendMoveMessage() can be called.
     */
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {

        System.out.println("Message received: " + messageType);

        // Pass data down to the client.
        client.handleGameMessage(messageType, msgDetails);

        System.out.println("Message handled.");

        // Assuming Gao expects that true means successfully handled.
        return true;

    }

    public String userName() {
        return username;
    }

}