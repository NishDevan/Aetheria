package com.devan.aetheria.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.devan.aetheria.managers.Assets;

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
        position.x += amount * Gdx.graphics.getDeltaTime();
    }

    public void jump() {
        if (isGrounded) {
            velocity.y = JUMP_POWER;
            isGrounded = false;
        }
    }

    public void update(float delta) {
        velocity.y += GRAVITY * delta;

        position.y += velocity.y * delta;

        float groundLevel = 96f;

        if (position.y <= groundLevel) {
            position.y = groundLevel;
            velocity.y = 0;
            isGrounded = true;
        } else {
            isGrounded = false;
        }
    }
    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }
}
