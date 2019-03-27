package heuristics;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;

public class MobilityTerritory implements IMoveValueHeuristic, IBoardValue{

    private final double mobilityValueCoeff = 1.0/4;
    ClosestToSquareHeuristic territory;
    MobilityOrderingHeuristic mobility;


    public MobilityTerritory(){
        territory = new ClosestToSquareHeuristic();
        mobility = new MobilityOrderingHeuristic();
    }

    @Override
    public double getBoardValue(Board b, int playerTurn) {
        return territory.getBoardValue(b,playerTurn) + mobilityValueCoeff*mobility.getBoardValue(b,playerTurn);
    }

    @Override
    public IMoveValueHeuristic copyMoveHeuristic() {
        return new MobilityTerritory();
    }

    @Override
    public IBoardValue copy(){
        return new MobilityTerritory();
    }

    @Override
    public double getMoveValue(Board b, Move m) {
        int playerColor = b.getPieceAt(m.startRow,m.startCol);
        if(!BoardPieces.isPlayerColor(playerColor)){
            throw new IllegalArgumentException("Move has no starting piece");
        }
        b.playMove(m);
        double result = getBoardValue(b, playerColor);
        b.revertMove(m);
        return result;
    }
}
