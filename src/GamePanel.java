import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    private int score = 0;
    private boolean gameOver = false;
    private Runnable onGameOver;
    private List<GameObject> objects = new ArrayList<>();
    private Player player;
    private long gameStartTime = System.currentTimeMillis();
    
    // Aesthetic additions
    private List<Particle> particles = new ArrayList<>();
    private List<Star> stars = new ArrayList<>();
    private int shakeDuration = 0;
    private int shakeIntensity = 0;
    private Random rand = new Random();
    
    // Lane lines anim
    private double laneLineOffset = 0;

    // Custom Font loading
    private Font retroFont;
    private Font retroFontLarge;
    private Font retroFontSmall;

    public GamePanel(Runnable onGameOver) {
        this.onGameOver = onGameOver;
        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(15, 23, 42)); // Deep Slate/Space dark blue
        setFocusable(true);
        requestFocusInWindow();

        player = new Player();
        addKeyListener(this);
        
        // Initialize starfield background
        for (int i = 0; i < 60; i++) {
            stars.add(new Star(rand.nextInt(800), rand.nextInt(600), rand.nextFloat() * 2 + 1));
        }

        // Setup Fonts
        retroFont = new Font("Courier New", Font.BOLD, 22);
        retroFontLarge = new Font("Courier New", Font.BOLD, 48);
        retroFontSmall = new Font("Courier New", Font.BOLD, 14);

        new Thread(this).start();
    }

    @Override
    public void run() {
        long lastSpawnTime = System.currentTimeMillis();

        while (!gameOver) {
            updateGame();
            repaint();

            // Spawn rate speeds up slightly based on score
            int spawnInterval = Math.max(800, 1800 - (score / 100) * 100);
            if (System.currentTimeMillis() - lastSpawnTime >= spawnInterval) {
                spawnRandomObject();
                lastSpawnTime = System.currentTimeMillis();
            }

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Delay slightly before showing Game Over input dialog so player sees collision & screen shake
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (onGameOver != null) {
            onGameOver.run();
        }
    }

    private void updateGame() {
        player.update();

        // Update starfield
        for (Star star : stars) {
            star.y += star.speed * 1.5;
            if (star.y > 600) {
                star.y = 0;
                star.x = rand.nextInt(800);
            }
        }

        // Scroll lane lines
        laneLineOffset = (laneLineOffset + 4) % 40;

        // Update particles
        Iterator<Particle> pIterator = particles.iterator();
        while (pIterator.hasNext()) {
            Particle p = pIterator.next();
            p.update();
            if (p.life <= 0) {
                pIterator.remove();
            }
        }

        // Update screen shake
        if (shakeDuration > 0) {
            shakeDuration--;
        }

        // Update falling game objects
        for (int i = 0; i < objects.size(); i++) {
            GameObject obj = objects.get(i);
            obj.update();

            // Collision check with lenient bounding boxes
            if (player.getBounds().intersects(obj.getBounds())) {
                triggerCollectionJuice(obj);
                
                if (obj instanceof HealthyFood) {
                    score += 10;
                } else if (obj instanceof UnhealthyFood) {
                    triggerScreenShake(20, 15);
                    endGame();
                } else if (obj instanceof Dumbbell) {
                    score *= 2; // Doubling feels more balanced and fun than times 10
                    if (score == 0) score = 10; // Ensure 0 score gets some points
                    triggerScreenShake(8, 8);
                }
                objects.remove(i);
                i--;
            } else if (obj.isOutOfScreen()) {
                objects.remove(i);
                i--;
            }
        }
    }

    private void triggerCollectionJuice(GameObject obj) {
        Color pColor = Color.GREEN;
        int count = 15;
        
        if (obj instanceof HealthyFood) {
            pColor = new Color(74, 222, 128); // Green
        } else if (obj instanceof UnhealthyFood) {
            pColor = new Color(239, 68, 68); // Red
            count = 35;
        } else if (obj instanceof Dumbbell) {
            pColor = new Color(253, 224, 71); // Gold Sparkles
            count = 25;
        }

        // Spawn beautiful radial burst particles at collision center
        int cx = obj.x;
        int cy = obj.y + 20;
        for (int k = 0; k < count; k++) {
            double angle = rand.nextDouble() * Math.PI * 2;
            double speed = rand.nextDouble() * 5 + 2;
            particles.add(new Particle(cx, cy, Math.cos(angle) * speed, Math.sin(angle) * speed, pColor, rand.nextInt(3) + 4));
        }
    }

    private void triggerScreenShake(int intensity, int duration) {
        this.shakeIntensity = intensity;
        this.shakeDuration = duration;
    }

    private void spawnRandomObject() {
        int lane = rand.nextInt(3);
        int x = lane * (800 / 3) + (800 / 6);

        GameObject obj;
        int chance = rand.nextInt(100);
        long elapsedTime = System.currentTimeMillis() - gameStartTime;

        if (elapsedTime < 15000) {
            // First 15 seconds: Healthy or Unhealthy Food
            if (rand.nextBoolean()) {
                obj = new HealthyFood(x, -50);
            } else {
                obj = new UnhealthyFood(x, -50);
            }
        } else {
            // After 15 seconds: allow Dumbbells to spawn with 15% probability
            if (chance < 85) {
                if (rand.nextBoolean()) {
                    obj = new HealthyFood(x, -50);
                } else {
                    obj = new UnhealthyFood(x, -50);
                }
            } else {
                obj = new Dumbbell(x, -50);
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
        // Save original graphics and apply screen shake if active
        Graphics2D g2d = (Graphics2D) g;
        int offsetX = 0;
        int offsetY = 0;
        if (shakeDuration > 0) {
            offsetX = rand.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
            offsetY = rand.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
        }

        // Clear panel background
        g2d.setColor(new Color(15, 23, 42));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Apply shake transform to subsequent draws
        g2d.translate(offsetX, offsetY);

        // Draw starfield
        g2d.setColor(new Color(255, 255, 255, 180));
        for (Star star : stars) {
            g2d.fillRect((int) star.x, (int) star.y, (int) star.speed, (int) star.speed);
        }

        // Draw lanes boundaries with retro dashed pixel-style
        g2d.setColor(new Color(51, 65, 85));
        for (int l = 1; l < 3; l++) {
            int lx = l * (800 / 3);
            for (int y = (int) laneLineOffset - 40; y < 600; y += 40) {
                g2d.fillRect(lx - 2, y, 4, 20); // pixel dashed line
            }
        }

        // Draw game objects
        for (GameObject obj : objects) {
            obj.draw(g2d);
        }

        // Draw player
        player.draw(g2d);

        // Draw particles
        for (Particle p : particles) {
            p.draw(g2d);
        }

        // Reset translate to draw non-shaking HUD overlays
        g2d.translate(-offsetX, -offsetY);

        // Draw HUD panel
        drawHUD(g2d);

        // Draw game-over overlay with big pixel font
        if (gameOver) {
            drawGameOverScreen(g2d);
        }
    }

    private void drawHUD(Graphics2D g) {
        // Top HUD bar
        g.setColor(new Color(30, 41, 59, 220));
        g.fillRect(10, 10, 220, 50);
        g.setColor(new Color(99, 102, 241));
        g.drawRect(10, 10, 220, 50);

        // Gold trophy icon (pixel style)
        g.setColor(new Color(253, 224, 71));
        g.fillRect(25, 22, 16, 12);
        g.fillRect(31, 34, 4, 8);
        g.fillRect(27, 42, 12, 4);
        
        // Score text
        g.setColor(Color.WHITE);
        g.setFont(retroFont);
        g.drawString("SCORE: " + score, 55, 42);

        // Healthy theme message
        g.setFont(retroFontSmall);
        g.setColor(new Color(148, 163, 184));
        g.drawString("Catch: Healthy  Avoid: Junk", 490, 36);
    }

    private void drawGameOverScreen(Graphics2D g) {
        // Darken screen
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Center Box
        g.setColor(new Color(30, 41, 59, 240));
        g.fillRect(150, 180, 500, 240);
        g.setColor(new Color(239, 68, 68));
        g.drawRect(150, 180, 500, 240);

        // Title
        g.setFont(retroFontLarge);
        g.setColor(new Color(239, 68, 68));
        String text = "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, 400 - textWidth / 2, 250);

        // Subtext
        g.setFont(retroFont);
        g.setColor(Color.WHITE);
        String subtext = "Your Score: " + score;
        int subWidth = g.getFontMetrics().stringWidth(subtext);
        g.drawString(subtext, 400 - subWidth / 2, 310);

        g.setFont(retroFontSmall);
        g.setColor(new Color(148, 163, 184));
        String waitText = "Preparing high score registry...";
        int waitWidth = g.getFontMetrics().stringWidth(waitText);
        g.drawString(waitText, 400 - waitWidth / 2, 360);
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
        // Not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    // Helper classes for stars and particles
    private static class Star {
        double x, y;
        double speed;

        Star(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
        }
    }

    private static class Particle {
        double x, y;
        double vx, vy;
        Color color;
        int size;
        int life;
        int maxLife;

        Particle(double x, double y, double vx, double vy, Color color, int size) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = color;
            this.size = size;
            this.maxLife = new Random().nextInt(15) + 15;
            this.life = this.maxLife;
        }

        void update() {
            x += vx;
            y += vy;
            vy += 0.1; // gravity
            life--;
        }

        void draw(Graphics2D g) {
            // Fade particles out
            int alpha = (int) ((double) life / maxLife * 255);
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            g.fillRect((int) x, (int) y, size, size);
        }
    }
}
