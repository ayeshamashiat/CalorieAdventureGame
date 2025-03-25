package com.game.CalorieAdventureGame;

import com.badlogic.gdx.Game;
import com.game.CalorieAdventureGame.screens.MenuScreen;

public class MainGame extends Game {
    @Override
    public void create() {
        this.setScreen(new MenuScreen(this));  // Set the first screen to MenuScreen
    }
}
