import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Dumbbell extends GameObject {
    private BufferedImage image;
    private double sparklePhase = 0;

    public Dumbbell(int x, int y) {
        super(x, y);
        this.type = "Dumbbell";
        loadImage();
    }

    private void loadImage() {
        try {
            image = ImageIO.read(getClass().getResource("/resources/dumbbell.png"));
        } catch (Exception e) {
            // Quietly fallback
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Sparkle Glow behind the dumbbell
        drawSparkles(g2d);
        
        drawPixelArt(g2d);
    }

    private void drawSparkles(Graphics2D g) {
        int px = x;
        int py = y + size/2;
        sparklePhase += 0.15;
        
        // Pulsing halo glow
        int glowSize = size + (int) (Math.sin(sparklePhase) * 12);
        g.setColor(new Color(253, 224, 71, 40)); // Golden soft yellow glow
        g.fillOval(px - glowSize/2, py - glowSize/2, glowSize, glowSize);
        
        // Small floating golden stars
        g.setColor(new Color(254, 240, 138));
        int starDist = (int) (22 + Math.sin(sparklePhase) * 6);
        double angle1 = sparklePhase;
        double angle2 = sparklePhase + Math.PI;
        
        g.fillRect((int) (px + Math.cos(angle1) * starDist) - 2, (int) (py + Math.sin(angle1) * starDist) - 2, 4, 4);
        g.fillRect((int) (px + Math.cos(angle2) * starDist) - 2, (int) (py + Math.sin(angle2) * starDist) - 2, 4, 4);
    }

    private void drawPixelArt(Graphics2D g) {
        int px = x - size / 2;
        int py = y;
        int p = size / 10; // Grid unit size (10x10 grid)

        // Draw shadow under the item
        g.setColor(new Color(0, 0, 0, 60));
        g.fillOval(px, py + size - 8, size, 8);

        // Sway motion
        double sway = Math.sin(bobPhase + y * 0.05) * 3;
        px += (int) sway;

        // Rotation
        java.awt.geom.AffineTransform oldTransform = g.getTransform();
        g.translate(px + size/2, py + size/2);
        g.rotate(bobPhase * 0.4); // Spin faster!
        
        int rx = -size/2;
        int ry = -size/2;

        // Draw Dumbbell Pixel Art (Sleek gray and steel design)
        
        // Steel Bar connecting the weights
        g.setColor(new Color(156, 163, 175)); // Steel silver
        g.fillRect(rx + 2*p, ry + 4*p, 6*p, 2*p);
        
        // Left plate stack
        g.setColor(new Color(55, 65, 81)); // Dark steel
        g.fillRect(rx + p, ry + 2*p, 2*p, 6*p);
        g.setColor(new Color(75, 85, 99)); // Medium steel
        g.fillRect(rx + 2*p, ry + 3*p, p, 4*p);
        
        // Right plate stack
        g.setColor(new Color(55, 65, 81)); // Dark steel
        g.fillRect(rx + 7*p, ry + 2*p, 2*p, 6*p);
        g.setColor(new Color(75, 85, 99)); // Medium steel
        g.fillRect(rx + 7*p, ry + 3*p, p, 4*p);
        
        // Highlights on the plates
        g.setColor(new Color(209, 213, 219)); // Light highlight
        g.fillRect(rx + p, ry + 2*p, p, p);
        g.fillRect(rx + 7*p, ry + 2*p, p, p);
        
        // Red tape in middle of bar
        g.setColor(new Color(239, 68, 68));
        g.fillRect(rx + 4*p, ry + 4*p, 2*p, 2*p);

        g.setTransform(oldTransform);
    }

    @Override
    public void update() {
        y += 7; // Dumbbells fall faster as a challenging power-up
        bobPhase += 0.08;
    }
}
