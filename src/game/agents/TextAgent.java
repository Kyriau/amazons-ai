package game.agents;

import game.datastructures.Move;

import java.util.Scanner;

public class TextAgent extends Agent {

    private Scanner sc;

    public TextAgent() {

        sc = new Scanner(System.in);

    }

    public void run() {

        // Might want timing events here.
        // For now the TextAgent does nothing in it's thread.

    }

    public Move makeMove() {

        System.out.println("Make a move: [fromRow] [fromCol] [toRow] [toCol] [arrowRow] [arrowCol]");

        String line = sc.nextLine();
        String[] params = new String[6];

        int fromRow = Integer.parseInt(params[0]);
        int fromCol = Integer.parseInt(params[1]);
        int toRow = Integer.parseInt(params[2]);
        int toCol = Integer.parseInt(params[3]);
        int arrowRow = Integer.parseInt(params[4]);
        int arrowCol = Integer.parseInt(params[5]);

        System.out.println("Returning Move");

        return new Move(fromRow, fromCol, toRow, toCol, arrowRow, arrowCol);

    }

    public void receiveMove(Move move) {

        System.out.println("Opponent has made a move.");
        System.out.println(move.toString());

    }

}