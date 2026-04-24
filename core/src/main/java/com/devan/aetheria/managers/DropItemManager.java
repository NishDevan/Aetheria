package com.devan.aetheria.managers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.devan.aetheria.entities.DropItem;
import com.devan.aetheria.entities.Player;
import com.devan.aetheria.pool.DropItemPool;
import com.devan.aetheria.inventory.Inventory;
import com.devan.aetheria.inventory.ItemType;

public class DropItemManager {
    private final DropItemPool pool = new DropItemPool(16, 256);
    private final Array<DropItem> activeDrops = new Array<>();
    private final Texture dirtTexture;

    public DropItemManager() {
        this.dirtTexture = Assets.getInstance().manager.get("dirt.png", Texture.class);
    }

    private Texture getTextureFor(ItemType itemType) {
        if (itemType == ItemType.DIRT) {
            return dirtTexture;
        }
        return dirtTexture;
    }

    public void spawnFromBlock(int gridX, int gridY, int tileSize, ItemType itemType) {
        DropItem drop = pool.obtain();
        Texture texture = getTextureFor(itemType);

        float x = (gridX * tileSize) + (tileSize - dirtTexture.getWidth()) * 0.5f;
        float y = (gridY * tileSize) + (tileSize - dirtTexture.getHeight()) * 0.5f;

        drop.spawn(x, y, texture, itemType, 1);
        activeDrops.add(drop);
    }

    public int collectNearby(Player player, Inventory inventory) {
        int collectedTotal = 0;

        for (int i = activeDrops.size - 1; i >= 0; i--) {
            DropItem drop = activeDrops.get(i);
            if (!drop.isActive()) continue;

            if (drop.overlaps(player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
                int leftover = inventory.add(drop.getItemType(), drop.getAmount());
                int collected = drop.getAmount() - leftover;
                collectedTotal += collected;

                if (leftover <= 0) {
                    activeDrops.removeIndex(i);
                    pool.free(drop);
                } else {
                    drop.setAmount(leftover);
                }
            }
        }
        return collectedTotal;
    }

    public void draw(SpriteBatch batch) {
        for (DropItem drop : activeDrops) {
            drop.draw(batch);
        }
    }

    public void clear() {
        for (DropItem drop : activeDrops) {
            pool.free(drop);
        }
        activeDrops.clear();
    }
}
