import java.awt.*;

public class Obstacle extends GameObject {

    public Obstacle(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(x, y, width, height);
    }
}
