import javax.swing.JFrame;

public class TetrisPlayer {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        GameArea gameArea = new GameArea();

        frame.add(gameArea);
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }
}