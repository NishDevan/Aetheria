package com.devan.aetheria.input;

import com.devan.aetheria.entities.Player;

public class MoveLeftCommand implements Command {
    @Override
    public void execute(Player player) {
        player.moveX(-200);
    }
}
