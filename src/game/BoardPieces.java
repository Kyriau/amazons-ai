package game;


public class BoardPieces {

    public static final int EMPTY = 0;
    public static final int ARROW = 1;
    public static final int BLACK = 2;
    public static final int WHITE = 3;

    public static int getColorCpposite(int pieceColor){
        switch (pieceColor){
            case BLACK:
                return WHITE;
            case WHITE:
                return BLACK;
            default:
                throw new IllegalArgumentException("illegal colour selection: color = " + pieceColor);
        }
    }

    public static String getPieceString(int piece){
        switch (piece){
            case EMPTY:
                return ".";
            case ARROW:
                return "@";
            case BLACK:
                return "B";
            case WHITE:
                return "W";
                default:
                    throw new IllegalArgumentException("no piece with that value");
        }
    }

}
