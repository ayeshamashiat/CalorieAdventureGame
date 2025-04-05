import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    private Thread gameThread;
    private boolean running = true;
    private JButton restartButton;


    private final int WIDTH = 800, HEIGHT = 600;
    private Player player;
    private ArrayList<GameObject> objects = new ArrayList<>();

    private int healthyPoints = 0;
    private int unhealthyPoints = 0;

    private long startTime;
    private final int GAME_DURATION = 60; // seconds

    private boolean gameOver = false;
    private String gameResult = "";

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocus();
        addKeyListener(this);

        player = new Player(WIDTH / 2, HEIGHT - 100);
        startTime = System.currentTimeMillis();

        gameThread = new Thread(this);
        gameThread.start();

        restartButton = new JButton("Restart");
        restartButton.setBounds(350, 320, 100, 40);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> restartGame());
        this.setLayout(null);
        this.add(restartButton);

    }

    @Override
    public void run() {
        long lastSpawnTime = System.currentTimeMillis();

        while (running) {
            if (!gameOver) {
                update();
                repaint();

                if (System.currentTimeMillis() - lastSpawnTime >= 2000) {
                    spawnRandomObject();
                    lastSpawnTime = System.currentTimeMillis();
                }

                long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                if (elapsed >= GAME_DURATION) {
                    endGame();
                }
            }

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();

        Iterator<GameObject> it = objects.iterator();
        while (it.hasNext()) {
            GameObject obj = it.next();
            obj.update();

            if (obj.getBounds().intersects(player.getBounds())) {
                if (obj instanceof HealthyFood) healthyPoints++;
                else if (obj instanceof UnhealthyFood) unhealthyPoints++;
                else if (obj instanceof Obstacle) {
                    gameOver = true;
                    gameResult = "Game Over: Hit an Obstacle!";
                    return;
                }
                it.remove();
            } else if (obj.isOutOfScreen()) {
                it.remove();
            }
        }
    }

    private void endGame() {
        gameOver = true;
        if (healthyPoints > unhealthyPoints)
            gameResult = "You Win!";
        else{
            gameResult = "You Lose!";
        }
        restartButton.setVisible(true);
        saveHighScore();
    }

    private void restartGame() {
        healthyPoints = 0;
        unhealthyPoints = 0;
        startTime = System.currentTimeMillis();
        gameOver = false;
        gameResult = "";
        objects.clear();
        restartButton.setVisible(false);
        SoundPlayer.play("restart.mp3", true);
    }
    

    private void spawnRandomObject() {
        int laneWidth = WIDTH / 3;
        int lane = new Random().nextInt(3);
        int x = lane * laneWidth + (laneWidth - 50) / 2;

        int choice = new Random().nextInt(10);
        if (choice < 4)
            objects.add(new HealthyFood(x, 0));
        else if (choice < 8)
            objects.add(new UnhealthyFood(x, 0));
        else
            objects.add(new Obstacle(x, 0));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawLanes(g);

        player.draw(g);

        for (GameObject obj : objects) {
            obj.draw(g);
        }

        drawScore(g);

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString(gameResult, WIDTH / 2 - 120, HEIGHT / 2);
        }
    }

    private void drawLanes(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        int laneWidth = WIDTH / 3;
        for (int i = 1; i < 3; i++) {
            g.drawLine(i * laneWidth, 0, i * laneWidth, HEIGHT);
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Healthy: " + healthyPoints, 10, 20);
        g.drawString("Unhealthy: " + unhealthyPoints, 10, 40);

        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        g.drawString("Time Left: " + Math.max(0, GAME_DURATION - elapsed), 650, 20);
        g.drawString("High Score: " + loadHighScore(), 650, 40);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private void saveHighScore() {
        try {
            int score = healthyPoints - unhealthyPoints;
            File file = new File("highscore.txt");
            int oldHigh = 0;
            if (file.exists()) {
                Scanner sc = new Scanner(file);
                if (sc.hasNextInt()) oldHigh = sc.nextInt();
                sc.close();
            }

            if (score > oldHigh) {
                PrintWriter pw = new PrintWriter(file);
                pw.println(score);
                pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int loadHighScore() {
        try {
            File file = new File("highscore.txt");
            if (!file.exists()) return 0;
            Scanner sc = new Scanner(file);
            int hs = sc.hasNextInt() ? sc.nextInt() : 0;
            sc.close();
            return hs;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
