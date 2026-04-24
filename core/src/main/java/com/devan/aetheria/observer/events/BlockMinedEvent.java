package com.devan.aetheria.observer.events;

import com.devan.aetheria.observer.GameEvent;

public class BlockMinedEvent implements GameEvent {
    public final int gridX;
    public final int gridY;

    public BlockMinedEvent(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
    }
}
