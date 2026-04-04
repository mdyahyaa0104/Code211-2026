import javax.swing.*;
import java.awt.*;

public class TetrisPlayer {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Tetris");
        JLabel statusLabel = new JLabel("Points: 0 | Level: 1");
        GameArea gameArea = new GameArea(statusLabel);

        // Create a label for points or messages
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Layout: Game area on top, label below
        frame.setLayout(new BorderLayout());
        frame.add(gameArea, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);

        frame.setSize(800, 900); // slightly taller for label
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }
}