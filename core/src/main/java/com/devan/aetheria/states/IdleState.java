package com.devan.aetheria.states;

import com.devan.aetheria.entities.Player;

public class IdleState implements PlayerState {
    private static final float MOVE_EPSILON = 0.01f;

    @Override
    public void enter(Player player) {
        player.setAnimationState(Player.AnimationState.IDLE);
    }

    @Override
    public void update(Player player, float delta) {
        player.resolveAirState();

        if (!player.isGrounded) {
            return;
        } else if (Math.abs(player.velocity.x) > MOVE_EPSILON) {
            player.changeState(new RunState());
        }
    }
}
