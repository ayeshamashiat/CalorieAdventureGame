import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class UnhealthyFood extends GameObject {
    private BufferedImage image;
    private static final String[] VARIETIES = {"Burger", "Pizza", "Donut", "Fries", "Cupcake", "Chocolate"};
    
    // Variety custom colors
    private Color mainColor;
    private Color detailColor;

    public UnhealthyFood(int x, int y) {
        super(x, y);
        Random rand = new Random();
        this.type = VARIETIES[rand.nextInt(VARIETIES.length)];
        
        setupVarietyColors();
        loadImage();
    }

    private void setupVarietyColors() {
        switch (type) {
            case "Burger":
                mainColor = new Color(245, 158, 11);
                detailColor = new Color(120, 53, 4);
                break;
            case "Pizza":
                mainColor = new Color(251, 191, 36);
                detailColor = new Color(220, 38, 38);
                break;
            case "Donut":
                mainColor = new Color(244, 63, 94);
                detailColor = new Color(253, 224, 71);
                break;
            case "Fries":
                mainColor = new Color(239, 68, 68);
                detailColor = new Color(250, 204, 21);
                break;
            case "Cupcake":
                mainColor = new Color(236, 72, 153);
                detailColor = new Color(146, 64, 14);
                break;
            case "Chocolate":
                mainColor = new Color(69, 26, 3);
                detailColor = new Color(229, 231, 235);
                break;
        }
    }

    private void loadImage() {
        try {
            image = ImageIO.read(getClass().getResource("/resources/unhealthy_food.png"));
        } catch (Exception e) {
            // Quietly fallback
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        drawPixelArt(g2d);
    }

    private void drawPixelArt(Graphics2D g) {
        int px = x - size / 2;
        int py = y;
        int p = size / 10;

        g.setColor(new Color(0, 0, 0, 50));
        g.fillOval(px, py + size - 8, size, 8);

        double sway = Math.sin(bobPhase + y * 0.05) * 4;
        px += (int) sway;

        java.awt.geom.AffineTransform oldTransform = g.getTransform();
        g.translate(px + size/2, py + size/2);
        g.rotate(Math.sin(bobPhase + y * 0.03) * 0.2);
        
        int rx = -size/2;
        int ry = -size/2;

        switch (type) {
            case "Burger":
                g.setColor(mainColor);
                g.fillArc(rx + p, ry + p, 8*p, 6*p, 0, 180);
                g.setColor(new Color(34, 197, 94));
                g.fillRect(rx + p, ry + 4*p, 8*p, p);
                g.setColor(new Color(253, 224, 71));
                int[] xc = {rx + p, rx + 9*p, rx + 5*p};
                int[] yc = {ry + 5*p, ry + 5*p, ry + 7*p};
                g.fillPolygon(xc, yc, 3);
                g.setColor(detailColor);
                g.fillRect(rx + 2*p, ry + 5*p, 6*p, 2*p);
                g.setColor(mainColor);
                g.fillRect(rx + 2*p, ry + 7*p, 6*p, p);
                break;

            case "Pizza":
                g.setColor(new Color(180, 83, 9));
                int[] xcr = {rx, rx + 10*p, rx + 5*p};
                int[] ycr = {ry + 2*p, ry + 2*p, ry + 10*p};
                g.fillPolygon(xcr, ycr, 3);
                g.setColor(mainColor);
                int[] xch = {rx + p, rx + 9*p, rx + 5*p};
                int[] ych = {ry + 3*p, ry + 3*p, ry + 9*p};
                g.fillPolygon(xch, ych, 3);
                g.setColor(detailColor);
                g.fillRect(rx + 4*p, ry + 4*p, p, p);
                g.fillRect(rx + 3*p, ry + 6*p, p, p);
                g.fillRect(rx + 6*p, ry + 5*p, p, p);
                break;

            case "Donut":
                g.setColor(new Color(251, 191, 36));
                g.fillOval(rx + p, ry + p, 8*p, 8*p);
                g.setColor(mainColor);
                g.fillOval(rx + 2*p, ry + 2*p, 6*p, 6*p);
                g.setColor(detailColor);
                g.fillRect(rx + 3*p, ry + 3*p, p, p);
                g.setColor(Color.WHITE);
                g.fillRect(rx + 6*p, ry + 4*p, p, p);
                g.setColor(new Color(56, 189, 248));
                g.fillRect(rx + 4*p, ry + 6*p, p, p);
                g.setColor(new Color(15, 23, 42));
                g.fillOval(rx + 4*p, ry + 4*p, 2*p, 2*p);
                break;

            case "Fries":
                g.setColor(detailColor);
                g.fillRect(rx + 2*p, ry, p, 5*p);
                g.fillRect(rx + 4*p, ry + p, p, 4*p);
                g.fillRect(rx + 5*p, ry - p, p, 6*p);
                g.fillRect(rx + 7*p, ry + p, p, 4*p);
                g.setColor(mainColor);
                int[] xBox = {rx + p, rx + 9*p, rx + 8*p, rx + 2*p};
                int[] yBox = {ry + 4*p, ry + 4*p, ry + 9*p, ry + 9*p};
                g.fillPolygon(xBox, yBox, 4);
                g.setColor(new Color(253, 224, 71));
                g.fillRect(rx + 4*p, ry + 6*p, 2*p, 2*p);
                break;

            case "Cupcake":
                g.setColor(detailColor);
                int[] xCup = {rx + 2*p, rx + 8*p, rx + 7*p, rx + 3*p};
                int[] yCup = {ry + 5*p, ry + 5*p, ry + 9*p, ry + 9*p};
                g.fillPolygon(xCup, yCup, 4);
                g.setColor(new Color(74, 30, 4));
                g.drawLine(rx + 4*p, ry + 5*p, rx + 4*p, ry + 8*p);
                g.drawLine(rx + 6*p, ry + 5*p, rx + 6*p, ry + 8*p);
                g.setColor(mainColor);
                g.fillOval(rx + p, ry + 2*p, 8*p, 4*p);
                g.fillOval(rx + 2*p, ry + p, 6*p, 3*p);
                g.setColor(new Color(220, 38, 38));
                g.fillOval(rx + 4*p, ry, 2*p, 2*p);
                break;

            case "Chocolate":
                g.setColor(detailColor);
                g.fillRect(rx + 2*p, ry + 4*p, 6*p, 5*p);
                g.setColor(mainColor);
                g.fillRect(rx + 2*p, ry + p, 6*p, 3*p);
                g.setColor(new Color(40, 15, 2));
                g.drawLine(rx + 4*p, ry + p, rx + 4*p, ry + 4*p);
                g.drawLine(rx + 6*p, ry + p, rx + 6*p, ry + 4*p);
                g.drawLine(rx + 2*p, ry + 2*p, rx + 8*p, ry + 2*p);
                g.setColor(new Color(220, 38, 38));
                g.fillRect(rx + 2*p, ry + 5*p, 6*p, 2*p);
                break;
        }

        g.setTransform(oldTransform);
    }

    @Override
    public void update(double speedMultiplier) {
        y += (int) (5 * speedMultiplier);
        bobPhase += 0.05;
    }
}
