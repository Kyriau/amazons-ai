package heuristics;

import game.datastructures.Board;
import game.datastructures.Move;

import java.util.ArrayList;
import java.util.Arrays;

public class HeuristicUtilities {

    public HeuristicUtilities() {

    }

    /**
     * Gross, but needs to be done this way to be accessed statically
     *
     * @param moves The moves to be ordered
     * @param b     The current board
     * @param h     The movement heuristic
     * @return
     */
    public ArrayList<Move> orderMovesMethod(ArrayList<Move> moves, Board b, IMoveValueHeuristic h) {
        MoveValuePair[] pairs = new MoveValuePair[moves.size()];
        for (int i = 0; i < moves.size(); i += 1) {
            pairs[i] = new MoveValuePair(moves.get(i), h.getMoveValue(b, moves.get(i)));
        }
        //Sorts into ascending order
        Arrays.sort(pairs);

        //Put into a new arrayList in descending order
        ArrayList<Move> sortedMoves = new ArrayList<Move>(pairs.length);
        for(int i = pairs.length-1; i >=0; i -=1){
            sortedMoves.add(pairs[i].m);
        }
        return sortedMoves;
    }

    private class MoveValuePair implements Comparable<MoveValuePair> {
        Move m;
        double value;

        MoveValuePair(Move m, double value) {
            this.m = m;
            this.value = value;
        }

        @Override
        public int compareTo(MoveValuePair o) {
            if( this.value < o.value){
                return -1;
            }else if(this.value > o.value){
                return 1;
            }else{
                return 0;
            }
        }
    }
}