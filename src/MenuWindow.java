import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MenuWindow extends JFrame {
    private JButton startButton;
    private JButton highScoresButton;


    public MenuWindow() {
        setTitle("Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(getForeground());
        setResizable(false);
        setSize(800, 600);
        setLayout(null);

        // Start Game Button
        startButton = new JButton("Start Game");
        startButton.setBounds(300, 250, 200, 50);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Start the game
                GameWindow gameWindow = new GameWindow(MenuWindow.this);
                gameWindow.setVisible(true);
                setVisible(false); // Hide the menu window
            }
        });
        add(startButton);

        // High Scores Button
        highScoresButton = new JButton("High Scores");
        highScoresButton.setBounds(300, 350, 200, 50);
        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show high scores
                showHighScores();
            }
        });
        add(highScoresButton);

        setVisible(true);
    }

    private void showHighScores() {
        List<String> highScores = loadHighScores();
        StringBuilder scoresText = new StringBuilder("High Scores:\n");
        for (String score : highScores) {
            scoresText.append(score).append("\n");
        }
        JOptionPane.showMessageDialog(this, scoresText.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }

    private List<String> loadHighScores() {
        List<String> highScores = new ArrayList<>();
        File file = new File("highscores.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    highScores.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        highScores.sort(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                try {
                    int score1 = Integer.parseInt(s1.split(" - ")[1]);
                    int score2 = Integer.parseInt(s2.split(" - ")[1]);
                    return Integer.compare(score2, score1); // Descending order
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    return 0; // Handle invalid score format gracefully
                }
            }
        });

        return highScores;
    }
}
