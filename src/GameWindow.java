import javax.swing.*;

public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("Calorie Adventure - Vanilla Java2D");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
