import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;

public class GameWindow extends JFrame {
    private GamePanel gamePanel;

    public GameWindow(MenuWindow menuWindow) {
        setTitle("Calorie Adventure - Play Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);

        // Add the GamePanel
        gamePanel = new GamePanel(() -> {
            // Callback when the game ends (eats unhealthy food)
            String playerName = JOptionPane.showInputDialog(this, "Enter your name:", "Game Over", JOptionPane.PLAIN_MESSAGE);
            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Player";
            }
            
            int finalScore = gamePanel.getScore();
            saveHighScore(playerName, finalScore);
            
            setVisible(false); // Hide game screen
            dispose();         // Release window resources
            
            // Open welcome screen with the highscores tab open and player's newly added spot highlighted!
            menuWindow.showHighScoresWithHighlight(playerName, finalScore);
        });

        add(gamePanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveHighScore(String playerName, int score) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("highscores.txt", true))) {
            writer.println(playerName + " - " + score);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
