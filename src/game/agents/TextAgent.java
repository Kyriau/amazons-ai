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

        int fromRow = sc.nextInt();
        int fromCol = sc.nextInt();
        int toRow = sc.nextInt();
        int toCol = sc.nextInt();
        int arrowRow = sc.nextInt();
        int arrowCol = sc.nextInt();

        return new Move(fromRow, fromCol, toRow, toCol, arrowRow, arrowCol);

    }

    public void receiveMove(Move move) {

        System.out.println("Opponent has made a move.");
        System.out.println(move.toString());

    }

}