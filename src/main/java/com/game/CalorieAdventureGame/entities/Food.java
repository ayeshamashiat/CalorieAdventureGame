package com.game.CalorieAdventureGame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Food {
    private Texture texture;
    private Rectangle bounds;
    private float x, y;
    private static final float SPEED = 200f;
    private boolean isHealthy;


    public Food(float x, float y) {
        isHealthy = Math.random() < 0.5;
        texture = new Texture(isHealthy ? "healthy_food.png" : "unhealthy_food.png");
        this.x = x;
        this.y = y;
        float scale = 0.1f;
        bounds = new Rectangle(x, y, texture.getWidth() * scale, texture.getHeight() * scale);
    }

    public void update(float delta) {
        y -= SPEED * delta;
        bounds.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, texture.getWidth() / 3f, texture.getHeight() / 3f);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isHealthy() {
        return isHealthy;
    }

    public void dispose() {
        texture.dispose();
    }
}
