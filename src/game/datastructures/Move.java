package game.datastructures;

public class Move{

    public int startRow; //Queen start row position
    public int startCol; //Queen start column position
    public int endRow;  //Queen end row position
    public int endCol; // The Queen end col position
    public int arrowRow; // The row of the deleted Square
    public int arrowCol; // The column of the deleted Square

    public Move(int startRow, int startCol, int endRow, int endCol, int arrowRow, int arrowCol){
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.arrowRow = arrowRow;
        this.arrowCol = arrowCol;
    }

    public boolean equals(Move m){
        return startRow == m.startRow
                && startCol == m.startCol
                && endRow == m.endRow
                && endCol == m.endCol
                && arrowRow == m.arrowRow
                && arrowCol == m.arrowCol;
    }

    public String toString(){
        StringBuilder s = new StringBuilder("(");
        s.append("("+startRow + "," + startCol +")");
        s.append("("+endRow + "," + endCol +")");
        s.append("("+arrowRow + "," + arrowCol +")");
        s.append(")");
        return s.toString();
    }
}
