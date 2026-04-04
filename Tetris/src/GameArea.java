import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GameArea extends JPanel {
    int rows = 20;
    int cols = 10;
    int cellSize = 30;

    Color[][] board = new Color[rows][cols];
    TetrisBlocks currentBlock = new TetrisBlocks();

    Timer timer;
    public GameArea() {
        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    update();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                repaint();
            }
        });
        timer.start();

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveLeft();
                repaint();
            }
        });

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveRight();
                repaint();
            }
        });

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        getActionMap().put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentBlock.getY() + currentBlock.getBlockHeight() < rows && !isCollisionBelow(currentBlock)) {
                    currentBlock.setY(currentBlock.getY() + 1);
                } else {
                    lockBlock();
                    try {
                        spawnNewBlock();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                if(currentBlock.getY() + currentBlock.getBlockHeight() > rows){
                    currentBlock.setY(currentBlock.getY() - 1);
                }

                repaint();
            }
        });

<<<<<<< HEAD
    private void gameOver() throws InterruptedException {
        System.out.println("GAME OVER!");
        timer.stop();

        Thread.sleep(1000000);
    }

    private boolean isCollisionBelow(TetrisBlocks block) {
        int[][] shape = block.getCurrentShape();
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int nextRow = block.getY() + r + 1;
                    int col = block.getX() + c;

                    if (nextRow >= rows) return true;

                    if (board[nextRow][col] != null) return true;
                }
            }
        }
        return false;
    }

    private void spawnNewBlock() throws InterruptedException {
        currentBlock = new TetrisBlocks();
        if (checkCollision(currentBlock)) {
            gameOver(); // trigger game over
        }
=======
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "rotateBlock");
        getActionMap().put("rotateBlock", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentBlock.setCurrentShape(rotateClockwise(currentBlock.getCurrentShape()));
                repaint();
            }
        });
>>>>>>> b6d4f5c7ba132dd6ad081aaf65675e53f9e2f8a4
    }

    private void update() throws InterruptedException {
        if (currentBlock == null) return; // stop updating if game over

        if (currentBlock.getY() + currentBlock.getBlockHeight() < rows && !isCollisionBelow(currentBlock)) {
            currentBlock.setY(currentBlock.getY() + 1);
        } else {
            lockBlock();
            spawnNewBlock();
        }

        if(currentBlock.getY() + currentBlock.getBlockHeight() > rows){
            currentBlock.setY(currentBlock.getY() - 1);
        }

        // wall collision
        if (currentBlock.getX() < 0) currentBlock.setX(0);
        if (currentBlock.getX() + currentBlock.getBlockWidth() > cols)
            currentBlock.setX(cols - currentBlock.getBlockWidth());
    }

    public void moveLeft() {
        currentBlock.setX(currentBlock.getX() - 1);
        if (currentBlock.getX() < 0 || checkCollision(currentBlock)) {
            currentBlock.setX(currentBlock.getX() + 1); // undo if invalid
        }
    }

    public void moveRight() {
        currentBlock.setX(currentBlock.getX() + 1);
        if (currentBlock.getX() + currentBlock.getBlockWidth() > cols || checkCollision(currentBlock)) {
            currentBlock.setX(currentBlock.getX() - 1); // undo if invalid
        }
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
                        board[boardRow][boardCol] = currentBlock.getColor();
                    }
                }
            }
        }

        clearLines();
    }

    public int clearLines() {
        int linesCleared = 0;

        for(int r = rows-1; r >= 0; r--){
            boolean fullLine = true;

            for(int c = 0; c < cols; c++){
                if(board[r][c] == null){
                    fullLine = false;
                }
            }

            if(fullLine){
                clearLine(r);
                linesCleared++;
            }
        }

        return linesCleared;
    }

    public void clearLine(int rowIndex) {
        // Clear the row first
        for (int c = 0; c < cols; c++) {
            board[rowIndex][c] = null; // empty cell
        }

        // Shift rows above down
        for (int r = rowIndex; r >= 1; r--) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = board[r - 1][c];
            }
        }

        // Clear the top row
        for (int c = 0; c < cols; c++) {
            board[0][c] = null;
        }
    }

    private void spawnNewBlock() {
        currentBlock = new TetrisBlocks();
        if (checkCollision(currentBlock)) {
            gameOver(); // trigger game over
        }
    }

    private void gameOver() {
        System.out.println("GAME OVER!");

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).clear();
        getActionMap().clear();
        timer.stop();
        repaint();
    }

    public int[][] rotateClockwise(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] rotated = new int[cols][rows]; // notice swapped dimensions

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                rotated[c][rows - 1 - r] = matrix[r][c];
            }
        }

        return rotated;
    }

    private boolean isCollisionBelow(TetrisBlocks block) {
        int[][] shape = block.getCurrentShape();
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int nextRow = block.getY() + r + 1;
                    int col = block.getX() + c;

                    if (nextRow >= rows) return true;

                    if (board[nextRow][col] != null) return true;
                }
            }
        }
        return false;
    }

    // Block-to-Block Collision Check
    private boolean checkCollision(TetrisBlocks block) {
        int[][] shape = block.getCurrentShape();
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int boardRow = block.getY() + r;
                    int boardCol = block.getX() + c;
                    if (boardRow < rows && board[boardRow][boardCol] != null) {
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
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board[r][c] != null) {
                    g.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }
        }

        // draw current falling block
        int[][] shape = currentBlock.getCurrentShape();
        g.setColor(currentBlock.getColor());
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