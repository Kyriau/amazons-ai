package game.datastructures;

public class BoardNode {

    private static final BoardNode NULL_PARENT = new BoardNode();

    private BoardNode parent;
    private Board b;
    private NodeType nodeType;
    double alpha, beta;

    private BoardNode(){

    }

    public BoardNode(NodeType nodeType, Board board){
        parent = NULL_PARENT;

    }

    public BoardNode(BoardNode parentNode, Board board, Move move){
        parent = parentNode;
        if(parentNode.nodeType == NodeType.MAX){
            nodeType = NodeType.MIN;
        }else{
            nodeType = NodeType.MAX;
        }
        alpha = Double.NEGATIVE_INFINITY;
        beta = Double.POSITIVE_INFINITY;
        b = Board.copyBoard(board);
        b.playMove(move);
    }

    public boolean isRoot(){
        return parent == NULL_PARENT;
    }

    public BoardNode getParent(){
        return parent;
    }

    public void receiveChildBoundValues(double a, double b){
        if(nodeType == NodeType.MIN && a < alpha){
            alpha = a;
        }else if(b > beta){
            beta = b;
        }
    }
    /**
     *
     */
    public void passUpValues(){
        parent.receiveChildBoundValues(alpha, beta);
    }

}
