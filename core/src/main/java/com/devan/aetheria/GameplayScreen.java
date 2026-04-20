package com.devan.aetheria;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameplayScreen implements Screen {
    private final AetheriaGame game;

    public GameplayScreen(AetheriaGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Call when this screen appears
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        game.batch.begin();

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Resizing if game window changed
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
