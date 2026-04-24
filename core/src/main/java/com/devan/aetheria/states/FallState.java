package com.devan.aetheria.states;

import com.devan.aetheria.entities.Player;

public class FallState implements PlayerState{
    private static final float MOVE_EPSILON = 0.01f;

    @Override
    public void enter(Player player) {
        player.setAnimationState(Player.AnimationState.FALL);
    }

    @Override
    public void update(Player player, float delta) {
        if (player.isGrounded) {
            if (Math.abs(player.velocity.x) <= MOVE_EPSILON) {
                player.changeState(new IdleState());
            } else {
                player.changeState(new RunState());
            }
        } else if (player.velocity.y > 0) {
            player.changeState(new JumpState());
        }
    }
}
