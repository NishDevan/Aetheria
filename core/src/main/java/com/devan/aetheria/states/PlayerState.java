package com.devan.aetheria.states;

import com.devan.aetheria.entities.Player;

public interface PlayerState {
    void enter(Player player);
    void update(Player player, float delta);
}
