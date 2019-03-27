package group5.amazons.singleplayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

public class Game extends JFrame implements ActionListener {

    private static final int SIZE = 8;

    private char[][] board = {
            {' ', ' ', 'b', ' ', ' ', 'b', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'b', ' ', ' ', ' ', ' ', ' ', ' ', 'b'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'w', ' ', ' ', ' ', ' ', ' ', ' ', 'w'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'w', ' ', ' ', 'w', ' ', ' '}
    };

    private char[][] lastBoard = copyBoard(board);

    private JButton[][] images = new JButton[SIZE][SIZE];

    private ImageIcon black;
    private ImageIcon white;
    private ImageIcon block;
    private ImageIcon blank;
    private ImageIcon ghost;

    private int phase = 0;
    private int sx = -1;
    private int sy = -1;
    private int dx = -1;
    private int dy = -1;

    public Game() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, SIZE * 40 + 40, SIZE * 40 + 40);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        setLayout(gbl);

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
        block = new ImageIcon(img);

        img = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
        gr = img.getGraphics();
        gr.setColor(Color.RED);
        gr.fillRect(0, 0, 39, 39);
        gr.setColor(Color.YELLOW);
        gr.drawRect(0, 0, 39, 39);
        gr.fillOval(15, 15, 9, 9);
        ghost = new ImageIcon(img);

        EmptyBorder empty = new EmptyBorder(0, 0, 0, 0);
        for(int x = 0; x < SIZE; x++) {
            c.gridx = x;
            for(int y = 0; y < SIZE; y++) {
                JButton button = new JButton();
                button.addActionListener(this);
                button.setBorder(empty);
                button.setActionCommand(x + " " + y);
                c.gridy = y;
                if(board[x][y] == 'b')
                    button.setIcon(black);
                else if(board[x][y] == 'w')
                    button.setIcon(white);
                else
                    button.setIcon(blank);
                images[x][y] = button;
                add(button, c);
            }
        }

        setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        int x = command.charAt(0) - 48;
        int y = command.charAt(2) - 48;

        if(phase == 0 && board[x][y] == 'w')
            getMoves(x, y);
        else if(phase == 1 && board[x][y] == 'g')
            getBlocks(x, y);
        else if(phase == 2 && board[x][y] == 'h')
            makeMove(x, y);
        else if(phase == 3 && board[x][y] == 'b')
            getMoves(x, y);
        else if(phase == 4 && board[x][y] == 'g')
            getBlocks(x, y);
        else if(phase == 5 && board[x][y] == 'h')
            makeMove(x, y);
        else
            resetTurn();

        draw();

    }

    private void getMoves(int x, int y) {

        boolean valid = false;

        sx = x;
        sy = y;

        for(int i = 1; x + i < SIZE; i++) {
            if(board[x + i][y] == ' ') {
                board[x + i][y] = 'g';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x - i >= 0; i++) {
            if(board[x - i][y] == ' ') {
                board[x - i][y] = 'g';
                valid = true;
            } else
                break;
        }

        for(int i = 1; y + i < SIZE; i++) {
            if(board[x][y + i] == ' ') {
                board[x][y + i] = 'g';
                valid = true;
            } else
                break;
        }

        for(int i = 1; y - i >= 0; i++) {
            if(board[x][y - i] == ' ') {
                board[x][y - i] = 'g';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x + i < SIZE && y + i < SIZE; i++) {
            if(board[x + i][y + i] == ' ') {
                board[x + i][y + i] = 'g';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x - i >= 0 && y + i < SIZE; i++) {
            if(board[x - i][y + i] == ' ') {
                board[x - i][y + i] = 'g';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x + i < SIZE && y - i >= 0; i++) {
            if(board[x + i][y - i] == ' ') {
                board[x + i][y - i] = 'g';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x - i >= 0 && y - i >= 0; i++) {
            if(board[x - i][y - i] == ' ') {
                board[x - i][y - i] = 'g';
                valid = true;
            } else
                break;
        }

        if(valid)
            phase++;
        else
            resetTurn();

    }

    private void getBlocks(int x, int y) {

        boolean valid = false;

        dx = x;
        dy = y;

        clearG();
        board[x][y] = phase < 3 ? 'w' : 'b';
        board[sx][sy] = ' ';

        for(int i = 1; x + i < SIZE; i++) {
            if(board[x + i][y] == ' ') {
                board[x + i][y] = 'h';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x - i >= 0; i++) {
            if(board[x - i][y] == ' ') {
                board[x - i][y] = 'h';
                valid = true;
            } else
                break;
        }

        for(int i = 1; y + i < SIZE; i++) {
            if(board[x][y + i] == ' ') {
                board[x][y + i] = 'h';
                valid = true;
            } else
                break;
        }

        for(int i = 1; y - i >= 0; i++) {
            if(board[x][y - i] == ' ') {
                board[x][y - i] = 'h';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x + i < SIZE && y + i < SIZE; i++) {
            if(board[x + i][y + i] == ' ') {
                board[x + i][y + i] = 'h';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x - i >= 0 && y + i < SIZE; i++) {
            if(board[x - i][y + i] == ' ') {
                board[x - i][y + i] = 'h';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x + i < SIZE && y - i >= 0; i++) {
            if(board[x + i][y - i] == ' ') {
                board[x + i][y - i] = 'h';
                valid = true;
            } else
                break;
        }

        for(int i = 1; x - i >= 0 && y - i >= 0; i++) {
            if(board[x - i][y - i] == ' ') {
                board[x - i][y - i] = 'h';
                valid = true;
            } else
                break;
        }

        if(valid)
            phase++;
        else
            resetTurn();

    }

    private void makeMove(int x, int y) {

        board[dx][dy] = phase < 3 ? 'w' : 'b';
        board[x][y] = 'x';

        clearG();
        clearH();

        lastBoard = copyBoard(board);

        phase = phase < 3 ? 3 : 0;

    }

    private void clearG() {
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                if(board[x][y] == 'g')
                    board[x][y] = ' ';
            }
        }
    }

    private void clearH() {
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                if(board[x][y] == 'h')
                    board[x][y] = ' ';
            }
        }
    }

    private void resetTurn() {
        sx = -1;
        sy = -1;
        dx = -1;
        dy = -1;
        board = copyBoard(lastBoard);
        phase = phase < 3 ? 0 : 3;
    }

    private void draw() {
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                if(board[x][y] == 'b' || board[x][y] == 'B')
                    images[x][y].setIcon(black);
                else if(board[x][y] == 'w' || board[x][y] == 'W')
                    images[x][y].setIcon(white);
                else if(board[x][y] == ' ')
                    images[x][y].setIcon(blank);
                else if(board[x][y] == 'x')
                    images[x][y].setIcon(block);
                else
                    images[x][y].setIcon(ghost);
            }
        }
    }

    private static char[][] copyBoard(char[][] source) {
        char[][] result = new char[SIZE][SIZE];
        for(int x = 0; x < SIZE; x++) {
            for(int y = 0; y < SIZE; y++) {
                result[x][y] = source[x][y];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Game game = new Game();
    }

}