package com.game.CalorieAdventureGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.CalorieAdventureGame.MainGame;
import com.game.CalorieAdventureGame.entities.Player;
import com.game.CalorieAdventureGame.entities.Food;
import java.util.ArrayList;
import com.badlogic.gdx.math.MathUtils;

public class GameScreen implements Screen {
    private MainGame game;
    private SpriteBatch batch;
    private Player player;
    private ArrayList<Food> foods;

    public GameScreen(MainGame game) {
        this.game = game;
        batch = new SpriteBatch();
        player = new Player();  // You’ll need to create this class for player logic
        foods = new ArrayList<>();  // Food array to hold food objects
    }

    @Override
    public void show() {
        // Initialize or reset game elements here
        spawnFood();
    }

    private void spawnFood() {
        // Example logic to spawn food at random positions
        for (int i = 0; i < 5; i++) {
            foods.add(new Food(MathUtils.random(0, Gdx.graphics.getWidth()), Gdx.graphics.getHeight()));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clear the screen

        // Update game elements
        player.update(delta);  // Update player logic
        for (Food food : foods) {
            food.update(delta);  // Update falling food
        }

        batch.begin();
        player.render(batch);  // Draw player
        for (Food food : foods) {
            food.render(batch);  // Draw falling food
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        // Dispose of player and food resources
    }
}
