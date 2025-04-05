import java.awt.*;

public abstract class GameObject {
    protected int x, y, width, height;
    protected int speed = 4;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
    }

    public void update() {
        y += speed;
    }

    public abstract void draw(Graphics g);

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean isOutOfScreen() {
        return y > 600;
    }
}
