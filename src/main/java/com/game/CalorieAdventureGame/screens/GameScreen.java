package com.game.CalorieAdventureGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.CalorieAdventureGame.MainGame;
import com.game.CalorieAdventureGame.entities.Player;
import com.game.CalorieAdventureGame.entities.Food;
import java.util.ArrayList;
import com.badlogic.gdx.math.MathUtils;
import java.util.Iterator;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
    private MainGame game;
    private SpriteBatch batch;
    private Player player;
    private ArrayList<Food> foods;
    private long lastSpawnTime;
    private long startTime;
    private int healthyPoints;
    private int unhealthyPoints;

    public GameScreen(MainGame game) {
        this.game = game;
        batch = new SpriteBatch();
        player = new Player();  // You’ll need to create this class for player logic
        foods = new ArrayList<>();  // Food array to hold food objects
        lastSpawnTime = TimeUtils.nanoTime(); 
        startTime = TimeUtils.nanoTime();
        healthyPoints = 0;
        unhealthyPoints = 0;
    }

    @Override
    public void show() {
        // Initialize or reset game elements here
        spawnFood();
    }

    private void spawnFood() {
        int lane = MathUtils.random(0, 2); // Randomly choose a lane (0 = left, 1 = center, 2 = right)
        float laneWidth = Gdx.graphics.getWidth() / 3f;
        float x = lane * laneWidth + (laneWidth - new Texture("food.png").getWidth() / 3f) / 2;
        Food food = new Food(x, Gdx.graphics.getHeight());
        foods.add(food);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clear the screen

        // Update game elements
        player.update(delta);  // Update player logic
        Iterator<Food> iter = foods.iterator();
        while (iter.hasNext()) {
            Food food = iter.next();
            food.update(delta);  // Update falling food
            if (player.checkCollision(food)) {
                if (food.isHealthy()) {
                    healthyPoints++;
                } else {
                    unhealthyPoints++;
                }
                iter.remove();
            }
        }

        // Spawn new food at intervals
        if (TimeUtils.nanoTime() - lastSpawnTime > 1000000000) { // 1 second interval
            spawnFood();
            lastSpawnTime = TimeUtils.nanoTime();
        }


        batch.begin();
        player.render(batch);  // Draw player
        for (Food food : foods) {
            food.render(batch);  // Draw falling food
        }
        batch.end();

        // Check if 2 minutes have passed
        if (TimeUtils.nanoTime() - startTime > 120000000000L) { // 2 minutes in nanoseconds
            if (healthyPoints > unhealthyPoints) {
                System.out.println("You Win! Healthy Points: " + healthyPoints);
            } else {
                System.out.println("You Lose! Unhealthy Points: " + unhealthyPoints);
            }
            Gdx.app.exit();  // Exit the game
        }
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
