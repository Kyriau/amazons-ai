package game.agents;

import game.datastructures.Move;

import java.util.Random;

public class DumbAgent extends Agent {

    private Random rand = new Random();

    @Override
    public Move getBestMove() {

        return new Move(
                rand.nextInt(10),
                rand.nextInt(10),
                rand.nextInt(10),
                rand.nextInt(10),
                rand.nextInt(10),
                rand.nextInt(10)
        );

    }

    @Override
    public void updateBoard(Move move) {

        System.out.println("Move received: " + move.toString());

    }

    @Override
    public void startSearch() {

    }

    @Override
    public void setAgentColor(int color) {

    }


    public void run() {

    }

}