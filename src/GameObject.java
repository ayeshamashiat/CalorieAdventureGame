import java.awt.*;

public abstract class GameObject {
    protected int x, y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Graphics g);

    public abstract void update();

    public boolean isOutOfScreen() {
        return y > 600; // Check if the object is out of the screen
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 50, 50); // Bounding box for collision detection
    }
}
