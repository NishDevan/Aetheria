package com.devan.aetheria.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devan.aetheria.inventory.Inventory;
import com.devan.aetheria.inventory.ItemStack;
import com.devan.aetheria.inventory.ItemType;
import com.devan.aetheria.managers.Assets;

public class HotbarHud {
    private static final int SLOT_COUNT = 10;
    private static final float SLOT_SIZE = 18f;
    private static final float SLOT_GAP = 4f;
    private static final float MARGIN_BOTTOM = 8f;

    private final BitmapFont font;
    private final Texture whitePixel;
    private final Texture dirtIcon;

    public HotbarHud() {
        this.font = new BitmapFont();
        this.dirtIcon = Assets.getInstance().manager.get("dirt.png", Texture.class);

        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        this.whitePixel = new Texture(pm);
        pm.dispose();
    }

    public void render(SpriteBatch batch, Inventory inventory, int selectedIndex, float worldWidth) {
        float totalWidth = SLOT_COUNT * SLOT_SIZE + (SLOT_COUNT - 1) * SLOT_GAP;
        float startX = (worldWidth - totalWidth) * 0.5f;
        float y = MARGIN_BOTTOM;

        for (int i = 0; i < SLOT_COUNT; i++) {
            float x = startX + i * (SLOT_SIZE + SLOT_GAP);

            batch.setColor(0f, 0f, 0f, 0.55f);
            batch.draw(whitePixel, x, y, SLOT_SIZE, SLOT_SIZE);

            if (i == selectedIndex) {
                drawBorder(batch, x, y, SLOT_SIZE, SLOT_SIZE, 2f, new Color(1f, 0.85f, 0.25f, 1f));
            } else {
                drawBorder(batch, x, y, SLOT_SIZE, SLOT_SIZE, 1f, new Color(0.8f, 0.8f, 0.8f, 1f));
            }

            ItemStack stack = inventory.getSlot(i);
            if (stack != null && !stack.isEmpty()) {
                Texture icon = getIcon(stack.getType());
                batch.setColor(Color.WHITE);
                batch.draw(icon, x + 2f, y + 2f, SLOT_SIZE - 4f, SLOT_SIZE - 4f);

                font.setColor(Color.WHITE);
                font.draw(batch, String.valueOf(stack.getAmount()), x + 2f, y + 10f);
            }

            font.setColor(0.9f, 0.9f, 0.9f, 1f);
            String slotLabel = (i == 9) ? "0" : String.valueOf(i + 1);
            font.draw(batch, slotLabel, x + SLOT_SIZE -6f, y + SLOT_SIZE - 2f);
        }

        batch.setColor(Color.WHITE);
    }

    private Texture getIcon(ItemType type) {
        if (type == ItemType.DIRT) return dirtIcon;
        return dirtIcon;
    }

    private void drawBorder(SpriteBatch batch, float x, float y, float w, float h, float t, Color c) {
        batch.setColor(c);
        batch.draw(whitePixel, x, y, w, t);
        batch.draw(whitePixel, x, y + h - t, w, t);
        batch.draw(whitePixel, x, y, t, h);
        batch.draw(whitePixel, x + w - t, y, t, h);
    }

    public void dispose() {
        font.dispose();
        whitePixel.dispose();
    }
}
