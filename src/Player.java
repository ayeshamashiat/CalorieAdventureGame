import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Player {
    private int x, y, width, height;
    private int lane = 1; // 0 = left, 1 = center, 2 = right
    private Image image;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        image = new ImageIcon("resources/cat.png").getImage();
    }

    public void update() {
        x = lane * 800 / 3 + (800 / 3 - width) / 2;
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && lane > 0) lane--;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && lane < 2) lane++;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
