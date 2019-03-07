package game.agents;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import heuristics.IBoardValue;
import heuristics.IMoveValueHeuristic;
import strategies.CopylessAlphaBeta;


/**
 *
 */
public class CopylessAlphaBetaPlayer extends Agent {

    private Move bestMove;
    private Board b;
    private int playerColor;
    IMoveValueHeuristic moveValue;
    IBoardValue boardValue;
    boolean useMoveHeuristic;


    public CopylessAlphaBetaPlayer(Board b, int playerColor, IMoveValueHeuristic moveValue, boolean useMoveHeuristic, IBoardValue boardValue){
        if(playerColor != BoardPieces.BLACK || playerColor != BoardPieces.WHITE){
            throw new IllegalArgumentException("No such player colour");
        }
        if(boardValue == null) {
            throw new IllegalArgumentException("Must have some way of evaluating the board");
        }
        if(moveValue == null && useMoveHeuristic == false){
            throw new IllegalArgumentException("No move heuristic is specified, but you desire to use a move heuristic");
        }
        this.b = Board.copyBoard(b);
        this.playerColor = playerColor;
        this.moveValue = moveValue;
        this.useMoveHeuristic = useMoveHeuristic;
        this.boardValue = boardValue;

    }

    /**
     * nce this method is invoked it will continue searching for ever until it is killed from the outside
     */
    @Override
    public void run() {
        int depth = 1;
        CopylessAlphaBeta alphaBetaSearch;

        //Loop until the thread is killed from the outside
        while(true){
            alphaBetaSearch = new CopylessAlphaBeta(b, boardValue, moveValue, useMoveHeuristic, depth, playerColor);
            bestMove  = alphaBetaSearch.performSearch();
            depth+=1;
        }



    }

    @Override
    public Move makeMove() {
        return bestMove;
    }

    public void receiveMove(Move move) {
        //TODO: Update board, restart heuristic searches
    }
}
