package com.devan.aetheria.states;

import com.devan.aetheria.entities.Player;

public class JumpState implements PlayerState{
    private static final float MOVE_EPSILON = 0.01f;

    @Override
    public void enter(Player player) {
        player.setAnimationState(Player.AnimationState.JUMP);
    }

    @Override
    public void update(Player player, float delta) {
        player.resolveAirState();
        if (player.isGrounded) {
            if (Math.abs(player.velocity.x) <= MOVE_EPSILON) {
                player.changeState(new IdleState());
            } else {
                player.changeState(new RunState());
            }
        }
    }
}
