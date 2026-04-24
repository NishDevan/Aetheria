package com.devan.aetheria.observer;

public interface GameEventListener<T extends GameEvent> {
    void onEvent(T event);
}
