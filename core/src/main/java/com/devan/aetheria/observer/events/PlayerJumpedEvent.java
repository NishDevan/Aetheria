package com.devan.aetheria.observer.events;

import com.badlogic.gdx.math.Vector2;
import com.devan.aetheria.observer.GameEvent;

public class PlayerJumpedEvent implements GameEvent {
    public final Vector2 position;

    public PlayerJumpedEvent(Vector2 position) {
        this.position = new Vector2(position);
    }
}
