package com.game.CalorieAdventureGame.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.game.CalorieAdventureGame.MainGame;

public class MenuScreen implements Screen {
    private MainGame game;
    private TextButton playButton;
    private TextButton exitButton;
    private Stage stage;
    private BitmapFont font;

    public MenuScreen(MainGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont();
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;

        // Create buttons
        playButton = new TextButton("Play", textButtonStyle);
        exitButton = new TextButton("Exit", textButtonStyle);

        // Table to hold buttons
        Table table = new Table();
        table.setFillParent(true);
        table.top().padTop(50);  // Center buttons with some top padding
        table.add(playButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0); // Space between buttons
        table.add(exitButton).fillX().uniformX();
        stage.addActor(table);

        // Button listeners
        playButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                game.setScreen(new GameScreen(game));  // Transition to the game screen
                return true;
            }
            return false;
        });

        exitButton.addListener(event -> {
            if (event.toString().equals("touchDown")) {
                Gdx.app.exit();  // Exit the application
                return true;
            }
            return false;
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
