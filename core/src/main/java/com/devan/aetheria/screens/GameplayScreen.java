package com.devan.aetheria.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.math.Vector3;
import com.devan.aetheria.AetheriaGame;
import com.devan.aetheria.entities.Player;
import com.devan.aetheria.input.Command;
import com.devan.aetheria.input.MoveLeftCommand;
import com.devan.aetheria.input.MoveRightCommand;
import com.devan.aetheria.input.JumpCommand;
import com.devan.aetheria.world.Block;
import com.devan.aetheria.world.BlockFactory;
import com.devan.aetheria.observer.EventBus;
import com.devan.aetheria.observer.GameEventListener;
import com.devan.aetheria.observer.events.BlockMinedEvent;
import com.devan.aetheria.observer.events.PlayerJumpedEvent;
import com.devan.aetheria.managers.DropItemManager;
import com.devan.aetheria.inventory.Inventory;
import com.devan.aetheria.inventory.ItemType;
import com.devan.aetheria.ui.HotbarHud;
import com.devan.aetheria.inventory.ItemStack;


public class GameplayScreen implements Screen {
    private final AetheriaGame game;
    private Player player;
    private Command keyA;
    private Command keyD;
    private Command keySpace;
    private OrthographicCamera camera;
    private Viewport viewport;
    private GameEventListener<BlockMinedEvent> blockMinedListener;
    private GameEventListener<PlayerJumpedEvent> playerJumpedListener;
    private DropItemManager dropItemManager;
    private OrthographicCamera hudCamera;
    private Viewport hudViewport;
    private HotbarHud hotbarHud;
    private int selectedHotbarIndex = 0;

    private Inventory inventory;
    private boolean observersRegistered = false;
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

        dropItemManager = new DropItemManager();
        inventory = new Inventory(10, 99);
        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(400, 225, hudCamera);
        hotbarHud = new HotbarHud();

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

        worldMap[10][3] = BlockFactory.createBlock("DIRT");
        worldMap[10][4] = BlockFactory.createBlock("DIRT");
        worldMap[10][5] = BlockFactory.createBlock("DIRT");

    }

    private void registerObservers() {
        if (observersRegistered) return;

        if (blockMinedListener == null) {
            blockMinedListener = event -> {
                dropItemManager.spawnFromBlock(event.gridX, event.gridY, TILE_SIZE, ItemType.DIRT);
                System.out.println("Block mined at: " + event.gridX + ", " + event.gridY);
            };
        }

        if (playerJumpedListener == null) {
            playerJumpedListener = event ->
                System.out.println("Player jumped from: " + event.position);
        }

        EventBus.getInstance().subscribe(BlockMinedEvent.class, blockMinedListener);
        EventBus.getInstance().subscribe(PlayerJumpedEvent.class, playerJumpedListener);
        observersRegistered = true;
    }

    private void unregisterObservers() {
        if (!observersRegistered) return;

        EventBus.getInstance().unsubscribe(BlockMinedEvent.class, blockMinedListener);
        EventBus.getInstance().unsubscribe(PlayerJumpedEvent.class, playerJumpedListener);
        observersRegistered = false;
    }

    @Override
    public void show() {
        registerObservers();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);

        // Keyboard Input Handling
        boolean leftPressed = Gdx.input.isKeyPressed(Input.Keys.A);
        boolean rightPressed = Gdx.input.isKeyPressed(Input.Keys.D);

        player.moveX(0);
        if (leftPressed && !rightPressed) {
            keyA.execute(player);
        } else if (rightPressed && !leftPressed) {
            keyD.execute(player);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) keySpace.execute(player);

        // World Physics
        player.update(delta, worldMap, TILE_SIZE);

        int collected = dropItemManager.collectNearby(player, inventory);
        if (collected > 0) {
            System.out.println("DIRT total: " + inventory.getTotal(ItemType.DIRT));
        }

        // Mining System (Using Left Click)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

            camera.unproject(mousePos);

            int gridX = (int) (mousePos.x / TILE_SIZE);
            int gridY = (int) (mousePos.y / TILE_SIZE);

            if (gridX >= 0 && gridX < MAP_WIDTH && gridY >= 0 && gridY < MAP_HEIGHT) {
                if (worldMap[gridX][gridY] != null) {
                    worldMap[gridX][gridY] = null;
                    EventBus.getInstance().publish(new BlockMinedEvent(gridX, gridY));
                }
            }
        }

        // Place block system (Right Click)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePos);

            int gridX = (int) (mousePos.x / TILE_SIZE);
            int gridY = (int) (mousePos.y / TILE_SIZE);

            if (gridX >= 0 && gridX < MAP_WIDTH && gridY >= 0 && gridY < MAP_HEIGHT) {
                if (worldMap[gridX][gridY] == null) {
                    ItemStack selectedStack = inventory.getSlot(selectedHotbarIndex);

                    if (selectedStack != null && selectedStack.getType() == ItemType.DIRT && selectedStack.getAmount() > 0) {
                        worldMap[gridX][gridY] = BlockFactory.createBlock("DIRT");
                        inventory.removeOneFromSlot(selectedHotbarIndex);
                    }
                }
            }
        }

        // Camera Update
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

        handleHotbarInput();

        dropItemManager.draw(game.batch);
        player.draw(game.batch);
        game.batch.end();

        game.batch.setProjectionMatrix(hudCamera.combined);
        game.batch.begin();
        hotbarHud.render(game.batch, inventory, selectedHotbarIndex, hudViewport.getWorldWidth());
        game.batch.end();
    }

    private void handleHotbarInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) selectedHotbarIndex = 0;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) selectedHotbarIndex = 1;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) selectedHotbarIndex = 2;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) selectedHotbarIndex = 3;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) selectedHotbarIndex = 4;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) selectedHotbarIndex = 5;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_7)) selectedHotbarIndex = 6;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_8)) selectedHotbarIndex = 7;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_9)) selectedHotbarIndex = 8;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) selectedHotbarIndex = 9;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        unregisterObservers();
    }

    @Override
    public void dispose() {
        hotbarHud.dispose();
        dropItemManager.clear();
        unregisterObservers();
    }
}
