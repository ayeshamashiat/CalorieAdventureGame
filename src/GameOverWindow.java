import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverWindow extends JFrame {
    public GameOverWindow(MenuWindow menuWindow) {
        setTitle("Game Over");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 200);
        setLayout(null);

        JLabel gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setBounds(150, 30, 200, 30);
        add(gameOverLabel);

        JButton restartButton = new JButton("Restart");
        restartButton.setBounds(100, 100, 80, 30);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Close the Game Over window
                new GameWindow(menuWindow); // Start a new game
            }
        });
        add(restartButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(220, 100, 80, 30);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Exit the application
            }
        });
        add(exitButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
