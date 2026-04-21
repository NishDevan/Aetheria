package com.devan.aetheria.input;

import com.devan.aetheria.entities.Player;

public class JumpCommand implements Command {
    @Override
    public void execute(Player player) {
        player.jump();
    }
}
