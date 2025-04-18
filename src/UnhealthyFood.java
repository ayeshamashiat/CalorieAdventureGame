import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class UnhealthyFood extends GameObject {
    private BufferedImage image;

    public UnhealthyFood(int x, int y) {
        super(x, y);
        loadImage();
    }

    private void loadImage() {
        try {
            // Load the image for unhealthy food
            image = ImageIO.read(getClass().getResource("/resources/unhealthy_food.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load unhealthy food image.");
        }
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 50, 50, null); // Draw the image
        } else {
            // Fallback if the image is not loaded
            g.setColor(Color.RED);
            g.fillRect(x, y, 50, 50);
        }
    }

    @Override
    public void update() {
        y += 5; // Move the object down
    }
}
