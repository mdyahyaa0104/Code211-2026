import java.awt.*;
import java.util.Random;

public class TetrisBlocks {
    private int[][] O = {
            {1, 1},
            {1, 1}
    }
            , I = {
            {1},
            {1},
            {1},
            {1}
    }
            , I2 = {
            {1, 1, 1, 1}
    }
            , S = {
            {0, 1, 1},
            {1, 1, 0}
    }
            , S2 = {
            {1, 0},
            {1, 1},
            {0, 1}
    }
            , Z = {
            {1, 1, 0},
            {0, 1, 1}
    }
            , Z2 = {
            {0, 1},
            {1, 1},
            {1, 0}
    }
            , L = {
            {1, 0},
            {1, 0},
            {1, 1}
    }
            , L2 = {
            {1, 1, 1},
            {1, 0, 0}
    }
            , L3 = {
            {1, 1},
            {0, 1},
            {0, 1}
    }
            , L4 = {
            {0, 0, 1},
            {1, 1, 1}
    }
            , J = {
            {0, 1},
            {0, 1},
            {1, 1}
    }
            , J2 = {
            {1, 0, 0},
            {1, 1, 1}
    }
            , J3 = {
            {1, 1},
            {1, 0},
            {1, 0}
    }
            , J4 = {
            {1, 1, 1},
            {0, 0, 1}
    }
            , T = {
            {1, 1, 1},
            {0, 1, 0}
    }
            , T2 = {
            {0, 1},
            {1, 1},
            {0, 1}
    }
            , T3 = {
            {0, 1, 0},
            {1, 1, 1}
    }
            , T4 = {
            {1, 0},
            {1, 1},
            {1, 0}
    };

    private int[][][] shapesList = {O,I,S,Z,L,J,T};
    private int[][] currentShape;
    private int x = 3, y = 0;

    public TetrisBlocks(){
        Random rand = new Random();
        int randIndex = rand.nextInt(7);
        currentShape = shapesList[randIndex];
    }

    public int[][] getCurrentShape() {
        return currentShape;
    }

    public void setCurrentShape(int[][] currentShape) {
        this.currentShape = currentShape;
    }

    public int getBlockHeight(){
        return currentShape.length;
    }

    public int getBlockWidth(){
        return currentShape[0].length;
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