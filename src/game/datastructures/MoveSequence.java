package game.datastructures;

import game.Move;

import java.util.ArrayList;


/**
 * a basic and fast class to help keep track of what moves we have tried for a certain note at a certain depth
 */
public class MoveSequence {

    private ArrayList<Move> moveSequence;
    private int pos;

    public MoveSequence(ArrayList<Move> moveSequence){
        if(moveSequence == null || moveSequence.size() ==0){
            throw new IllegalArgumentException("Null or empty move sequences are not allowed");
        }
        moveSequence = moveSequence;
        pos = -1;
    }

    public Move getNextMove(){
        pos+= 1;
        return moveSequence.get(pos);
    }

    public boolean hasAnotherMove(){
        return pos < moveSequence.size();
    }


}
