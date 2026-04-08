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
    Color backgroundColor, gridColor;
    Color[] themeBlockColors;
    String theme = "light";
    private JLabel statusLabel; // store reference

    Color[][] board = new Color[rows][cols];
    TetrisBlocks currentBlock;
    Timer timer;

    public GameArea(JLabel statusLabel) {
        this.statusLabel = statusLabel;

        changeTheme(theme);
        currentBlock = new TetrisBlocks(themeBlockColors);

        timer = new Timer(speed, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
                repaint();
            }
        });

        timer.start();

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("A"), "moveLeft");
        getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveLeft();
                repaint();
            }
        });

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("D"), "moveRight");

        getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveRight();
                repaint();
            }
        });

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("S"), "moveDown");
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

                if (isOutOfBounds(currentBlock) || isBlockCollision(currentBlock)) {
                    currentBlock.setCurrentShape(oldShape); // undo rotation
                }

                repaint();
            }
        });

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("T"), "changeTheme");
        getActionMap().put("changeTheme", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(theme.equals("light")){
                    changeTheme("dark");
                }
                else if(theme.equals("dark")){
                    changeTheme("light");
                }
                repaint();
            }
        });
    }

    public void update() {
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
        if (currentBlock.getX() < 0 || isBlockCollision(currentBlock)) {
            currentBlock.setX(currentBlock.getX() + 1); // undo if invalid
        }
    }

    public void moveRight() {
        currentBlock.setX(currentBlock.getX() + 1);
        if (currentBlock.getX() + currentBlock.getBlockWidth() > cols || isBlockCollision(currentBlock)) {
            currentBlock.setX(currentBlock.getX() - 1); // undo if invalid
        }
    }

    public void updateScore() {
        points += ((linesCleared * level) * 100);
        statusLabel.setText("Points: " + points + " | Level: " + level);
    }

    public void gameOver() {
        System.out.println("GAME OVER!");

        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).clear();
        getActionMap().clear();
        timer.stop();
        repaint();
    }

    int linesCleared = 0;

    public void spawnNewBlock() {
        currentBlock = new TetrisBlocks(themeBlockColors);
        if (isBlockCollision(currentBlock)) {
            if(isGameOver()){
                lockBlock();
            }
        }
        while(isOutOfBounds(currentBlock)){
            currentBlock = new TetrisBlocks(themeBlockColors);
        }
    }

    // stop block move
    public void lockBlock() {
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
            while (isBlockCollision(currentBlock)) {
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
    public boolean isBlockCollision(TetrisBlocks block) {
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

    public void changeTheme(String theme) {
        if (theme.equals("dark")) {
            backgroundColor = new Color(18, 18, 18);
            gridColor = new Color(255, 255, 255, 40);

            themeBlockColors = new Color[]{
                    new Color(0, 255, 255),
                    new Color(255, 255, 0),
                    new Color(128, 0, 128),
                    new Color(0, 255, 0),
                    new Color(255, 0, 0),
                    new Color(0, 0, 255),
                    new Color(255, 165, 0)
            };

        } else if (theme.equals("light")) {
            backgroundColor = Color.WHITE;
            gridColor = new Color(200, 200, 200);


            themeBlockColors = new Color[]{
                    Color.CYAN,
                    Color.YELLOW,
                    Color.MAGENTA,
                    Color.GREEN,
                    Color.RED,
                    Color.BLUE,
                    Color.ORANGE
            };
        }

        this.theme = theme;
        repaint();
    }

    public void paintComponent(Graphics g) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(gridColor);
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
                    g.setColor(board[r][c]);
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
                    Color base = currentBlock.getColor();

                    // fill
                    g.setColor(base);
                    g.fillRect(x, y, cellSize, cellSize);

                    // highlight (top/left) shadow (bottom/right)
                    g.setColor(base.brighter());
                    g.drawLine(x, y, x + cellSize - 1, y);
                    g.drawLine(x, y, x, y + cellSize - 1);
                    g.setColor(base.darker());
                    g.drawLine(x, y + cellSize - 1, x + cellSize - 1, y + cellSize - 1);
                    g.drawLine(x + cellSize - 1, y, x + cellSize - 1, y + cellSize - 1);
                }
            }
        }
    }
}