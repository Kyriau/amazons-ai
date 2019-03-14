package game.client;

import game.datastructures.Board;
import game.datastructures.BoardPieces;
import game.datastructures.Move;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A Java AWT window that shows a game as it is played.
 */
public class GameWindow extends JFrame {

    private Board board;

    private JButton[][] images;

    private ImageIcon black;
    private ImageIcon white;
    private ImageIcon arrow;
    private ImageIcon blank;

    public GameWindow() {

        board = new Board();
        images = new JButton[board.getNumRows()][board.getNumCols()];

        BufferedImage img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        Graphics gr = img.getGraphics();
        gr.setColor(Color.RED);
        gr.fillRect(0, 0, 39, 39);
        gr.setColor(Color.YELLOW);
        gr.drawRect(0, 0, 39, 39);
        gr.setColor(Color.BLACK);
        gr.fillOval(1, 1, 37, 37);
        black = new ImageIcon(img);

        img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        gr = img.getGraphics();
        gr.setColor(Color.RED);
        gr.fillRect(0, 0, 39, 39);
        gr.setColor(Color.YELLOW);
        gr.drawRect(0, 0, 39, 39);
        gr.setColor(Color.WHITE);
        gr.fillOval(1, 1, 37, 37);
        white = new ImageIcon(img);

        img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        gr = img.getGraphics();
        gr.setColor(Color.RED);
        gr.fillRect(0, 0, 39, 39);
        gr.setColor(Color.YELLOW);
        gr.drawRect(0, 0, 39, 39);
        blank = new ImageIcon(img);

        img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        gr = img.getGraphics();
        gr.setColor(Color.DARK_GRAY);
        gr.fillRect(0, 0, 39, 39);
        gr.setColor(Color.YELLOW);
        gr.drawRect(0, 0, 39, 39);
        arrow = new ImageIcon(img);

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setBounds(0, 0, Board.DEFAULT_NUM_ROWS * 40 + 40, Board.DEFAULT_NUM_COLS * 40 + 40);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gbl);

        EmptyBorder empty = new EmptyBorder(0, 0, 0, 0);
        for(int x = 0; x < board.getNumRows(); x++) {
            c.gridx = x;
            for(int y = 0; y < board.getNumCols(); y++) {
                JButton button = new JButton();
                //button.addActionListener(this);
                button.setBorder(empty);
                button.setActionCommand(x + " " + y);
                c.gridy = y;
                if(board.getPieceAt(x, y) == BoardPieces.BLACK)
                    button.setIcon(black);
                else if(board.getPieceAt(x, y) == BoardPieces.WHITE)
                    button.setIcon(white);
                else
                    button.setIcon(blank);
                images[x][y] = button;
                add(button, c);
            }
        }

        setVisible(true);

    }

    public void playMove(Move move) {

        board.playMove(move);

        updateImage(move.startRow, move.startCol);
        updateImage(move.endRow, move.endCol);
        updateImage(move.arrowRow, move.arrowCol);

    }

    public void updateImage(int row, int col) {
        JButton button = images[row][col];
        if(board.getPieceAt(row, col) == BoardPieces.BLACK)
            button.setIcon(black);
        else if(board.getPieceAt(row, col) == BoardPieces.WHITE)
            button.setIcon(white);
        else if(board.getPieceAt(row, col) == BoardPieces.ARROW)
            button.setIcon(arrow);
        else
            button.setIcon(blank);
    }

}