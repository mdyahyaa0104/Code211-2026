import java.awt.*;
import java.util.Arrays;
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

            , S = {
            {0, 1, 1},
            {1, 1, 0}
    }

            , Z = {
            {1, 1, 0},
            {0, 1, 1}
    }

            , L = {
            {1, 0},
            {1, 0},
            {1, 1}
    }
            , J = {
            {0, 1},
            {0, 1},
            {1, 1}
    }
            , T = {
            {1, 1, 1},
            {0, 1, 0}
    };

    private int[][][] shapesList = {O,I,S,Z,L,J,T};
    private int[][] currentShape;
    private int x = 1, y = 0;

    private Color color;
    private Color[] colors = {
            new Color(0, 255, 255),   // I - cyan
            new Color(255, 255, 0),   // O - yellow
            new Color(128, 0, 128),   // T - purple
            new Color(0, 255, 0),     // S - green
            new Color(255, 0, 0),     // Z - red
            new Color(0, 0, 255),     // J - blue
            new Color(255, 165, 0)    // L - orange
    };

    public TetrisBlocks(Color[] themeColors){
        Random rand = new Random();
        int randIndex = rand.nextInt(shapesList.length);
        currentShape = shapesList[randIndex];

        color = themeColors[randIndex];
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

    public Color getColor() {
        return color;  
    }
}