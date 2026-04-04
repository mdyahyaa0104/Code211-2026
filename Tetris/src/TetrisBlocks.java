import java.awt.*;
import java.util.Random;

public class TetrisBlocks {
    private int[][] O = {
            {1, 1},
            {1, 1}
    };

    private int[][] I = {
            {1},
            {1},
            {1},
            {1}
    };

    private int[][] S = {
            {0, 1, 1},
            {1, 1, 0}
    };

    private int[][] Z = {
            {1, 1, 0},
            {0, 1, 1}
    };

    private int[][] L = {
            {1, 0},
            {1, 0},
            {1, 1}
    };

    private int[][] J = {
            {0, 1},
            {0, 1},
            {1, 1}
    };

    private int[][] T = {
            {1, 1, 1},
            {0, 1, 0}
    };

    private int[][][] shapesList = {O,I,S,Z,L,J,T};
    private int[][] currentShape;
    private int x = 4, y = 0;

    public TetrisBlocks(){
        Random rand = new Random();
        int randIndex = rand.nextInt(8);
        currentShape = shapesList[randIndex];
    }

    public int[][] getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(int[][] currentShape) {
        this.currentShape = currentShape;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
