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
    
    // Core game additions
    private int lives = 3;
    private final int maxLives = 3;
    private long immunityEndTime = 0; // Dumbbell 15s immunity
    private long invulnerabilityEndTime = 0; // 1.2s post-hit invincibility
    
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

            // Progressive spawn rate speeds up based on score
            // Starts at 2000ms, drops down to 600ms as score increases!
            int spawnInterval = Math.max(600, 2000 - (score / 30) * 100);
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

        // Progressive Difficulty speed multiplier based on score
        double speedMultiplier = 1.0 + (score / 150.0);

        // Update starfield speed dynamically as game speeds up
        for (Star star : stars) {
            star.y += star.speed * 1.5 * Math.min(2.5, speedMultiplier);
            if (star.y > 600) {
                star.y = 0;
                star.x = rand.nextInt(800);
            }
        }

        // Scroll lane lines
        laneLineOffset = (laneLineOffset + 4 * Math.min(2.5, speedMultiplier)) % 40;

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

        long currentTime = System.currentTimeMillis();
        boolean hasImmunity = currentTime < immunityEndTime;
        boolean hasInvulnerability = currentTime < invulnerabilityEndTime;

        // Update falling game objects
        for (int i = 0; i < objects.size(); i++) {
            GameObject obj = objects.get(i);
            obj.update(speedMultiplier); // Pass progressive speed multiplier

            // Collision check with lenient bounding boxes
            if (player.getBounds().intersects(obj.getBounds())) {
                triggerCollectionJuice(obj);
                
                if (obj instanceof HealthyFood) {
                    // Double score if immunity power-up is active!
                    score += hasImmunity ? 20 : 10;
                } else if (obj instanceof UnhealthyFood) {
                    if (hasImmunity) {
                        // Deflect unhealthy food with gold sparkle splash, no life loss!
                        score += 5; // Reward with +5 points for deflecting junk!
                        triggerScreenShake(4, 5);
                    } else if (hasInvulnerability) {
                        // Skip damage during standard post-damage frames
                    } else {
                        // Lose a life
                        lives--;
                        triggerScreenShake(15, 10);
                        if (lives <= 0) {
                            endGame();
                        } else {
                            // Brief recovery invulnerability to prevent chain hits
                            invulnerabilityEndTime = currentTime + 1200;
                        }
                    }
                } else if (obj instanceof Dumbbell) {
                    // Score boost! Adds +50 points
                    score += 50;
                    // Grant 15 seconds of complete immunity to unhealthy foods
                    immunityEndTime = currentTime + 15000;
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
        long currentTime = System.currentTimeMillis();
        
        if (obj instanceof HealthyFood) {
            pColor = (currentTime < immunityEndTime) ? new Color(253, 224, 71) : new Color(74, 222, 128); // Golden if double score!
        } else if (obj instanceof UnhealthyFood) {
            pColor = (currentTime < immunityEndTime) ? new Color(253, 224, 71) : new Color(239, 68, 68); // Gold if deflected!
            count = (currentTime < immunityEndTime) ? 20 : 35;
        } else if (obj instanceof Dumbbell) {
            pColor = new Color(253, 224, 71); // Gold Sparkles
            count = 25;
        }

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

        if (elapsedTime < 12000) {
            if (rand.nextBoolean()) {
                obj = new HealthyFood(x, -50);
            } else {
                obj = new UnhealthyFood(x, -50);
            }
        } else {
            // Dumbbells spawn rate increases slightly at high difficulty
            int dumbbellChance = Math.min(30, 15 + (score / 150) * 3);
            if (chance < (100 - dumbbellChance)) {
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
        Graphics2D g2d = (Graphics2D) g;
        int offsetX = 0;
        int offsetY = 0;
        if (shakeDuration > 0) {
            offsetX = rand.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
            offsetY = rand.nextInt(shakeIntensity * 2 + 1) - shakeIntensity;
        }

        g2d.setColor(new Color(15, 23, 42));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.translate(offsetX, offsetY);

        // Starfield
        g2d.setColor(new Color(255, 255, 255, 180));
        for (Star star : stars) {
            g2d.fillRect((int) star.x, (int) star.y, (int) star.speed, (int) star.speed);
        }

        // Lanes
        g2d.setColor(new Color(51, 65, 85));
        for (int l = 1; l < 3; l++) {
            int lx = l * (800 / 3);
            for (int y = (int) laneLineOffset - 40; y < 600; y += 40) {
                g2d.fillRect(lx - 2, y, 4, 20);
            }
        }

        // Draw objects
        for (GameObject obj : objects) {
            obj.draw(g2d);
        }

        // Blinking indicator during post-damage invulnerability frames
        long currentTime = System.currentTimeMillis();
        boolean isBlinking = (currentTime < invulnerabilityEndTime) && ((currentTime / 150) % 2 == 0);
        
        if (!isBlinking) {
            player.draw(g2d);
        }

        // Glowing shield circle around the player during Dumbbell immunity
        if (currentTime < immunityEndTime) {
            g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{9}, 0));
            // Rotate the shield over time
            double shieldRot = (currentTime / 200.0) % (Math.PI * 2);
            java.awt.geom.AffineTransform oldT = g2d.getTransform();
            
            Rectangle pBounds = player.getBounds();
            g2d.translate(pBounds.x + pBounds.width / 2.0, pBounds.y + pBounds.height / 2.0);
            g2d.rotate(shieldRot);
            
            // Neon gold color
            g2d.setColor(new Color(253, 224, 71, 180));
            g2d.drawOval(-36, -36, 72, 72);
            
            g2d.setTransform(oldT);
            g2d.setStroke(new BasicStroke(1)); // Reset
        }

        // Draw particles
        for (Particle p : particles) {
            p.draw(g2d);
        }

        g2d.translate(-offsetX, -offsetY);

        // Draw HUD overlay
        drawHUD(g2d);

        if (gameOver) {
            drawGameOverScreen(g2d);
        }
    }

    private void drawHUD(Graphics2D g) {
        // Score HUD box
        g.setColor(new Color(30, 41, 59, 220));
        g.fillRect(10, 10, 200, 50);
        g.setColor(new Color(99, 102, 241));
        g.drawRect(10, 10, 200, 50);

        // Gold trophy icon
        g.setColor(new Color(253, 224, 71));
        g.fillRect(22, 22, 16, 12);
        g.fillRect(28, 34, 4, 8);
        g.fillRect(24, 42, 12, 4);
        
        g.setColor(Color.WHITE);
        g.setFont(retroFont);
        g.drawString("SCORE: " + score, 48, 42);

        // LIVES System HUD Box
        g.setColor(new Color(30, 41, 59, 220));
        g.fillRect(220, 10, 150, 50);
        g.setColor(new Color(239, 68, 68));
        g.drawRect(220, 10, 150, 50);

        // Draw pixel-art hearts based on current lives
        for (int i = 0; i < maxLives; i++) {
            int hx = 235 + i * 40;
            int hy = 25;
            if (i < lives) {
                drawPixelHeart(g, hx, hy);
            } else {
                // Empty heart box outline
                g.setColor(new Color(71, 85, 105));
                g.drawRect(hx + 1, hy, 10, 10);
            }
        }

        // Immunity timer progress bar at the top center/right
        long currentTime = System.currentTimeMillis();
        if (currentTime < immunityEndTime) {
            double remainingSeconds = (immunityEndTime - currentTime) / 1000.0;
            
            g.setColor(new Color(30, 41, 59, 220));
            g.fillRect(380, 10, 200, 50);
            g.setColor(new Color(253, 224, 71));
            g.drawRect(380, 10, 200, 50);

            // Shrunk gold bar
            int barWidth = (int) (180 * (remainingSeconds / 15.0));
            g.fillRect(390, 32, barWidth, 12);

            g.setFont(retroFontSmall);
            g.setColor(Color.WHITE);
            g.drawString(String.format("SHIELD: %.1fs", remainingSeconds), 390, 26);
        } else {
            // General guidance message
            g.setFont(retroFontSmall);
            g.setColor(new Color(148, 163, 184));
            g.drawString("Junk cuts a Life! Dumbbells grant Shield!", 390, 36);
        }
    }

    private void drawPixelHeart(Graphics2D g, int hx, int hy) {
        g.setColor(new Color(239, 68, 68)); // Red
        g.fillRect(hx + 2, hy, 3, 2);
        g.fillRect(hx + 7, hy, 3, 2);
        g.fillRect(hx + 1, hy + 2, 10, 3);
        g.fillRect(hx + 2, hy + 5, 8, 2);
        g.fillRect(hx + 4, hy + 7, 4, 2);
        g.fillRect(hx + 5, hy + 9, 2, 2);
    }

    private void drawGameOverScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(new Color(30, 41, 59, 240));
        g.fillRect(150, 180, 500, 240);
        g.setColor(new Color(239, 68, 68));
        g.drawRect(150, 180, 500, 240);

        g.setFont(retroFontLarge);
        g.setColor(new Color(239, 68, 68));
        String text = "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, 400 - textWidth / 2, 250);

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

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

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
            vy += 0.1;
            life--;
        }

        void draw(Graphics2D g) {
            int alpha = (int) ((double) life / maxLife * 255);
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
            g.fillRect((int) x, (int) y, size, size);
        }
    }
}
