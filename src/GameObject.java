import java.awt.*;

public abstract class GameObject {
    protected int x, y;
    protected String type = "";
    protected double bobPhase = Math.random() * Math.PI * 2; // Random start phase for bobbing
    protected double rotation = 0;
    protected int size = 44; // Pixel size for rendering

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Graphics g);

    public abstract void update();

    public String getType() {
        return type;
    }

    public boolean isOutOfScreen() {
        return y > 620; // Check if the object is out of the screen
    }

    public Rectangle getBounds() {
        return new Rectangle(x - size / 2, y, size, size); // Centered bounding box
    }

    // Utility: draw a filled pixel-art square at a given grid position within the object
    protected void drawPixel(Graphics g, int px, int py, int pixelSize, Color color) {
        g.setColor(color);
        g.fillRect(px, py, pixelSize, pixelSize);
    }
}
