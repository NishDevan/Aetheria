package com.devan.aetheria.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class EventBus {
    private static EventBus instance;
    private final Map<Class<? extends GameEvent>, List<GameEventListener<? extends GameEvent>>> listeners;

    private EventBus() {
        listeners = new HashMap<>();
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public synchronized <T extends GameEvent> void subscribe(Class<T> eventType, GameEventListener<T> listener) {
        listeners.computeIfAbsent(eventType, key -> new ArrayList<>()).add(listener);
    }

    public synchronized <T extends GameEvent> void unsubscribe(Class<T> eventType, GameEventListener<T> listener) {
        List<GameEventListener<? extends GameEvent>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
            if (eventListeners.isEmpty()) {
                listeners.remove(eventType);
            }
        }
    }

    public synchronized <T extends GameEvent> void publish(T event) {
        List<GameEventListener<? extends GameEvent>> eventListeners = listeners.get(event.getClass());
        if (eventListeners == null) return;

        List<GameEventListener<? extends GameEvent>> snapshot = new ArrayList<>(eventListeners);
        for (GameEventListener<? extends GameEvent> listener : snapshot) {
            ((GameEventListener<T>) listener).onEvent(event);
        }
    }
}
