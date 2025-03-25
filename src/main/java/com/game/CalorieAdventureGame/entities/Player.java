package com.game.CalorieAdventureGame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Input;

public class Player {
    private Texture playerTexture;  // Texture for the player (cat)
    private Rectangle playerRectangle;  // Rectangle for collision detection
    private float x, y;  // Position of the player (cat)
    private float speed = 300f;  // Player speed (adjust as needed)

    public Player() {
        playerTexture = new Texture("cat.png");  // Replace with your cat image file
        x = Gdx.graphics.getWidth() / 2 - playerTexture.getWidth() / 2;  // Start at the center
        y = 100;  // Initial Y position (can be adjusted)
        playerRectangle = new Rectangle(x, y, playerTexture.getWidth(), playerTexture.getHeight());
    }

    public void update(float delta) {
        // Example movement logic
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerRectangle.x -= 200 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerRectangle.x += 200 * delta;
        }

        // Restrict movement within screen bounds
        if (playerRectangle.x < 0) playerRectangle.x = 0;
        if (playerRectangle.x > Gdx.graphics.getWidth() - playerRectangle.width) playerRectangle.x = Gdx.graphics.getWidth() - playerRectangle.width;
    }

    public void render(SpriteBatch batch) {
        // Draw the player (cat) on the screen
        batch.draw(playerTexture, playerRectangle.x, playerRectangle.y);
    }

    public Rectangle getBounds() {
        // Return the player's rectangle for collision detection
        return playerRectangle;
    }

    public void dispose() {
        playerTexture.dispose();  // Dispose of the texture when done
    }
}
