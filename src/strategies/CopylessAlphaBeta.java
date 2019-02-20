package strategies;
import game.Board;
import game.BoardPieces;
import game.Move;
import game.datastructures.MoveSequence;
import heuristics.IBoardValue;

import java.util.ArrayList;
import java.util.Stack;

/**
 * This class allows diving and coming back up at a low cost, meant for fast, efficient traversal.
 * Employs basic AlphaBeta
 */
public class CopylessAlphaBeta {

    private final Board originalBoard;

    private Board board;
    private int maxDepth;
    private int currentPlayersTurnColor;
    private IBoardValue boardValue;
    private Move bestMove;


    /**
     * Calling this function Will start the Alpha Beta search
     * @return a move that guarantees the best possible score given the search depth
     */
    public Move performSearch(){
        //Initialize
        refreshDataStructures();
        ArrayList<Move> moves = board.getAllMoves(currentPlayersTurnColor);
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double result;
        bestMove = null;
        int opponentTurn = BoardPieces.getColorCpposite(currentPlayersTurnColor);

        if(moves.isEmpty()){
            return null; //GAME OVER
        }

        //Continue while there are more moves to play
        for (Move a: moves) {
            result = alphaBetaLayer(a, 1, opponentTurn, alpha, beta);
            if(alpha < result){
                alpha = result;
                bestMove = a;
            }
        }

        return bestMove;
    }

    /**
     * Go one layer lower in the game tree by applying move m. When this function returns, the board state is the same as when it was called
     * @param m The move that will be applied to transition to that game state
     * @return Either the new alpha or beta value.
     */
    protected double alphaBetaLayer(Move m, int depth, int playerTurn, double alpha, double beta){
        board.playMove(m);
        if(depth == maxDepth){
            double value = boardValue.getBoardValueAsDouble(board);
            board.revertMove(m);
            return value;
        }
        double result;

        ArrayList<Move> moves = board.getAllMoves(playerTurn);
        int nextDepth = depth +1;
        int opponentTurn = BoardPieces.getColorCpposite(playerTurn);

        if(depth%2 == 0){//MAX LAYER
            for (Move a: moves) {
                result = alphaBetaLayer(a, nextDepth, opponentTurn, alpha, beta);
                if(alpha < result){
                    alpha = result;
                }
            }
            board.revertMove(m);
            return alpha;
        }else{//MIN LAYER
            for (Move a: moves) {
                result = alphaBetaLayer(a, nextDepth, opponentTurn, alpha, beta);
                if(beta > result){
                    beta = result;
                }
            }
            board.revertMove(m);
            return beta;
        }
    }

    /**
     *
     * @param b The initial board state
     * @param valueHeuristic How to evaluate the value of a leaf node
     * @param maxDepth The depth limit of the search
     * @param currentPlayersTurnColor Whose turn it is to move in the Board's initial state
     */
    public CopylessAlphaBeta(Board b, IBoardValue valueHeuristic, int maxDepth, int currentPlayersTurnColor){
        if(maxDepth < 1){
            throw new IllegalArgumentException("We should at least search one layer");
        }
        this.maxDepth = maxDepth;
        this.currentPlayersTurnColor = currentPlayersTurnColor;
        this.boardValue = valueHeuristic;
        originalBoard = b;
    }

    public void setMaxDepth(int maxDepth){
        if(maxDepth < 1){
            throw new IllegalArgumentException("We should at least search one layer");
        }
        this.maxDepth = maxDepth;
    }

    public int getMaxDepth(){
        return maxDepth;
    }

    private void refreshDataStructures(){
        board = Board.copyBoard(originalBoard);
    }

}
