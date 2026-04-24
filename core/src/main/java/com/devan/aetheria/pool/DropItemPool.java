package com.devan.aetheria.pool;

import com.badlogic.gdx.utils.Pool;
import com.devan.aetheria.entities.DropItem;

public class DropItemPool extends Pool<DropItem> {
    public DropItemPool(int initialCapacity, int max) {
        super(initialCapacity, max);
    }

    @Override
    protected DropItem newObject() {
        return new DropItem();
    }
}
