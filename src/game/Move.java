package game;

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
}
