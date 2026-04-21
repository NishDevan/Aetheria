package com.devan.aetheria.input;

import com.devan.aetheria.entities.Player;

public interface Command {
    void execute(Player player);
}
