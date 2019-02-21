package strategies;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import heuristics.IBoardValue;
import heuristics.IMoveValueHeuristic;

import java.util.ArrayList;
import java.util.Arrays;

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
    private IMoveValueHeuristic moveValue;
    private Move bestMove;
    private boolean useMoveHeuristic; // If false, do not apply move heuristic


    /**
     * Calling this function will start the Alpha Beta search
     * @return a move that guarantees the best possible score given the search depth, if null, the game is over
     */
    public Move performSearch(){
        //Initialize
        refreshDataStructures();
        ArrayList<Move> moves = board.getAllMoves(currentPlayersTurnColor);
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double result;
        int opponentTurn = BoardPieces.getColorCpposite(currentPlayersTurnColor);

        if(moves.isEmpty()){
            return null; //GAME OVER
        }
        //Comment out this line to ignore heuristic and get speed up. Do not forget the same line in the other method
        if(useMoveHeuristic) {
            moves = orderMovesByHeuristic(moves);
        }
        bestMove = moves.get(0);//Set equal to the first move arbitrarily

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
        ArrayList<Move> moves = board.getAllMoves(playerTurn);
        //COMMENT OUT LINE BELOW IF HEURISTIC NOT IN USE
        if(useMoveHeuristic) {
            moves = orderMovesByHeuristic(moves);
        }
        //Check for game end
        if(moves.isEmpty()){
            if(depth%2 == 0){ // MAX LAYER
                return Double.NEGATIVE_INFINITY;
            }else{ // MIN LAYER
                return Double.POSITIVE_INFINITY;
            }
        }

        if(depth == maxDepth){
            double value = boardValue.getBoardValueAsDouble(board);
            board.revertMove(m);
            return value;
        }
        double result;

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
     * @param boardValueHeuristic How to evaluate the value of a leaf node
     * @param maxDepth The depth limit of the search
     * @param currentPlayersTurnColor Whose turn it is to move in the Board's initial state
     */
    public CopylessAlphaBeta(Board b,
                             IBoardValue boardValueHeuristic,
                             IMoveValueHeuristic moveValueHeuristic,
                             boolean useMoveHeuristic,
                             int maxDepth,
                             int currentPlayersTurnColor){

        if(maxDepth < 1){
            throw new IllegalArgumentException("We should at least search one layer");
        }
        this.maxDepth = maxDepth;
        this.currentPlayersTurnColor = currentPlayersTurnColor;
        this.boardValue = boardValueHeuristic;
        this.moveValue = moveValueHeuristic;
        this.useMoveHeuristic = useMoveHeuristic;
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

    /**
     *
     * @param moves The moves to be ordered by the heuristic passed into the constructor
     * @return The moves in descending order as specified by the heuristic
     */
    public ArrayList<Move> orderMovesByHeuristic(ArrayList<Move> moves){
        MoveValuePair[] mvPairs = new MoveValuePair[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            mvPairs[i] = new MoveValuePair(moves.get(i), moveValue.getMoveValue(board, moves.get(i)));
        }
        Arrays.sort(mvPairs);
        moves = new ArrayList<Move>(mvPairs.length);
        for (MoveValuePair mvp : mvPairs ) {
            moves.add(mvp.m);
        }
        return moves;
    }

    class MoveValuePair implements Comparable<MoveValuePair>{

        Move m;
        double moveValue;

        public MoveValuePair(Move m, double moveValue){
            if(m == null){
                throw new IllegalArgumentException("it is expected that we are only sorting actual potential moves.");
            }
             this.m  = m;
             this.moveValue = moveValue;
        }

        @Override
        public int compareTo(MoveValuePair o) {
            if(moveValue < o.moveValue){
                return -1;
            }else if(moveValue > o.moveValue){
                return 1;
            }else{
                return 0;
            }
        }
    }

}
