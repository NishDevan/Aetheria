package com.devan.aetheria;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devan.aetheria.managers.Assets;
import com.devan.aetheria.screens.GameplayScreen;

public class AetheriaGame extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        Assets.getInstance().load();

        Assets.getInstance().manager.finishLoading();

        this.setScreen(new GameplayScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();

        if (screen != null) {
            screen.dispose();
        }

        Assets.getInstance().dispose();
    }
}
