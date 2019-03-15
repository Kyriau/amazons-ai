package game.client;

import game.agents.Agent;
import game.agents.DumbAgent;
import game.datastructures.Board;
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
    private Board b; //The current state of the game

    private Thread delay;
    private GameTimer timer;

    private GameWindow window;

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

            if(input.startsWith("login")) {
                login();
            } else if(input.startsWith("logout")) {
                logout();
            } else if(input.startsWith("rooms")) {
                rooms();
            } else if(input.startsWith("join")) {
                join();
            } else if(input.startsWith("agent")) {
                agent();
            } else if(input.startsWith("timer")) {
                timer();
            } else if(!input.startsWith("exit")) {
                System.out.println("Unrecognized Command.");
            }

        } while(!input.startsWith("exit"));

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
            sb.append("\tagent\t- Set the agent used to play.\n");
            sb.append("\ttimer\t- Set the speed to play at.\n");
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

        // Default agent: makes random invalid moves.
        agent = new DumbAgent();

        // Default timer: waits 25 seconds before making a move.
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

    private void agent() {

        System.out.println(Agent.getAgentList());
        String line = sc.nextLine();

        agent = Agent.parseAgent(line);

        timer.setAgent(agent);

    }

    private void timer() {

        System.out.print("Set timer length in milliseconds: ");
        timer.setTime(sc.nextLong());

    }

    private void exit() {

        logout();

        // Stop any other threads too

    }

    public void handleGameMessage(String messageType, Map<String,Object> msgDetails) {

        if(messageType.equals(GameMessage.GAME_ACTION_START)) {
            //Get Agent Running
            Thread agentThread = new Thread(agent);
            agentThread.start();

            // White is "supposed" to play first, but Gao has Black playing first
            String blackName = (String) msgDetails.get(ClientPlayer.PLAYER_BLACK_STRING);
            if(blackName.equals(gaoPlayer.userName())) {
                agent.startSearch();
                startTimer();
            }

            window = new GameWindow();

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

            agent.updateBoard(move);
            window.playMove(move);

            startTimer();
            agent.startSearch();

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
        window.playMove(move);
        agent.updateBoard(move);
    }

}