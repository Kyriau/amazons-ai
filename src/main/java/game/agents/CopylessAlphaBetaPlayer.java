package game.agents;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import heuristics.*;
import strategies.CopylessAlphaBeta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.out;


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
    private int turn;
    private ArrayList<Move> allMovesPlayed= new ArrayList<>(92);

    private long startTime = 0;


    public CopylessAlphaBetaPlayer(Board b, int playerColor, IMoveValueHeuristic moveValue, boolean useMoveHeuristic, IBoardValue boardValue){
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
        turn = 1;
        startTime = System.currentTimeMillis();
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
        out.println("Agent "+ this + " Color = " + playerColor);
        if(currentMoveSearch != null) {
            currentMoveSearch.interrupt();
        }

        outputMoveText(move);

        b.playMove(move);
        allMovesPlayed.add(move);
        turn+=1;
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
    public void giveSearchResult(CopylessAlphaBeta threadSearcher, Move bestMove){
        //System.out.println("giveSearchResult: Enter");
        if(threadSearcher == currentMoveSearch){//Check it is the same object
            out.println("Completed Search at Depth: " + depth);
            this.bestMove = bestMove;
            if(bestMove == null){
                super.setGameOver(true);
                return;//No moves left in search. We lost
            }
            if(depth < 30) {
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
        if(turn <= 12) {//12 is optimal, black manages a close victory, found empirically
            currentMoveSearch = new CopylessAlphaBeta(b, boardValue.copy(), moveValue.copyMoveHeuristic(), useMoveHeuristic, depth, playerColor, this);
        }else{//Ideally, this should be done when we ca tell the board has only isolated player cells.
            //Currently testing to see if ordering by closestToSquareHeuristic is optimal
            currentMoveSearch = new CopylessAlphaBeta(b, new ClosestToSquareHeuristic(), moveValue.copyMoveHeuristic(), useMoveHeuristic, depth, playerColor, this);
        }


        Thread searchThread = new Thread(currentMoveSearch);
        //System.out.println("SearchAtDepth: Starting New Thread");
        searchThread.start();
    }

    /**
     *
     * @return An array of the moves sequentially made up to this point
     */
    public Move[] moveHistory(){
        return allMovesPlayed.toArray(new Move[allMovesPlayed.size()]);
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
                    new MobilityOrderingHeuristic(),//Mobility Heuristic
                    true,
                    new MobilityTerritory());
        }else if(colorOfFirstToMove.equalsIgnoreCase("BLACK")){
            return new CopylessAlphaBetaPlayer(new Board(),
                    BoardPieces.BLACK,
                    new MobilityOrderingHeuristic(),//Mobility Heuristic
                    true,
                    new MobilityTerritory());
        }else{
            throw new IllegalArgumentException("No Such Color");
        }
    }

    private void outputMoveText(Move move) {

        try {
            File logDir = new File(System.getProperty("user.dir") + File.separator + "logs");
            if(!logDir.isDirectory()){
                logDir.mkdir();
            }

            File logFile = new File(logDir.getPath() + File.separator + "moves_" + startTime + ".txt");
            FileOutputStream out;
            if(!logFile.exists()){
                logFile.createNewFile();
                out = new FileOutputStream(logFile);
            }else{
                out = new FileOutputStream(logFile, true);
            }
            out.write((move.toString() + '\n').getBytes());
            out.flush();
            out.close();
            System.out.println("Appended move to : " + logFile);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

}
