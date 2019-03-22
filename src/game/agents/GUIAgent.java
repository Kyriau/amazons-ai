package game.agents;

import game.datastructures.Move;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIAgent extends Agent implements ActionListener {

    private int phase;
    private int[] move = new int[6];
    private Move bestMove;

    @Override
    public void run() {

    }

    @Override
    public Move makeMove() {
        return bestMove;
    }

    @Override
    public void receiveMove(Move move) {

    }

    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        int x = command.charAt(0) - 48;
        int y = command.charAt(2) - 48;

        move[phase] = x;
        move[phase + 1] = y;

        if(phase == 4) {
            bestMove = new Move(
                    move[0],
                    move[1],
                    move[2],
                    move[3],
                    move[4],
                    move[5]
            );
            phase = 0;
        } else
            phase = phase + 2;

    }

}