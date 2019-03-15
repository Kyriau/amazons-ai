package game.client;

import game.agents.Agent;
import game.datastructures.Move;

public class GameTimer implements Runnable {

    private Agent agent;
    private Client client;

    private long time;

    public GameTimer(Agent agent, Client client) {
        this.agent = agent;
        this.client = client;
        time = 25000; // 25 seconds
    }

    public GameTimer(Agent agent, Client client, long time) {
        this.agent = agent;
        this.client = client;
        this.time = time;
    }

    public void run() {

        try {
            System.out.println("Waiting for " + time + " milliseconds.");
            Thread.sleep(time);
            System.out.println("Making Move");
            client.sendMove(agent.getBestMove());
            System.out.println("Move Made");
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

}