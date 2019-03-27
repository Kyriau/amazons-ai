package heuristics;

import game.datastructures.Board;
import game.datastructures.BoardPieces;

/**
 * More likely to fill areas optimally
 */
public class DepthHeuristic implements IBoardValue {

    @Override
    public double getBoardValue(Board b, int playerTurn) {
        int score = 0;
        int nRows = b.numRows;
        int nCols = b.numCols;
        for(int i = 0; i < nRows; i+=1){
            for(int j = 0; j < nCols; j+=1){
                if(b.getPieceAt(i,j) == BoardPieces.EMPTY){
                    score += 1;
                }
            }
        }

        return score;
    }

    @Override
    public IBoardValue copy() {
        return new DepthHeuristic();
    }
}
