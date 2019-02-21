package heuristics;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class ClosestToSquareHeuristic implements IBoardValue{

    @Override
    public int getBoardValueAsInt(Board b, int playerTurn) {
        return (int) java.lang.Math.round(getBoardValueAsDouble(b, playerTurn));
    }

    /**
     *
     * @param b The board to be evaluated
     * @param playerTurn
     * @return The ratio of white squares over black squares. If White loses the negative infinity if Black loses then positive infinity
     */
    @Override
    public double getBoardValueAsDouble(Board b, int playerTurn) {
        if(!BoardPieces.isPlayerColor(playerTurn)){
            throw new IllegalArgumentException("No such player colour to indicate player's turn");
        }
        int[][] whiteLocations = b.getPieceLocations(BoardPieces.WHITE);
        int[][] blackLocations = b.getPieceLocations(BoardPieces.BLACK);
        int[][] whiteDistances = new int[b.numRows][b.numCols];
        int[][] blackDistances = new int[b.numRows][b.numCols];


        populateWithScores(b, whiteLocations, whiteDistances);
        populateWithScores(b, blackLocations, blackDistances);

        int whiteScore = 0;
        int blackScore = 0;

        //Calculate scores, no one gets a score for an unreached or contested location
        for(int i = 0; i < b.numRows; i++){
            for(int j = 0; j < b.numCols; j++){
                if(whiteDistances[i][j] > blackDistances[i][j]){
                    whiteScore += 1;
                }else if(whiteDistances[i][j] < blackDistances[i][j]){
                    blackScore += 1;
                }
            }
        }

        //If no moves for anyone then the person whose turn it is will lose
        if(whiteScore == 0 && blackScore == 0){
            if(playerTurn == BoardPieces.WHITE){
                return Double.NEGATIVE_INFINITY;
            }else{
                return Double.POSITIVE_INFINITY;
            }
        }
        if(whiteScore == 0 && blackScore > 0){
            return Double.NEGATIVE_INFINITY;
        }else if(whiteScore > 0 && blackScore == 0){
            return Double.POSITIVE_INFINITY;
        }else{
            return ((double)whiteScore/blackScore);
        }

    }

    private void populateWithScores( Board b, int[][] pieceLocations, int[][] distances){
        Queue<Location> frontier;
        Location loc;
        ArrayList<Location> validLocations;
        boolean[][] searched;


        for (int[] pieceLocation: pieceLocations) {
            frontier = new ArrayDeque<Location>(25);
            searched = new boolean[b.numRows][b.numCols]; // Searched for this piece before?
            searched[pieceLocation[0]][pieceLocation[1]] = true;
            frontier.add(new Location(pieceLocation[0], pieceLocation[1]));
            int distance = 1;
            while (!frontier.isEmpty()){
                validLocations = getMovesFromLocation(b, frontier.remove(), searched);
                for(Location candidate : validLocations){
                    if( distance < distances[candidate.row][candidate.col]){
                        distances[candidate.row][candidate.col] = distance;
                        frontier.add(candidate);
                    }
                }

                distance += 1;
            }
        }

    }

    private final Location[] offsets = new Location[]{
            new Location(-1, -1),
            new Location(-1, 0),
            new Location(-1, 1),
            new Location(0, 1),
            new Location(1, 1),
            new Location(1, 0),
            new Location(1, -1),
            new Location(0, -1)
    };

    /**
     * @param initialLocation The location to find all valid places to move a queen in one turn
     * @return The set of all possible moves the Queen specified at (row,col)
     */
    private ArrayList<Location> getMovesFromLocation(Board b, Location initialLocation, boolean[][] searched){
        ArrayList<Location> locs = new ArrayList<>(37);//Should be the maxiumum number of possible squares a queen could move to
        //For every direction we can move a queen
        for(Location offset : offsets){
            int rowIndex = offset.row + initialLocation.row;//move one position away from piece
            int colIndex = offset.col + initialLocation.col;
            //For every valid spot we can move a queen to
            while(0 <= rowIndex
                    && rowIndex < b.numRows
                    && 0 <= colIndex
                    && colIndex < b.numCols
                    && b.getPieceAt(rowIndex, colIndex) == BoardPieces.EMPTY){

                if(!searched[rowIndex][colIndex]){
                    locs.add(new Location(rowIndex, colIndex));
                }
                rowIndex += offset.row;
                colIndex += offset.col;
            }
        }
        return locs;
    }


    class Location{
        public int row;
        public int col;

        Location(int row, int col){
            this.row = row;
            this.col = col;
        }

    }
}
