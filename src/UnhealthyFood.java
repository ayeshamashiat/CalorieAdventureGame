import java.awt.*;

import javax.swing.ImageIcon;

public class UnhealthyFood extends GameObject {
    private Image image;

    public UnhealthyFood(int x, int y) {
        super(x, y);
        image = new ImageIcon("resources/unhealthy_food.png").getImage();

    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
