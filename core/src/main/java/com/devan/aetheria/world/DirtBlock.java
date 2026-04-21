package com.devan.aetheria.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devan.aetheria.managers.Assets;

public class DirtBlock implements Block {
    private Texture texture;

    public DirtBlock() {
        texture = Assets.getInstance().manager.get("dirt.png", Texture.class);
    }

    @Override
    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(texture, x, y);
    }
}
