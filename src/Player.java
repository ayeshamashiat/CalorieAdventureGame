import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private int lane = 1; // Start in the center lane (0 = left, 1 = center, 2 = right)
    private final int y = 480; // Fixed vertical position
    private final int width = 64, height = 64;
    private final int laneWidth = 800 / 3; // Divide the screen into 3 lanes
    private double x; // Current x position of the player (floating point for smooth movement)
    private double targetX; // Target x position based on lane
    private BufferedImage catImage; // Image for the player
    
    // Animation properties
    private double bobTime = 0;
    private double angle = 0;

    public Player() {
        loadCatImage(); // Load the cat image
        // Initialize position to the center lane
        targetX = lane * laneWidth + (laneWidth - width) / 2.0;
        x = targetX;
    }

    private void loadCatImage() {
        try {
            // Load the cat image from the resources folder
            catImage = ImageIO.read(getClass().getResource("/resources/cat.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Warning: Could not load cat image. Custom pixel-art fallback will be used.");
        }
    }

    public void moveLeft() {
        if (lane > 0) {
            lane--;
            updateTargetX();
        }
    }

    public void moveRight() {
        if (lane < 2) {
            lane++;
            updateTargetX();
        }
    }

    private void updateTargetX() {
        targetX = lane * laneWidth + (laneWidth - width) / 2.0;
    }

    public void update() {
        // Smoothly interpolate position towards target x position (lerp)
        double speed = 0.18; // Speed coefficient for smoothness
        x += (targetX - x) * speed;

        // Bobbing animation over time
        bobTime += 0.15;
        // Rotation based on movement direction
        double diff = targetX - x;
        angle = diff * 0.005; // Slight tilt in movement direction
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        // Enable anti-aliasing for smooth rotation
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int renderX = (int) x;
        int renderY = y + (int) (Math.sin(bobTime) * 4); // Bob up and down by 4 pixels

        // Save original transform
        java.awt.geom.AffineTransform oldTransform = g2d.getTransform();
        
        // Translate and rotate around the center of the player
        g2d.translate(renderX + width / 2.0, renderY + height / 2.0);
        g2d.rotate(angle);

        if (catImage != null) {
            // Draw the loaded cat image centered
            g2d.drawImage(catImage, -width / 2, -height / 2, width, height, null);
        } else {
            // Fallback: draw a beautiful pixel-art style cat using Java2D
            drawFallbackCat(g2d, -width / 2, -height / 2);
        }

        // Restore original transform
        g2d.setTransform(oldTransform);
    }

    private void drawFallbackCat(Graphics2D g, int rx, int ry) {
        // Pixel art style orange cat
        int p = 4; // grid pixel size (16x16 grid for 64x64 size)
        
        // Body (orange)
        g.setColor(new Color(235, 130, 45));
        g.fillRect(rx + 2*p, ry + 5*p, 12*p, 10*p);
        
        // Head / Ears
        g.fillRect(rx + 3*p, ry + p, 10*p, 5*p);
        g.fillRect(rx + 3*p, ry, p, p); // Left ear tip
        g.fillRect(rx + 12*p, ry, p, p); // Right ear tip
        
        // Belly (cream color)
        g.setColor(new Color(253, 230, 201));
        g.fillRect(rx + 5*p, ry + 8*p, 6*p, 7*p);
        
        // Eyes (black and white pixels)
        g.setColor(Color.WHITE);
        g.fillRect(rx + 5*p, ry + 2*p, p, p);
        g.fillRect(rx + 10*p, ry + 2*p, p, p);
        g.setColor(new Color(40, 40, 40));
        g.fillRect(rx + 5*p, ry + 3*p, p, p);
        g.fillRect(rx + 10*p, ry + 3*p, p, p);
        
        // Nose (pink)
        g.setColor(new Color(240, 110, 140));
        g.fillRect(rx + 7*p, ry + 3*p, 2*p, p);
        
        // Cheeks (blush pink)
        g.setColor(new Color(250, 160, 180));
        g.fillRect(rx + 4*p, ry + 4*p, p, p);
        g.fillRect(rx + 11*p, ry + 4*p, p, p);

        // Paws (white socks)
        g.setColor(Color.WHITE);
        g.fillRect(rx + 3*p, ry + 14*p, 2*p, 2*p);
        g.fillRect(rx + 11*p, ry + 14*p, 2*p, 2*p);
        
        // Tail
        g.setColor(new Color(235, 130, 45));
        g.fillRect(rx + p, ry + 8*p, p, 5*p);
        g.fillRect(rx, ry + 7*p, p, 2*p); // tail tip
    }

    public Rectangle getBounds() {
        // Return active bounds slightly padded for better gameplay feel
        return new Rectangle((int) x + 6, y + 6, width - 12, height - 12);
    }
}
