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
        currentBlock.y--;

        // simple floor collision
        if (currentBlock.y + currentBlock.currentShape.length < 0) {
            currentBlock.y = 0; // reset for now
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
    }
}