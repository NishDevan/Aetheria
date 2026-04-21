package com.devan.aetheria.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devan.aetheria.managers.Assets;
import com.devan.aetheria.world.Block;

public class Player {
    public Vector2 position;
    public Vector2 velocity;
    private Texture texture;
    private final float GRAVITY = -800f;
    private final float JUMP_POWER = 350f;
    private boolean isGrounded = false;

    public Player() {
        position = new Vector2(100, 96); // Initial Position
        velocity = new Vector2(0, 0);
        texture = Assets.getInstance().manager.get("player.png", Texture.class);
    }

    public void moveX(float amount) {
        velocity.x = amount;
    }

    public void jump() {
        if (isGrounded) {
            velocity.y = JUMP_POWER;
            isGrounded = false;
        }
    }

    public void update(float delta, Block[][] map, int tileSize) {
        float playerWidth = texture.getWidth();

        // Physics at X axis
        float nextX = position.x + (velocity.x * delta);

        float checkPointX = (velocity.x > 0) ? nextX + playerWidth : nextX;

        int gridX = (int) (checkPointX / tileSize);
        int gridY = (int) ((position.y + 16) / tileSize);

        if (gridX >= 0 && gridX < map.length && gridY >= 0 && gridY < map[0].length) {
            if (map[gridX][gridY] != null) {
                if (velocity.x > 0) {
                    nextX = (gridX * tileSize) - playerWidth;
                } else if (velocity.x < 0) {
                    nextX = (gridX + 1) * tileSize;
                }
                velocity.x = 0;
            }
        }

        position.x = nextX;
        velocity.x = 0;

        // Physics at Y axis
        velocity.y += GRAVITY * delta;

        float nextY = position.y + (velocity.y * delta);

        int playerGridX = (int) ((position.x + 16) / tileSize);

        int feetGridY = (int) (nextY / tileSize);

        if (playerGridX >= 0 && playerGridX < map.length && feetGridY >= 0 && feetGridY < map[0].length) {
            if (velocity.y < 0 && map[playerGridX][feetGridY] != null) {
                nextY = (feetGridY + 1) * tileSize;
                velocity.y = 0;
                isGrounded = true;
            } else {
                isGrounded = false;
            }
        } else {
            isGrounded = false;
        }

        position.y = nextY;
    }
    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
}
