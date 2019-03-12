package game.agents;

import game.datastructures.Move;

public class DumbAgent extends Agent {

    public Move makeMove() {

        return new Move(1, 1, 2, 2, 3, 3);

    }

    public void receiveMove(Move move) {

        System.out.println(move.toString());

    }

    public void run() {

    }

}