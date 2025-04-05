import javax.swing.*;
import java.awt.*;

public class HealthyFood extends GameObject {
    private Image image;

    public HealthyFood(int x, int y) {
        super(x, y);
        image = new ImageIcon("resources/healthy_food.png").getImage();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }
}
