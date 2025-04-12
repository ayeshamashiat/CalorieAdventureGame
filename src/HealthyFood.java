import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class HealthyFood extends GameObject {
    private BufferedImage image;

    public HealthyFood(int x, int y) {
        super(x, y);
        loadImage();
    }

    private void loadImage() {
        try {
            // Load the image for healthy food
            image = ImageIO.read(getClass().getResource("/resources/healthy_food.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load healthy food image.");
        }
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 50, 50, null); // Draw the image
        } else {
            // Fallback if the image is not loaded
            g.setColor(Color.GREEN);
            g.fillRect(x, y, 50, 50);
        }
    }

    @Override
    public void update() {
        y += 5; // Move the object down
    }
}
