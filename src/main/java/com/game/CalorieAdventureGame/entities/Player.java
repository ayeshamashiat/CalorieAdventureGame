package com.game.CalorieAdventureGame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private Texture playerTexture;  // Texture for the player (cat)
    private Rectangle playerRectangle;  // Rectangle for collision detection
    private float x, y;  // Position of the player (cat)
    private float speed = 300f;  // Player speed (adjust as needed)

    public Player() {
        playerTexture = new Texture("cat.jpg");  // Replace with your cat image file
        x = Gdx.graphics.getWidth() / 2 - playerTexture.getWidth() / 2;  // Start at the center
        y = 100;  // Initial Y position (can be adjusted)
        playerRectangle = new Rectangle(x, y, playerTexture.getWidth(), playerTexture.getHeight());
    }

    public void update(float delta) {
        // Get user input and move the player
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            x -= speed * delta;  // Move left
        }
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            x += speed * delta;  // Move right
        }

        // Prevent player from going off-screen
        if (x < 0) {
            x = 0;
        }
        if (x > Gdx.graphics.getWidth() - playerTexture.getWidth()) {
            x = Gdx.graphics.getWidth() - playerTexture.getWidth();
        }

        // Update the player's rectangle for collision detection
        playerRectangle.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        // Draw the player (cat) on the screen
        batch.draw(playerTexture, x, y);
    }

    public Rectangle getBounds() {
        // Return the player's rectangle for collision detection
        return playerRectangle;
    }

    public void dispose() {
        playerTexture.dispose();  // Dispose of the texture when done
    }
}
