package com.game.CalorieAdventureGame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private Texture texture;
    private float x, y;
    private Rectangle bounds;
    private static final float SPEED = 300f;
    private static final float LANE_WIDTH = Gdx.graphics.getWidth() / 3f;
    private static final float scale = 0.1f;

    public Player() {
        texture = new Texture("cat.png");
        float width = texture.getWidth()*scale;
        float height = texture.getHeight()*scale;
        x = Gdx.graphics.getWidth() / 2f - width / 2f;
        y = 10; // Starting position near the bottom
        bounds = new Rectangle(x, y, width, height);
    }

    public void update(float delta) {
        // Example movement logic
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bounds.x -= SPEED * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bounds.x += SPEED * delta;
        }

        // Restrict movement within screen bounds
        if (bounds.x < 0) bounds.x = 0;
        if (bounds.x > Gdx.graphics.getWidth() - bounds.width) bounds.x = Gdx.graphics.getWidth() - bounds.width;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, bounds.x, bounds.y, bounds.width, bounds.height);
    }

    public boolean checkCollision(Food food) {
        return bounds.overlaps(food.getBounds());
    }

    public Rectangle getBounds() {
        // Return the player's rectangle for collision detection
        return bounds;
    }

    public void dispose() {
        texture.dispose();
    }
}
