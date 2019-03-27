package strategies;

import game.agents.SelectivePlayer;
import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;
import heuristics.HeuristicUtilities;
import heuristics.IBoardValue;
import heuristics.IMoveValueHeuristic;

import java.util.ArrayList;
import java.util.Arrays;


public class SelectiveSearch implements Runnable{

        private int globalMoveCount = 0;

        private final Board originalBoard;

        private Board board;
        private int maxDepth;
        private int currentPlayersTurnColor;
        private IBoardValue boardValue;
        private IMoveValueHeuristic moveValue;
        private HeuristicUtilities heuristicUtils;
        private Move bestMove;
        private boolean useMoveHeuristic; // If false, do not apply move heuristic
        double selectivityCoefficient;

        //Stuff for the agent to move
        private boolean interrupted;
        private boolean runExited;
        private SelectivePlayer invokingPlayer;

        /**
         *
         * @param b The initial board state
         * @param boardValueHeuristic How to evaluate the value of a leaf node
         * @param maxDepth The depth limit of the search
         * @param currentPlayersTurnColor Whose turn it is to move in the Board's initial state
         */
        public SelectiveSearch(Board b,
                                 IBoardValue boardValueHeuristic,
                                 IMoveValueHeuristic moveValue,
                                 int maxDepth,
                                 int currentPlayersTurnColor,
                                 SelectivePlayer invokingPlayer,
                               double selectivityCoefficient){

            if(maxDepth < 1){
                throw new IllegalArgumentException("We should at least search one layer");
            }

            if(selectivityCoefficient < 0 || selectivityCoefficient > 1){
                throw new IllegalArgumentException("Selectivity is the proportion of moves to search after ordering by board value: must be in range 0 to 1");
            }else{
                this.selectivityCoefficient = selectivityCoefficient;
            }
            this.maxDepth = maxDepth;
            this.currentPlayersTurnColor = currentPlayersTurnColor;
            this.boardValue = boardValueHeuristic;
            originalBoard = Board.copyBoard(b);
            this.heuristicUtils = new HeuristicUtilities();
            this.moveValue = moveValue;

            //Purely for multithreaded control
            this.interrupted = false;
            this.runExited= false;
            this.invokingPlayer = invokingPlayer;
        }

        /**
         *
         * @return
         */
        public Move multiMethodAlphaBetaSearch(){
            if(runExited){
                throw new IllegalStateException("Once an AlphaBeta Search object has been interrupted, it cannot be used again");
            }
            //Initialize : Makes a board copyMoveHeuristic
            refreshDataStructures();
            ArrayList<Move> moves = board.getAllMoves(currentPlayersTurnColor);
            if(moves.isEmpty()){
                return null; //GAME OVER
            }
            if(useMoveHeuristic){
                moves = heuristicUtils.orderMovesMethod(moves, board, moveValue);
            }

            double alpha = Double.NEGATIVE_INFINITY;
            double beta = Double.POSITIVE_INFINITY;
            double result;

            //Proportion of moves to keep
            //This should never be so large as it will not fit in an int
            int selectedMoves = (int)Math.ceil(moves.size()*selectivityCoefficient);
            bestMove = moves.get(0);//Set equal to the first move
            //System.out.println("Moves =  " + moves.size() + ", selectedMoves = " + selectedMoves);
            //Continue while there are more moves to play
            Move m;
            for(int i = 0; i < selectedMoves; i+=1){
                m = moves.get(i);
                //System.out.println("Next toplevel move");
                //globalMoveCount +=1;
                board.playMove(m);
                result = minLayer(1, alpha, beta);
                board.revertMove(m);
                //System.out.println(globalMoveCount);
                if(alpha < result){
                    alpha = result;
                    bestMove = m;
                }
                if(result >= beta){
                    return m;
                }

                if(interrupted){
                    break;
                }
                // globalMoveCount+= 1;
            }

            return bestMove;
        }

        protected double maxLayer(int depth, double alpha, double beta){
            //Terminal State Checking
            if(depth == maxDepth){
                return boardValue.getBoardValue(board, currentPlayersTurnColor);
            }
            ArrayList<Move> moves = board.getAllMoves(currentPlayersTurnColor);
            if(moves.isEmpty()){
                return Double.NEGATIVE_INFINITY;//Max player cannot move, so utility of -infinity
            }
            if(useMoveHeuristic){
                moves = heuristicUtils.orderMovesMethod(moves, board, moveValue);
            }
            //Proportion of moves to keep
            //This should never be so large as it will not fit in an int
            int selectedMoves = (int)Math.ceil(moves.size()*selectivityCoefficient);
            //search deeper
            int nextDepth = depth + 1;
            double result = Double.NEGATIVE_INFINITY;
            Move m;
            //System.out.println("Selective: Moves =" + selectedMoves + ", depth = " + depth);
            for(int i = 0; i < selectedMoves; i+=1){
                m = moves.get(i);
                if(interrupted){
                    return result;
                }
                //globalMoveCount +=1;
                board.playMove(m);
                result = Math.max(result,minLayer(nextDepth, alpha,beta));
                board.revertMove(m);

                if(result >= beta){
                    return result;
                }
                alpha = Math.max(alpha, result);
            }
            return result;
        }

        protected double minLayer(int depth, double alpha, double beta){
            //Terminal State Checking
            if(depth == maxDepth){
                //We still use the same utility regardless of who is calling it for this version since we always look from our players perspective
                return boardValue.getBoardValue(board, currentPlayersTurnColor);
            }
            ArrayList<Move> moves = board.getAllMoves(BoardPieces.getColorCpposite(currentPlayersTurnColor));
            if(moves.isEmpty()){
                return Double.POSITIVE_INFINITY;//Min player cannot move, so utility of -infinity
            }
            if(useMoveHeuristic){
                moves = heuristicUtils.orderMovesMethod(moves, board, moveValue);
            }

            //Proportion of moves to keep
            //This should never be so large as it will not fit in an int
            int selectedMoves = (int)Math.ceil(moves.size()*selectivityCoefficient);
            //search deeper
            int nextDepth = depth + 1;
            double result = Double.POSITIVE_INFINITY;
            //System.out.println("Selective: Moves =" + selectedMoves + ", depth = " + depth);
            Move m;
            for(int i = 0; i < selectedMoves; i+=1){
                m = moves.get(i);
                if(interrupted){
                    return result;
                }
                //globalMoveCount +=1;
                board.playMove(m);
                result = Math.min(result, maxLayer(nextDepth, alpha,beta));
                board.revertMove(m);
                if(result <= alpha){
                    return result;
                }
                beta = Math.min(beta, result);
            }
            return result;
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
         * Tell the object to stop processing
         */
        public void interrupt(){
            this.interrupted = true;
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

        /**
         * Runs iterative deepening DFS on the boards state passed in.
         * Runs until interrupted. Once interrupted the thread will end and this object is intended to be discarded
         */
        @Override
        public void run() {
            //Move bestMoveAtDepth = performSearch();
            Move bestMoveAtDepth = multiMethodAlphaBetaSearch();
            if(invokingPlayer!= null) {//Here for testing
                invokingPlayer.giveSearchResult(this, bestMoveAtDepth);
            }
            this.runExited = true;
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

