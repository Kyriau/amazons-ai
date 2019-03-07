package client;

import java.util.Map;
import java.util.Scanner;

import ygraphs.ai.smart_fox.games.GameClient;

public class TextClient extends Client {

    private Scanner sc;

    private GameClient gaoClient;

    public TextClient() {

        sc = new Scanner(System.in);

    }

    public void run() {

        System.out.println("Text client for CoSc 322's Amazon's AI project.");
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
            } else if(!input.equals("exit")) {
                System.out.println("Unrecognized Command.");
            }

        } while(!input.equals("exit"));

        exit();

    }

    private void login() {

        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        // Removing the ClientPlayer and using the other constructor ends up throwing some NullPointerException
        gaoClient = new GameClient(username, password, new ClientPlayer(username, this));

    }

    private void logout() {

        if(gaoClient != null)
            gaoClient.logout();
        gaoClient = null;

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
        else
            sb.append("\tlogout\t- Disconnect from Gao's server.\n");
        sb.append("\texit\t- Exit program.\n");

        return sb.toString();

    }

    public void handleGameMessage(String messageType, Map<String,Object> msgDetails) {

        //TODO: Notify Agent.

    }

}