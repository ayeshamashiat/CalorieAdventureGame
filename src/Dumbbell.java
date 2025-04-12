import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Dumbbell extends GameObject {
    private BufferedImage image;

    public Dumbbell(int x, int y) {
        super(x, y);
        loadImage();
    }

    private void loadImage() {
        try {
            // Load the dumbbell image from the resources folder
            image = ImageIO.read(getClass().getResource("/resources/dumbbell.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load dumbbell image.");
        }
    }

    @Override
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, 50, 50, null); // Draw the dumbbell image
        } else {
            // Fallback if the image is not loaded
            g.setColor(Color.GRAY);
            g.fillRect(x, y, 50, 50);
        }
    }

    @Override
    public void update() {
        y += 10; // Move the dumbbell down faster than food
    }
}
