package game.client;

import game.agents.Agent;
import game.agents.DumbAgent;
import game.datastructures.Move;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import ygraphs.ai.smart_fox.GameMessage;
import ygraphs.ai.smart_fox.games.AmazonsGameMessage;
import ygraphs.ai.smart_fox.games.GameClient;
import ygraphs.ai.smart_fox.games.GamePlayer;

public class TextClient extends Client {

    private Scanner sc;

    private GameClient gaoClient;
    private GamePlayer gaoPlayer;

    private Agent agent;

    private Thread delay;
    private GameTimer timer;

    private int turnOrder;
    private int turnCount;

    private Move nextMove;

    public TextClient() {

        sc = new Scanner(System.in);

    }

    public void run() {

        System.out.println("Text client for CoSc 322's Amazon's AI project");
        System.out.println("Group 5 - Jeff, Daniel, Kyle, Devon");
        System.out.println();

        String input;

        do {

            System.out.print(getCommands());

            input = sc.nextLine();

            if(input.equalsIgnoreCase("login")) {
                login();
            } else if(input.equalsIgnoreCase("logout")) {
                logout();
            } else if(input.equalsIgnoreCase("rooms")) {
                rooms();
            } else if(input.equalsIgnoreCase("join")) {
                join();
            } else if(!input.equalsIgnoreCase("exit")) {
                System.out.println("Unrecognized Command.");
            }

        } while(!input.equals("exit"));

        exit();

    }

    private String getCommands() {

        StringBuilder sb = new StringBuilder();

        sb.append("List of Commands\n");
        if(gaoClient == null)
            sb.append("\tlogin\t- Connect to Gao's server.\n");
        else {
            sb.append("\tlogout\t- Disconnect from Gao's server.\n");
            sb.append("\trooms\t- List the Amazons rooms available.\n");
            sb.append("\tjoin\t- Join an Amazons room.\n");
        }
        sb.append("\texit\t- Exit program.\n");

        return sb.toString();

    }

    private void login() {

        logout();

        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        // Removing the ClientPlayer and using the other constructor ends up throwing some NullPointerException
        gaoPlayer = new ClientPlayer(username, this);
        gaoClient = new GameClient(username, password, gaoPlayer);

        //TODO: dynamic agent choice
        agent = new DumbAgent();
        timer = new GameTimer(agent, this);

    }

    private void logout() {

        if(gaoClient != null)
            gaoClient.logout();

        gaoClient = null;
        gaoPlayer = null;

        agent = null;

        // Gao's code has some funky stuff where his gaoClient.logout() won't close an IO thread
        // This means that sometimes all our code may finish running, but the JVM won't close.

    }

    private void rooms() {

        if(gaoClient == null) {
            System.out.println("Must be logged in to view rooms.");
            return;
        }

        ArrayList<String> rooms = gaoClient.getRoomList();

        if(rooms.size() == 0)
            System.out.println("There are currently no rooms available.");

        for(String s : rooms) {
            System.out.println(s);
        }

    }

    private void join() {

        System.out.print("Join room: ");
        String room = sc.nextLine();

        gaoClient.joinRoom(room);

    }

    private void exit() {

        logout();

        // Stop any other threads too

    }

    public void handleGameMessage(String messageType, Map<String,Object> msgDetails) {

        if(messageType.equals(GameMessage.GAME_ACTION_START)) {

            // White is "supposed" to play first, but Gao at some point said Black will go first
            String blackName = (String) msgDetails.get(ClientPlayer.PLAYER_BLACK_STRING);
            if(blackName.equals(gaoPlayer.userName())) {
                turnOrder = 0;
                startTimer();
            } else {
                turnOrder = 1;
            }

        } else if(messageType.equals(GameMessage.GAME_ACTION_MOVE)) {

            ArrayList<Integer> from = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.QUEEN_POS_CURR);
            ArrayList<Integer> to = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.Queen_POS_NEXT);
            ArrayList<Integer> arrow = (ArrayList<Integer>) msgDetails.get(AmazonsGameMessage.ARROW_POS);

            Move move = new Move(
                    from.get(0),
                    from.get(1),
                    to.get(0),
                    to.get(1),
                    arrow.get(0),
                    arrow.get(1)
            );

            agent.receiveMove(move);

            startTimer();

        }

    }

    private void startTimer() {
        //delay.interrupt();
        delay = new Thread(timer);
        delay.start();
    }

    /**
     * Send a move to Gao's server.
     * @param move The move to send.
     */
    public void sendMove(Move move) {
        System.out.println("Sending Move: " + move.toString());
        gaoClient.sendMoveMessage(
                new int[] {move.startRow, move.startCol},
                new int[] {move.endRow, move.endCol},
                new int[] {move.arrowRow, move.arrowCol}
        );
    }

}