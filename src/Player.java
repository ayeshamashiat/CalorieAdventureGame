import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private int lane = 1; // Start in the center lane (0 = left, 1 = center, 2 = right)
    private final int y = 500; // Fixed vertical position
    private final int width = 60, height = 60;
    private final int laneWidth = 800 / 3; // Divide the screen into 3 lanes
    private int x; // Current x position of the player
    private BufferedImage catImage; // Image for the player
    private long lastMoveTime = 0; // Track the last time the player moved
    private final int moveCooldown = 200; // Cooldown in milliseconds

    public Player() {
        loadCatImage(); // Load the cat image
        update(); // Initialize the player's position
    }

    private void loadCatImage() {
        try {
            // Load the cat image from the resources folder
            catImage = ImageIO.read(getClass().getResource("/resources/cat.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: Could not load cat image.");
        }
    }

    public void moveLeft() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime >= moveCooldown && lane > 0) {
            lane--; // Move to the left lane
            lastMoveTime = currentTime; // Update the last move time
        }
    }

    public void moveRight() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime >= moveCooldown && lane < 2) {
            lane++; // Move to the right lane
            lastMoveTime = currentTime; // Update the last move time
        }
    }

    public void update() {
        // Update the player's x position based on the current lane
        x = lane * laneWidth + (laneWidth - width) / 2;
    }

    public void draw(Graphics g) {
        if (catImage != null) {
            g.drawImage(catImage, x, y, width, height, null); // Draw the cat image
        } else {
            // Fallback if the image is not loaded
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
