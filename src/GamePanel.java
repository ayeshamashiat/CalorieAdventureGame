import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    private int score = 0;
    private boolean gameOver = false;
    private Runnable onGameOver;
    private List<GameObject> objects = new ArrayList<>();
    private Player player;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private long gameStartTime = System.currentTimeMillis(); // Track when the game started

    public GamePanel(Runnable onGameOver) {
        this.onGameOver = onGameOver;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        player = new Player();
        addKeyListener(this);
        new Thread(this).start();
    }

    @Override
    public void run() {
        long lastSpawnTime = System.currentTimeMillis();

        while (!gameOver) {
            updateGame();
            repaint();

            if (System.currentTimeMillis() - lastSpawnTime >= 2000) { // Spawn every 2 seconds
                spawnRandomObject();
                lastSpawnTime = System.currentTimeMillis();
            }

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Notify the GameWindow when the game ends
        if (onGameOver != null) {
            onGameOver.run();
        }
    }

    private void updateGame() {
        player.update(); // Update the player's position

        for (int i = 0; i < objects.size(); i++) {
            GameObject obj = objects.get(i);
            obj.update(); // Update the object's position

            // Check for collisions
            if (player.getBounds().intersects(obj.getBounds())) {
                if (obj instanceof HealthyFood) {
                    score += 10; // Gain points for healthy food
                } else if (obj instanceof UnhealthyFood) {
                    endGame(); // End the game if the player hits unhealthy food
                } else if (obj instanceof Dumbbell) {
                    score *= 10; // Multiply the score by 10
                }
                objects.remove(i); // Remove the object after collision
                i--;
            }

            // Remove objects that fall out of the screen
            if (obj.isOutOfScreen()) {
                objects.remove(i);
                i--;
            }
        }
    }

    private void spawnRandomObject() {
        Random rand = new Random();
        int lane = rand.nextInt(3); // Randomly choose one of the three lanes
        int x = lane * (800 / 3) + (800 / 6); // Center of the lane

        GameObject obj;
        int chance = rand.nextInt(100); // Generate a random number between 0 and 99

        // Ensure dumbbells do not appear in the first 20 seconds of the game
        long elapsedTime = System.currentTimeMillis() - gameStartTime;
        if (elapsedTime < 20000) { // 20 seconds
            if (rand.nextBoolean()) {
                obj = new HealthyFood(x, 0); // Spawn healthy food
            } else {
                obj = new UnhealthyFood(x, 0); // Spawn unhealthy food
            }
        } else {
            // After 20 seconds, allow dumbbells to spawn with a lower probability
            if (chance < 80) { // 80% chance to spawn healthy or unhealthy food
                if (rand.nextBoolean()) {
                    obj = new HealthyFood(x, 0); // Spawn healthy food
                } else {
                    obj = new UnhealthyFood(x, 0); // Spawn unhealthy food
                }
            } else { // 20% chance to spawn a dumbbell
                obj = new Dumbbell(x, 0); // Spawn a dumbbell
            }
        }

        objects.add(obj);
    }

    public int getScore() {
        return score;
    }

    public void endGame() {
        gameOver = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player
        player.draw(g);

        // Draw game objects
        for (GameObject obj : objects) {
            obj.draw(g);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Healthy: " + score, 10, 20);

        // Draw game-over message
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over!", 300, 300);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            player.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            player.moveRight();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            movingLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            movingRight = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
