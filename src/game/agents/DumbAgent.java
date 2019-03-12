package game.agents;

import game.datastructures.Move;

import java.util.Random;

public class DumbAgent extends Agent {

    private Random rand = new Random();

    public Move makeMove() {

        return new Move(
                rand.nextInt(10),
                rand.nextInt(10),
                rand.nextInt(10),
                rand.nextInt(10),
                rand.nextInt(10),
                rand.nextInt(10)
        );

    }

    public void receiveMove(Move move) {

        System.out.println("Move received: " + move.toString());

    }

    public void run() {

    }

}