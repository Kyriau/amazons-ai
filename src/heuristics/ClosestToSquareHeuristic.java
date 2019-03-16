package heuristics;

import game.datastructures.Board;
import game.datastructures.BoardPieces;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class ClosestToSquareHeuristic implements IBoardValue{


    private final int[][] whiteDistances;//Will be used to hold the number of moves in the shortest path to a square, zero indicates no path
    private final int[][] blackDistances;//Will be used to hold the number of moves in the shortest path to a square, zero indicates no path
    private final boolean[][] searched;//For a given piece, this array will be used to ensure we do not re-search locations
    private final Queue<Location> frontier;//This is a standard breadth first search queue

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
     *
     * @param targetBoard This is just used to initialize the number of rows and columns expected, it is not held on to her altered
     */
    public ClosestToSquareHeuristic(Board targetBoard){
        whiteDistances = new int[targetBoard.numRows][targetBoard.numCols];
        blackDistances = new int[targetBoard.numRows][targetBoard.numCols];
        searched = new boolean[targetBoard.numRows][targetBoard.numCols];
        frontier = new ArrayDeque<Location>(100);
    }

    private ClosestToSquareHeuristic(int numRow, int numCol){
        whiteDistances = new int[numRow][numCol];
        blackDistances = new int[numRow][numCol];
        searched = new boolean[numRow][numCol];
        frontier = new ArrayDeque<Location>(100);
    }

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
        reinitializeToMax(whiteDistances);
        reinitializeToMax(blackDistances);


        populateWithScores(b, whiteLocations, whiteDistances);
        populateWithScores(b, blackLocations, blackDistances);

        int whiteScore = 0;
        int blackScore = 0;

        //Calculate scores, no one gets a score for an unreached or contested location
        for(int i = 0; i < b.numRows; i++){
            for(int j = 0; j < b.numCols; j++){
                if(whiteDistances[i][j] < blackDistances[i][j]){
                    whiteScore += 1;
                }else if(whiteDistances[i][j] > blackDistances[i][j]){
                    blackScore += 1;
                }
            }
        }
        //System.out.println("Player turn = " + playerTurn + ", whiteScore = " + whiteScore + ", blackScore = " + blackScore);
        //Convert Score to ratio
        if(playerTurn == BoardPieces.WHITE){
            if(whiteScore == 0 && blackScore == 0){
                //System.out.println("Exit: White 1");
                    return 1;
            }else if(whiteScore == 0 && blackScore > 0){
                //System.out.println("Exit: White 2");
                return Double.NEGATIVE_INFINITY;
            }else if(whiteScore > 0 && blackScore == 0){
                //System.out.println("Exit: White 3");
                return Double.POSITIVE_INFINITY;
            }else{
                //System.out.println("Exit: White 4");
                return ((double)whiteScore/blackScore);
            }
        }else{//Else PlayerTurn == black
            if(whiteScore == 0 && blackScore == 0){
                //System.out.println("Exit: Black 1");
                return 1;
            }else if(whiteScore == 0 && blackScore > 0){
                //System.out.println("Exit: Black 2");
                return Double.POSITIVE_INFINITY;
            }else if(whiteScore > 0 && blackScore == 0){
                //System.out.println("Exit: Black 3");
                return Double.NEGATIVE_INFINITY;
            }else{
                //System.out.println("Exit: Black 4");
                return ((double)blackScore/whiteScore);
            }
        }

    }

    @Override
    public ClosestToSquareHeuristic copy() {
        return new ClosestToSquareHeuristic(whiteDistances.length, whiteDistances[0].length);
    }

    private void populateWithScores( Board b, int[][] pieceLocations, int[][] distances){
        //If too much GC, consider just emptying this List intsead of
        ArrayList<Location> validLocations;


        //frontier is always empty at the start of this method because it runs until the frontier is empty
        if(!frontier.isEmpty()){
            throw new IllegalStateException("Frontier was expected to be empty.");
        }
        for (int[] pieceLocation: pieceLocations) {
            reinitializeToFalse(searched); // Searched for this piece before?
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

    /**
     * this method is intended to limit the number of times the Java must run its garbage collector by recycling memory
     * @param array
     */
    private void reinitializeToMax(int[][] array){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                array[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    /**
     * this method is intended to limit the number of times the Java must run its garbage collector by recycling memory
     * @param array
     */
    private void reinitializeToFalse(boolean[][] array){
        for(int i = 0; i < array.length; i++){
            for(int j = 0; j < array[0].length; j++){
                array[i][j] = false;
            }
        }
    }

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
