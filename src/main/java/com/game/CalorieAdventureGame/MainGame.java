package com.game.CalorieAdventureGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.game.CalorieAdventureGame.screens.GameScreen;

public class MainGame extends Game {
    @Override
    public void create() {
        this.setScreen(new GameScreen(this));  // Set the first screen to GameScreen
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);  // Clear the screen every frame
        super.render();  // Render the active screen
    }
}
