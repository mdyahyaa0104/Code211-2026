import java.awt.*;

public class TetrisBlocks {
    private int x, y; // Current grid position
    private int[][] shape; // 2D array representing the piece
    private Color color;

    public void moveDown() {
        y++; // Increment Y to move the block down the grid
    }
}
