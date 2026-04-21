package com.devan.aetheria.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.devan.aetheria.AetheriaGame;
import com.devan.aetheria.entities.Player;
import com.devan.aetheria.input.Command;
import com.devan.aetheria.input.MoveLeftCommand;
import com.devan.aetheria.input.MoveRightCommand;
import com.devan.aetheria.input.JumpCommand;
import com.devan.aetheria.world.Block;
import com.devan.aetheria.world.BlockFactory;


public class GameplayScreen implements Screen {
    private final AetheriaGame game;
    private Player player;
    private Command keyA;
    private Command keyD;
    private Command keySpace;
    private OrthographicCamera camera;
    private Viewport viewport;

    private final int MAP_WIDTH = 20;
    private final int MAP_HEIGHT = 10;
    private final int TILE_SIZE = 32;
    private Block[][] worldMap;

    public GameplayScreen(AetheriaGame game) {
        this.game = game;
        this.player = new Player();
        this.keyA = new MoveLeftCommand();
        this.keyD = new MoveRightCommand();
        this.keySpace = new JumpCommand();

        // Setup Camera
        camera = new OrthographicCamera();
        viewport = new FitViewport(400, 225, camera);

        initWorld();

        player.position.set(100, TILE_SIZE * 3);
    }

    private void initWorld() {
        worldMap = new Block[MAP_WIDTH][MAP_HEIGHT];

        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                if (y < 3) {
                    worldMap[x][y] = BlockFactory.createBlock("DIRT");
                } else {
                    worldMap[x][y] = null;
                }
            }
        }
    }

    @Override
    public void show() {
        // Call when this screen appears
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        player.update(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            keyA.execute(player);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            keyD.execute(player);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            keySpace.execute(player);
        }

        camera.position.x = player.position.x + 16;
        camera.position.y = (TILE_SIZE * 3) + 50;
        camera.update();

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        // Map Rendering
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                if (worldMap[x][y] != null) {
                    // Gambar tiap blok sesuai posisinya di grid
                    worldMap[x][y].draw(game.batch, x * TILE_SIZE, y * TILE_SIZE);
                }
            }
        }

        player.draw(game.batch);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
