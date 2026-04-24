package com.devan.aetheria.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.devan.aetheria.managers.Assets;
import com.devan.aetheria.states.IdleState;
import com.devan.aetheria.states.PlayerState;
import com.devan.aetheria.world.Block;
import com.devan.aetheria.states.FallState;
import com.devan.aetheria.states.JumpState;
import com.devan.aetheria.observer.EventBus;
import com.devan.aetheria.observer.events.PlayerJumpedEvent;

public class Player {
    public enum AnimationState {
        IDLE,
        RUN,
        JUMP,
        FALL
    }

    public Vector2 position;
    public Vector2 velocity;
    private final float playerWidth;
    private final float playerHeight;
    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> runAnimation;
    private final Animation<TextureRegion> jumpAnimation;
    private AnimationState animationState = AnimationState.IDLE;
    private float stateTime;
    private PlayerState currentState;
    private final float GRAVITY = -800f;
    private final float JUMP_POWER = 350f;
    public boolean isGrounded = false;
    private boolean facingRight = true;

    public Player() {
        position = new Vector2(100, 96); // Initial Position
        velocity = new Vector2(0, 0);

        Texture idleTexture = Assets.getInstance().manager.get("player.png", Texture.class);
        Texture runTexture = Assets.getInstance().manager.get("player_run.png", Texture.class);
        Texture jumpTexture = Assets.getInstance().manager.get("player_jump.png", Texture.class);

        playerWidth = idleTexture.getWidth();
        playerHeight = idleTexture.getHeight();

        TextureRegion idleFrame = new TextureRegion(idleTexture);
        idleAnimation = new Animation<>(0.5f, idleFrame);

        TextureRegion[] runFrames = TextureRegion.split(runTexture, (int) playerWidth, (int) playerHeight)[0];
        runAnimation = new Animation<>(0.08f, runFrames);

        TextureRegion jumpFrame = new TextureRegion(jumpTexture);
        jumpAnimation = new Animation<>(0.2f, jumpFrame);

        stateTime = 0f;
        changeState(new IdleState());
    }

    public void changeState(PlayerState newState) {
        if (currentState != null && currentState.getClass() == newState.getClass()) {
            return;
        }

        this.currentState = newState;
        this.stateTime = 0f;
        this.currentState.enter(this);
    }

    public void setAnimationState(AnimationState animationState) {
        if (this.animationState != animationState) {
            this.animationState = animationState;
            this.stateTime = 0f;
        }
    }

    public void moveX(float amount) {
        velocity.x = amount;
        if (amount > 0f) facingRight = true;
        else if (amount < 0f) facingRight = false;
    }

    public void jump() {
        if (isGrounded) {
            velocity.y = JUMP_POWER;
            isGrounded = false;
            EventBus.getInstance().publish(new PlayerJumpedEvent(position));
        }
    }

    public void update(float delta, Block[][] map, int tileSize) {
        stateTime += delta;

        if (currentState != null) {
            currentState.update(this, delta);
        }

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
        float checkPointY = (velocity.y > 0) ? nextY + playerHeight : nextY;
        boolean hitGround = false;

        int checkGridX = (int) ((position.x + (playerWidth / 2f)) / tileSize);
        int checkGridY = (int) (checkPointY / tileSize);

        if (checkGridX >= 0 && checkGridX < map.length && checkGridY >= 0 && checkGridY < map[0].length) {
            if (map[checkGridX][checkGridY] != null) {
                if (velocity.y > 0) {
                    // Nabrak Plafon
                    nextY = (checkGridY * tileSize) - playerHeight;
                    velocity.y = 0;
                } else if (velocity.y < 0) {
                    // Nabrak Lantai
                    nextY = (checkGridY + 1) * tileSize;
                    velocity.y = 0;
                    hitGround = true;
                }
            }
        }

        isGrounded = hitGround;
        position.y = nextY;
    }

    private Animation<TextureRegion> getCurrentAnimation() {
        switch (animationState) {
            case RUN:
                return runAnimation;
            case JUMP:
                return jumpAnimation;
            case FALL:
                return jumpAnimation;
            case IDLE:
            default:
                return idleAnimation;
        }
    }

    public void resolveAirState() {
        if (!isGrounded) {
            if (velocity.y > 0) {
                changeState(new JumpState());
            } else if (velocity.y < 0) {
                changeState(new FallState());
            }
        }
    }

    public void draw(SpriteBatch batch) {
        Animation<TextureRegion> currentAnimation = getCurrentAnimation();
        boolean looping = animationState != AnimationState.JUMP;
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, looping);

        float drawX = facingRight ? position.x : position.x + playerWidth;
        float drawWidth = facingRight ? playerWidth : -playerWidth;

        batch.draw(currentFrame, drawX, position.y, drawWidth, playerHeight);
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getWidth() {
        return playerWidth;
    }

    public float getHeight() {
        return playerHeight;
    }
}
