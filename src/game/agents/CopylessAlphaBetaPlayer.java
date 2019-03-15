package game.agents;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import heuristics.ClosestToSquareHeuristic;
import heuristics.EqualityMoveHeuristic;
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

    private CopylessAlphaBeta currentMoveSearch;
    private int depth;


    public CopylessAlphaBetaPlayer(Board b, int playerColor, IMoveValueHeuristic moveValue, boolean useMoveHeuristic, IBoardValue boardValue){
        if(playerColor != BoardPieces.BLACK && playerColor != BoardPieces.WHITE){
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
        this.depth = 1;

    }

    /**
     * Empty for this player
     */
    @Override
    public void run() {

    }


    @Override
    public Move getBestMove() {
        return bestMove;
    }

    @Override
    public void updateBoard(Move move) {
        //Stop the current search
        System.out.println("updateBoard: Enter");
        if(currentMoveSearch != null) {
            currentMoveSearch.interrupt();
        }

        b.playMove(move);
        playerColor = BoardPieces.getColorCpposite(playerColor);
        depth = 1;
    }

    @Override
    public void startSearch(){
        depth = 1;
        searchAtDepth();
    }


    /**
     *
     * @param threadSearcher The object reference for the searcher
     * @param bestMove The best move by threadSearcher
     */
    public void giveSearchResult(CopylessAlphaBeta threadSearcher, Move bestMove){
        System.out.println("giveSearchResult: Enter");
        if(threadSearcher == currentMoveSearch){//Check it is the same object
            System.out.println("Completed Search at Depth: " + depth);
            this.bestMove = bestMove;
            searchNextDepth();
        }
    }

    /**
     * A function that acts as the recursion point for
     */
    private void searchNextDepth(){
        System.out.println("SearchNextDepth: Enter");
        depth += 1;
        searchAtDepth();
    }

    /**
     * This will complete the search and then call this classes "giveSearchResults"
     */
    private void searchAtDepth(){
        //Ensure current search terminates quickly
        if(currentMoveSearch != null) {
            currentMoveSearch.interrupt();
        }

        //Begin new search at depth
        currentMoveSearch = new CopylessAlphaBeta(b, boardValue, moveValue, useMoveHeuristic, depth, playerColor, this);
        Thread searchThread = new Thread(currentMoveSearch);
        System.out.println("SearchAtDepth: Starting New Thread");
        searchThread.start();
    }

    /**
     *
     * @param colorOfFirstToMove either "BLACK" or "WHITE"
     * @return a CopylessAlphaBetaPlayer with the starting Color
     */
    public static CopylessAlphaBetaPlayer buildDefault(String colorOfFirstToMove){
        if(colorOfFirstToMove.equalsIgnoreCase("WHITE")){
            return new CopylessAlphaBetaPlayer(new Board(),
                    BoardPieces.WHITE,
                    new EqualityMoveHeuristic(),
                    false,
                    new ClosestToSquareHeuristic(new Board()));
        }else if(colorOfFirstToMove.equalsIgnoreCase("BLACK")){
            return new CopylessAlphaBetaPlayer(new Board(),
                    BoardPieces.BLACK,
                    new EqualityMoveHeuristic(),
                    false,
                    new ClosestToSquareHeuristic(new Board()));
        }else{
            throw new IllegalArgumentException("No Such Color");
        }
    }
}
