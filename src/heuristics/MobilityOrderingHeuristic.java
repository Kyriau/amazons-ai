package heuristics;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;

public class MobilityOrderingHeuristic implements IMoveValueHeuristic{

    private final boolean[][] visited;

    public MobilityOrderingHeuristic(){
        visited = new boolean[10][10];
    }

    /**
     *
     * @param b The Board that the move will be applied to
     * @param m The move to be applied
     * @return The ratio between the player making the move and their opponent
     */
    @Override
    public double getMoveValue(Board b, Move m) {
        int playerColor = b.getPieceAt(m.startRow,m.startCol);
        if(!BoardPieces.isPlayerColor(playerColor)){
            throw new IllegalArgumentException("Move has no starting piece: " + m);
        }
        b.playMove(m);
        int playerMobility = getMobilityOf(b, b.getPieceLocations(playerColor));
        int opponentMobility = getMobilityOf(b, b.getPieceLocations(BoardPieces.getColorCpposite(playerColor)));
        /*
        System.out.println("Player Mobility " + playerMobility);
        System.out.println("Opponent Mobility " + opponentMobility);
        System.out.println(b);
        //*/
        b.revertMove(m);
        if(opponentMobility == 0){
            return Double.POSITIVE_INFINITY;//The opponents turn will be next, and they cannot move, so you win
        }else{
            return ((double)playerMobility)/opponentMobility;
        }
    }

    @Override
    public IMoveValueHeuristic copyMoveHeuristic() {
        return new MobilityOrderingHeuristic();
    }

    public double getBoardValue(Board b, int playerColor){
        int playerMobility = getMobilityOf(b, b.getPieceLocations(playerColor));
        int opponentMobility = getMobilityOf(b, b.getPieceLocations(BoardPieces.getColorCpposite(playerColor)));
        /*
        System.out.println("Player Mobility " + playerMobility);
        System.out.println("Opponent Mobility " + opponentMobility);
        System.out.println(b);
        //*/
        if(opponentMobility == 0){
            return Double.POSITIVE_INFINITY;//The opponents turn will be next, and they cannot move, so you win
        }else{
            return ((double)playerMobility)/opponentMobility;
        }
    }


    /**
     *
     * @param b
     * @param pieceLocations in {{row1,col1},{row2,col2}, ... , {row4,col4}}
     * @return The number of free spaces available to all pieces if the move is made
     */
    private int getMobilityOf(Board b, int[][] pieceLocations){
        resetVisited();
        int mobility = 0;

        for (int[] pieceLoc : pieceLocations){
            int pRow = pieceLoc[0];
            int pCol = pieceLoc[1];

            int sRow;
            int sCol;
            //Unrolled loop
            //Check above
            for(int shift = 1; shift < 10; shift+=1){
                sRow = pRow - shift;
                if(sRow >=0 && b.isEmptyAt(sRow,pCol)){
                    if(!visited[sRow][pCol]){
                        visited[sRow][pCol] = true;
                        mobility += 1;
                    }
                }else{
                    break;
                }
            }

            //Check topRight
            for(int shift = 1; shift < 10; shift+=1){
                sRow = pRow - shift;
                sCol = pCol + shift;
                if(sRow >=0 && sCol < 10 && b.isEmptyAt(sRow,sCol)){
                    if(!visited[sRow][sCol]){
                        visited[sRow][sCol] = true;
                        mobility += 1;
                    }
                }else{
                    break;
                }
            }

            //Check right
            for(int shift = 1; shift < 10; shift+=1){
                sCol = pCol + shift;
                if(sCol < 10 && b.isEmptyAt(pRow,sCol)){
                    if(!visited[pRow][sCol]){
                        visited[pRow][sCol] = true;
                        mobility += 1;
                    }
                }else{
                    break;
                }
            }
            //BottomRight
            for(int shift = 1; shift < 10; shift+=1){
                sRow = pRow + shift;
                sCol = pCol + shift;
                if(sRow < 10 && sCol < 10 && b.isEmptyAt(sRow,sCol)){
                    if(!visited[sRow][sCol]){
                        visited[sRow][sCol] = true;
                        mobility += 1;
                    }
                }else{
                    break;
                }
            }

            //Down
            for(int shift = 1; shift < 10; shift+=1){
                sRow = pRow + shift;
                if(sRow < 10 &&  b.isEmptyAt(sRow,pCol)){
                    if(!visited[sRow][pCol]){
                        visited[sRow][pCol] = true;
                        mobility += 1;
                    }
                }else{
                    break;
                }
            }

            //BottomLeft
            for(int shift = 1; shift < 10; shift+=1){
                sRow = pRow + shift;
                sCol = pCol - shift;
                if(sRow < 10 && sCol >= 0 && b.isEmptyAt(sRow,sCol)){
                    if(!visited[sRow][sCol]){
                        visited[sRow][sCol] = true;
                        mobility += 1;
                    }
                }else{
                    break;
                }
            }

            //Left
            for(int shift = 1; shift < 10; shift+=1){
                sCol = pCol - shift;
                if(sCol >= 0 && b.isEmptyAt(pRow,sCol)){
                    if(!visited[pRow][sCol]){
                        visited[pRow][sCol] = true;
                        mobility += 1;
                    }
                }else{
                    break;
                }
            }

            //TopLeft
            //BottomLeft
            for(int shift = 1; shift < 10; shift+=1){
                sRow = pRow - shift;
                sCol = pCol - shift;
                if(sRow >= 0 && sCol >= 0 && b.isEmptyAt(sRow,sCol)){
                    if(!visited[sRow][sCol]){
                        visited[sRow][sCol] = true;
                        mobility += 1;
                    }
                }else{
                    break;
                }
            }
        }

        return mobility;
    }

    private void resetVisited(){
        for(int i = 0; i < visited.length; i += 1){
            for(int j = 0; j < visited[0].length; j +=1){
                visited[i][j] = false;
            }
        }
    }
}
