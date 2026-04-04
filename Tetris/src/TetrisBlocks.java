import java.awt.*;
import java.util.Random;

public class TetrisBlocks {
    int[][] O = {
            {1, 1},
            {1, 1}
    };

    int[][] I = {
            {1},
            {1},
            {1},
            {1}
    };

    int[][] S = {
            {0, 1, 1},
            {1, 1, 0}
    };

    int[][] Z = {
            {1, 1, 0},
            {0, 1, 1}
    };

    int[][] L = {
            {1, 0},
            {1, 0},
            {1, 1}
    };

    int[][] J = {
            {0, 1},
            {0, 1},
            {1, 1}
    };

    int[][] T = {
            {1, 1, 1},
            {0, 1, 0}
    };

    int[][][] shapesList = {O,I,S,Z,L,J,T};
    int[][] currentShape;
    int x = 4;
    int y = 0;

    public TetrisBlocks(){
        Random rand = new Random();
        int randIndex = rand.nextInt(8);
        currentShape = shapesList[randIndex];
    }

    public int[][] getCurrentShape() {
        return currentShape;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
