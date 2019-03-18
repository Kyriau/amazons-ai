package game.agents;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import heuristics.*;
import strategies.SelectiveSearch;


/**
 *
 */
public class SelectivePlayer extends Agent {

    private Move bestMove;
    private Board b;
    private int playerColor;
    private double selectivityCoeff;
    IMoveValueHeuristic moveValue;
    IBoardValue boardValue;
    boolean useMoveHeuristic;

    private SelectiveSearch currentMoveSearch;
    private int depth;
    private int turn;


    public SelectivePlayer(Board b, int playerColor, IMoveValueHeuristic moveValue, IBoardValue boardValue, double selectivityCoeff){
        if(playerColor != BoardPieces.BLACK && playerColor != BoardPieces.WHITE){
            throw new IllegalArgumentException("No such player colour");
        }
        if(boardValue == null) {
            throw new IllegalArgumentException("Must have some way of evaluating the board");
        }
        if(moveValue == null && useMoveHeuristic == true){
            throw new IllegalArgumentException("No move heuristic is specified, but you desire to use a move heuristic");
        }
        this.b = Board.copyBoard(b);
        this.playerColor = playerColor;
        this.moveValue = moveValue;
        this.useMoveHeuristic = useMoveHeuristic;
        this.boardValue = boardValue;
        this.depth = 1;
        this.selectivityCoeff = selectivityCoeff;
        turn = 1;

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
        //System.out.println("updateBoard: Enter");
        turn += 1;
        System.out.println("Agent "+ this + " Color = " + playerColor);
        if(currentMoveSearch != null) {
            currentMoveSearch.interrupt();
        }

        b.playMove(move);
        //playerColor = BoardPieces.getColorCpposite(playerColor);
        depth = 1;
    }

    @Override
    public void startSearch(){
        depth = 1;
        searchAtDepth();
    }

    @Override
    public void setAgentColor(int color) {
        if(playerColor != BoardPieces.BLACK && playerColor != BoardPieces.WHITE){
            throw new IllegalArgumentException("No such player colour");
        }
        this.playerColor = color;
    }

    @Override
    public int getAgentColor(){
        return playerColor;
    }


    /**
     *
     * @param threadSearcher The object reference for the searcher
     * @param bestMove The best move by threadSearcher
     */
    public void giveSearchResult(SelectiveSearch threadSearcher, Move bestMove){
        //System.out.println("giveSearchResult: Enter");
        if(threadSearcher == currentMoveSearch){//Check it is the same object
            System.out.println("Completed Search at Depth: " + depth);
            this.bestMove = bestMove;
            if(bestMove == null){
                return;//No moves left in search. We lost
            }
            if(depth < 100) {
                searchNextDepth();
            }
        }
    }

    /**
     * A function that acts as the recursion point for
     */
    private void searchNextDepth(){
        //System.out.println("SearchNextDepth: Enter");
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
        //It is necessairy to copyMoveHeuristic boardValue or else there are access conflicts between threads
        /*
        if(turn*0.015 + 0.10  < 0.5) {
            if(turn < 10) {
                currentMoveSearch = new SelectiveSearch(b, boardValue.copy(), new MobilityOrderingHeuristic(), depth, playerColor, this, selectivityCoeff);
            }else{
                currentMoveSearch = new SelectiveSearch(b, boardValue.copy(), moveValue.copyMoveHeuristic(), depth, playerColor, this, selectivityCoeff);
            }
        }else{
            currentMoveSearch = new SelectiveSearch(b, boardValue.copy(), moveValue.copyMoveHeuristic(), depth, playerColor, this, 1);
        }*/

        currentMoveSearch = new SelectiveSearch(b, boardValue.copy(), moveValue.copyMoveHeuristic(), depth, playerColor, this, 1);

        Thread searchThread = new Thread(currentMoveSearch);
        //System.out.println("SearchAtDepth: Starting New Thread");
        searchThread.start();
    }

    /**
     *
     * @param colorOfFirstToMove either "BLACK" or "WHITE"
     * @return a CopylessAlphaBetaPlayer with the starting Color
     */
    public static SelectivePlayer buildDefault(String colorOfFirstToMove){
        if(colorOfFirstToMove.equalsIgnoreCase("WHITE")){
            return new SelectivePlayer(new Board(),
                    BoardPieces.WHITE,
                    new MobilityOrderingHeuristic(),
                    new ClosestToSquareHeuristic(),
                    0.1);
        }else if(colorOfFirstToMove.equalsIgnoreCase("BLACK")){
            return new SelectivePlayer(new Board(),
                    BoardPieces.BLACK,
                    new MobilityOrderingHeuristic(),
                    new ClosestToSquareHeuristic(),
                    0.1);
        }else{
            throw new IllegalArgumentException("No Such Color");
        }
    }
}
