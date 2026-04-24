package com.devan.aetheria.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.devan.aetheria.inventory.ItemType;

public class DropItem implements Pool.Poolable {
    private final Vector2 position = new Vector2();
    private boolean active;
    private Texture texture;
    private int amount;
    private ItemType itemType;

    public void spawn(float x, float y, Texture texture, ItemType itemType, int amount) {
        this.position.set(x, y);
        this.texture = texture;
        this.itemType = itemType;
        this.amount = amount;
        this.active = true;
    }

    public void draw(SpriteBatch batch) {
        if (!active || texture == null) return;
        batch.draw(texture, position.x, position.y);
    }

    public boolean overlaps(float x, float y, float w, float h) {
        if (!active || texture == null) return false;
        float tw = texture.getWidth();
        float th = texture.getHeight();
        return x < position.x + tw && x + w > position.x && y < position.y + th && y + h > position.y;
    }

    public boolean isActive() {
        return active;
    }

    public int getAmount() {
        return amount;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setAmount(int amount) {
        this.amount = Math.max(0, amount);
    }

    @Override
    public void reset() {
        position.set(0f, 0f);
        active = false;
        texture = null;
        amount = 0;
        itemType = null;
    }
}
