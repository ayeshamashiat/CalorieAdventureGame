import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class HealthyFood extends GameObject {
    private BufferedImage image;
    private static final String[] VARIETIES = {"Apple", "Broccoli", "Carrot", "Banana", "Blueberries", "Salad"};
    
    // Custom colors for pixel art
    private Color mainColor;
    private Color detailColor;
    
    public HealthyFood(int x, int y) {
        super(x, y);
        // Randomly pick a variety
        Random rand = new Random();
        this.type = VARIETIES[rand.nextInt(VARIETIES.length)];
        
        // Setup colors based on variety
        setupVarietyColors();
        
        loadImage();
    }

    private void setupVarietyColors() {
        switch (type) {
            case "Apple":
                mainColor = new Color(220, 38, 38);   // Vibrant Red
                detailColor = new Color(22, 163, 74);  // Green leaf
                break;
            case "Broccoli":
                mainColor = new Color(21, 128, 61);   // Dark Green
                detailColor = new Color(74, 222, 128); // Light Green florets
                break;
            case "Carrot":
                mainColor = new Color(249, 115, 22);  // Orange
                detailColor = new Color(34, 197, 94);  // Green top
                break;
            case "Banana":
                mainColor = new Color(234, 179, 8);   // Yellow
                detailColor = new Color(113, 63, 18);  // Brown tips
                break;
            case "Blueberries":
                mainColor = new Color(37, 99, 235);   // Royal Blue
                detailColor = new Color(29, 78, 216);  // Dark Blue shadows
                break;
            case "Salad":
                mainColor = new Color(74, 222, 128);  // Lettuce green
                detailColor = new Color(239, 68, 68);  // Tomato red dots
                break;
        }
    }

    private void loadImage() {
        try {
            // Load the default image or a specific image if available
            image = ImageIO.read(getClass().getResource("/resources/healthy_food.png"));
        } catch (Exception e) {
            // Quietly fall back to pixel art rendering
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // If image loading fails or we want variety graphics, draw our beautifully custom-designed pixel art!
        // We'll draw our rich pixel art for maximum detail and visual wow-factor!
        drawPixelArt(g2d);
    }

    private void drawPixelArt(Graphics2D g) {
        int px = x - size / 2;
        int py = y;
        int p = size / 10; // Grid unit size (10x10 grid)

        // Draw shadow under the item
        g.setColor(new Color(0, 0, 0, 50));
        g.fillOval(px, py + size - 8, size, 8);

        // Add a gentle floating/swaying effect to the items as they fall
        double sway = Math.sin(bobPhase + y * 0.05) * 4;
        px += (int) sway;

        // Apply a small rotation for dynamic look
        java.awt.geom.AffineTransform oldTransform = g.getTransform();
        g.translate(px + size/2, py + size/2);
        g.rotate(Math.sin(bobPhase + y * 0.03) * 0.2);
        
        int rx = -size/2;
        int ry = -size/2;

        switch (type) {
            case "Apple":
                // Core shape
                g.setColor(mainColor);
                g.fillOval(rx + p, ry + 2*p, 8*p, 7*p);
                // Indent on top & bottom
                g.setColor(new Color(153, 27, 27)); // Dark red outline
                g.drawOval(rx + p, ry + 2*p, 8*p, 7*p);
                // Leaf
                g.setColor(detailColor);
                g.fillRect(rx + 5*p, ry, 2*p, p);
                g.fillRect(rx + 6*p, ry + p, 2*p, p);
                // Stem
                g.setColor(new Color(120, 53, 4));
                g.fillRect(rx + 4*p, ry + p, p, 2*p);
                break;

            case "Broccoli":
                // Stem
                g.setColor(new Color(134, 239, 172));
                g.fillRect(rx + 4*p, ry + 5*p, 2*p, 4*p);
                // Dark green crown
                g.setColor(mainColor);
                g.fillOval(rx + 2*p, ry + p, 6*p, 5*p);
                g.fillOval(rx + p, ry + 3*p, 4*p, 4*p);
                g.fillOval(rx + 5*p, ry + 3*p, 4*p, 4*p);
                // Florets detail
                g.setColor(detailColor);
                g.fillOval(rx + 3*p, ry + 2*p, 2*p, 2*p);
                g.fillOval(rx + 5*p, ry + 3*p, 2*p, 2*p);
                break;

            case "Carrot":
                // Leafy green top
                g.setColor(detailColor);
                g.fillRect(rx + 6*p, ry, p, 3*p);
                g.fillRect(rx + 7*p, ry + p, 2*p, p);
                
                // Orange carrot body (diagonal carrot)
                g.setColor(mainColor);
                int[] xPoints = {rx + 7*p, rx + 2*p, rx + p, rx + 2*p, rx + 6*p};
                int[] yPoints = {ry + 2*p, ry + 7*p, ry + 9*p, ry + 8*p, ry + 3*p};
                g.fillPolygon(xPoints, yPoints, 5);
                
                // Carrot ridges
                g.setColor(new Color(194, 65, 12));
                g.drawLine(rx + 4*p, ry + 5*p, rx + 5*p, ry + 4*p);
                g.drawLine(rx + 2*p, ry + 7*p, rx + 3*p, ry + 6*p);
                break;

            case "Banana":
                // Curved body
                g.setColor(mainColor);
                g.fillArc(rx, ry + p, 9*p, 9*p, 45, 90);
                g.setColor(Color.BLACK); // Background mask to make crescent
                g.fillArc(rx + p, ry, 9*p, 9*p, 45, 90);
                
                // Redraw banana with fill arc
                g.setColor(mainColor);
                g.fillOval(rx + p, ry + 3*p, 7*p, 4*p);
                // Tips
                g.setColor(detailColor);
                g.fillRect(rx + p, ry + 3*p, p, p);
                g.fillRect(rx + 7*p, ry + 5*p, p, p);
                break;

            case "Blueberries":
                // Three blueberries in a bunch
                g.setColor(mainColor);
                g.fillOval(rx + 2*p, ry + 4*p, 4*p, 4*p);
                g.fillOval(rx + 4*p, ry + p, 4*p, 4*p);
                g.fillOval(rx + p, ry + p, 4*p, 4*p);
                
                // Highlights
                g.setColor(new Color(147, 197, 253));
                g.fillRect(rx + 2*p, ry + 2*p, p, p);
                g.fillRect(rx + 5*p, ry + 2*p, p, p);
                g.fillRect(rx + 3*p, ry + 5*p, p, p);
                
                // Crown detail
                g.setColor(detailColor);
                g.fillRect(rx + 3*p, ry + p, p, p);
                g.fillRect(rx + 6*p, ry + p, p, p);
                break;

            case "Salad":
                // Bowl shape
                g.setColor(new Color(203, 213, 225)); // light gray bowl
                g.fillArc(rx, ry + 2*p, 10*p, 7*p, 180, 180);
                
                // Salad Greens inside
                g.setColor(mainColor);
                g.fillOval(rx + p, ry + p, 8*p, 3*p);
                g.fillOval(rx + 2*p, ry, 6*p, 3*p);
                
                // Tomato toppings
                g.setColor(detailColor);
                g.fillRect(rx + 3*p, ry + p, p, p);
                g.fillRect(rx + 6*p, ry + 2*p, p, p);
                break;
        }

        g.setTransform(oldTransform);
    }

    @Override
    public void update() {
        y += 5; // Move down
        bobPhase += 0.05; // increment float phase
    }
}
