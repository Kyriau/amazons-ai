package game.client;

import game.agents.Agent;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import ygraphs.ai.smart_fox.games.GameClient;
import ygraphs.ai.smart_fox.games.GamePlayer;

public class TextClient extends Client {

    private Scanner sc;

    private GameClient gaoClient;
    private GamePlayer gaoPlayer;

    private Agent agent;

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

            if(input.equals("login")) {
                login();
            } else if(input.equals("logout")) {
                logout();
            } else if(input.equals("rooms")) {
                rooms();
            } else if(input.equals("join")) {
                join();
            } else if(!input.equals("exit")) {
                System.out.println("Unrecognized Command.");
            }

        } while(!input.equals("exit"));

        exit();

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

    }

    private void logout() {

        if(gaoClient != null)
            gaoClient.logout();

        gaoClient = null;
        gaoPlayer = null;

        // Gao's code has some funky stuff where logout won't close an IO thread
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

    public String getCommands() {

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

    public void handleGameMessage(String messageType, Map<String,Object> msgDetails) {

        //TODO: Notify Agent.

    }

}