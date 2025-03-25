package com.game.CalorieAdventureGame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;

public class Food {
    private Texture foodTexture;  // Texture for the food item
    private Rectangle foodRectangle;  // Rectangle for collision detection
    private float x, y;  // Position of the food item
    private float speed = 200f;  // Speed at which the food falls
    private boolean isHealthy;  // Flag to determine if the food is healthy or unhealthy

    public Food(float screenWidth) {
        // Randomly set the x position
        this.x = MathUtils.random(0, screenWidth - 64);  // Assuming food texture width is 64
        this.y = 800;  // Starting y position (above the screen)
        
        // Randomly determine if the food is healthy or unhealthy
        this.isHealthy = MathUtils.randomBoolean();

        // Assign a texture based on whether it's healthy or not
        if (isHealthy) {
            foodTexture = new Texture("healthy_food.png");  // Replace with your healthy food texture
        } else {
            foodTexture = new Texture("unhealthy_food.png");  // Replace with your unhealthy food texture
        }

        // Initialize the rectangle for collision detection
        foodRectangle = new Rectangle(x, y, foodTexture.getWidth(), foodTexture.getHeight());
    }

    public Food(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(float delta) {
        // Move the food item downwards
        y -= speed * delta;
        foodRectangle.setPosition(x, y);  // Update the position of the rectangle

        // If food goes off the screen (below), reset its position to the top
        if (y < 0) {
            y = 800;  // Reset y position
            x = MathUtils.random(0, 800 - foodTexture.getWidth());  // Randomize x position
        }
    }

    public void render(SpriteBatch batch) {
        // Draw the food item on the screen
        batch.draw(foodTexture, x, y);
    }

    public Rectangle getBounds() {
        // Return the rectangle for collision detection with the player
        return foodRectangle;
    }

    public void dispose() {
        // Dispose of the texture to free up memory
        foodTexture.dispose();
    }

    public boolean isHealthy() {
        return isHealthy;
    }
}
