import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;


public class GameArea extends JPanel {
    int rows = 20, cols = 10, cellSize = 30;
    int points = 10, level = 1;
    int speed = 500;
    private JLabel statusLabel; // store reference


    Color[][] board = new Color[rows][cols];
    TetrisBlocks currentBlock = new TetrisBlocks();

    Timer timer;
    public GameArea(JLabel statusLabel) {

        this.statusLabel = statusLabel;

        timer = new Timer(speed, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
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
                    spawnNewBlock();
                }

                if(currentBlock.getY() + currentBlock.getBlockHeight() > rows){
                    currentBlock.setY(currentBlock.getY() - 1);
                }

                repaint();
            }
        });

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("R"), "rotateBlock");
        getActionMap().put("rotateBlock", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] oldShape = currentBlock.getCurrentShape();
                int[][] rotated = rotateClockwise(oldShape);

                currentBlock.setCurrentShape(rotated);

                if (isOutOfBounds(currentBlock) || checkCollision(currentBlock)) {
                    currentBlock.setCurrentShape(oldShape); // undo rotation
                }

                repaint();
            }
        });
    }

    private void update() {
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

    private void updateScore() {
        points += ((linesCleared * level) * 100);
        statusLabel.setText("Points: " + points + " | Level: " + level);
    }

    private void gameOver() {
        System.out.println("GAME OVER!");

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).clear();
        getActionMap().clear();
        timer.stop();
        repaint();
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
        updateScore();
    }


    int linesCleared = 0;

    public void clearLines() {

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
                r++;
            }
        }

        if((linesCleared + 1) % 10 == 0){
            level++;
            speed = Math.max(100, this.speed - 50);
            timer.setDelay(speed);
        }
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
            if(isGameOver()){
                lockBlock();
            }
        }
        while(isOutOfBounds(currentBlock)){
            currentBlock = new TetrisBlocks();
        }
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

    public boolean isGameOver(){
        try {
            while (checkCollision(currentBlock)) {
                currentBlock.setX(currentBlock.getX() + 1);
            }
        } catch(IndexOutOfBoundsException e){
            gameOver();
            return true;
        }
        return false;
    }

    public boolean isOutOfBounds(TetrisBlocks block) {
        int[][] shape = block.getCurrentShape();

        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (shape[r][c] == 1) {
                    int x = block.getX() + c;
                    int y = block.getY() + r;

                    if (x < 0 || x >= cols || y >= rows) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCollisionBelow(TetrisBlocks block) {
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
    public boolean checkCollision(TetrisBlocks block) {
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