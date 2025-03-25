package com.game.CalorieAdventureGame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.CalorieAdventureGame.MainGame;

public class GameOverScreen implements Screen {
    private MainGame game;
    private Stage stage;
    private BitmapFont font;
    private int healthyPoints;
    private int unhealthyPoints;

    public GameOverScreen(MainGame game, int healthyPoints, int unhealthyPoints) {
        this.game = game;
        this.healthyPoints = healthyPoints;
        this.unhealthyPoints = unhealthyPoints;
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        
        // Create LabelStyle and TextButtonStyle
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Create labels and buttons
        Label resultLabel = new Label("Game Over!", labelStyle);
        Label scoreLabel = new Label("Healthy Points: " + healthyPoints + "\nUnhealthy Points: " + unhealthyPoints, labelStyle);
        TextButton playAgainButton = new TextButton("Play Again", textButtonStyle);
        TextButton exitButton = new TextButton("Exit", textButtonStyle);

        // Table to hold labels and buttons
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);
        table.add(resultLabel).padBottom(20);
        table.row();
        table.add(scoreLabel).padBottom(20);
        table.row();
        table.add(playAgainButton).fillX().uniformX().padBottom(10);
        table.row();
        table.add(exitButton).fillX().uniformX();

        stage.addActor(table);

        // Button listeners
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));  // Transition to the game screen
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();  // Exit the application
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);  // Set stage as the input processor
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clear the screen
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));  // Update the stage
        stage.draw();  // Draw the stage
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);  // Adjust the stage on window resize
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();  // Dispose stage to free up resources
        font.dispose();   // Dispose font to free up resources
    }
}
