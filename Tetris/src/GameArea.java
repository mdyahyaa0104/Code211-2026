import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameArea extends JPanel {
    int rows = 20;
    int cols = 10;
    int cellSize = 30;

    int[][] board = new int[rows][cols];
    TetrisBlocks currentBlock = new TetrisBlocks();

    public GameArea() {
        Timer timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });
        timer.start();
    }

    private void update() {

        // simple floor collision
        currentBlock.setY(currentBlock.getY()+1); // fall down

        // floor collision
        if (currentBlock.getY() + currentBlock.getBlockHeight() >= rows) {
            // lock block into board
            lockBlock();
            // spawn new block
            currentBlock = new TetrisBlocks();
        }


        // simple wall collision
        if(currentBlock.getX() < 1){
            currentBlock.setX(1);
        }
        if(currentBlock.getX() > cols){
            currentBlock.setX(cols);
        }
    }

    private void lockBlock() {
        int[][] shape = currentBlock.getCurrentShape();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int boardRow = currentBlock.getY() + r;
                    int boardCol = currentBlock.getX() + c;
                    if (boardRow >= 0 && boardRow < rows && boardCol >= 0 && boardCol < cols) {
                        board[boardRow][boardCol] = 1;
                    }
                }
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw grid
        g.setColor(Color.DARK_GRAY);
        for (int r = 0; r <= rows; r++) {
            g.drawLine(0, r * cellSize, cols * cellSize, r * cellSize);
        }
        for (int c = 0; c <= cols; c++) {
            g.drawLine(c * cellSize, 0, c * cellSize, rows * cellSize);
        }

        int[][] shape = currentBlock.getCurrentShape();

        g.setColor(Color.GREEN);

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int x = (currentBlock.getX() + c) * cellSize;
                    int y = (currentBlock.getY() + r) * cellSize;

                    g.fillRect(x, y, cellSize, cellSize);
                }
            }
        }
    }
}