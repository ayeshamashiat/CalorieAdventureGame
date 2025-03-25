package com.game.CalorieAdventureGame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Player {
    private Texture texture;
    private float x, y;
    private static final float SPEED = 300f;
    private static final float LANE_WIDTH = Gdx.graphics.getWidth() / 3f;

    public Player() {
        texture = new Texture("cat.png");
        x = Gdx.graphics.getWidth() / 2f - texture.getWidth() / 2f;
        y = 10; // Starting position near the bottom
    }

    public void update(float delta) {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.LEFT)) {
            x = Math.max(0, x - LANE_WIDTH);
        }
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.RIGHT)) {
            x = Math.min(Gdx.graphics.getWidth() - LANE_WIDTH, x + LANE_WIDTH);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, texture.getWidth() / 2f, texture.getHeight() / 2f);
    }

    public void dispose() {
        texture.dispose();
    }
}
