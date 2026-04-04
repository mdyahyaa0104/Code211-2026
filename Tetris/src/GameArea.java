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

    private boolean isCollisionBelow(TetrisBlocks block) {
        int[][] shape = block.getCurrentShape();
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int nextRow = block.getY() + r + 1;
                    int col = block.getX() + c;

                    // check floor
                    if (nextRow >= rows) return true;

                    // check if the cell below is already occupied
                    if (board[nextRow][col] == 1) return true;
                }
            }
        }
        return false;
    }

    private void update() {
        // calculate bottom of the block
        int bottomY = currentBlock.getY() + currentBlock.getBlockHeight();

        // check if block can move down without hitting floor or another block
        if (bottomY < rows && !isCollisionBelow(currentBlock)) {
            currentBlock.setY(currentBlock.getY() + 1); // move down
        } else {
            // this stops blocks
            lockBlock();

            //spawns in new blcok
            currentBlock = new TetrisBlocks();


            if (checkCollision(currentBlock)) {
                System.out.println("Game Over");

            }

            return;
        }

        // wall collision
        if (currentBlock.getX() < 0) currentBlock.setX(0);
        if (currentBlock.getX() + currentBlock.getBlockWidth() > cols)
            currentBlock.setX(cols - currentBlock.getBlockWidth());
    }

    // stop block move
    private void lockBlock() {
        int[][] shape = currentBlock.getCurrentShape();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int boardRow = currentBlock.getY() + r;
                    int boardCol = currentBlock.getX() + c;
                    if (boardRow < rows && boardCol < cols) {
                        board[boardRow][boardCol] = 1;
                    }
                }
            }
        }
    }

    // This checls whenfsd the blcosk collides with other blcisls
    private boolean checkCollision(TetrisBlocks block) {
        int[][] shape = block.getCurrentShape();
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int boardRow = block.getY() + r;
                    int boardCol = block.getX() + c;
                    if (boardRow < rows && board[boardRow][boardCol] == 1) {
                        return true; // collision detected
                    }
                }
            }
        }
        return false;
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

        // draw locked blocks
        g.setColor(Color.BLUE);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] == 1) {
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }
        }

        // draw current falling block
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