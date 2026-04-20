package com.devan.aetheria;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
    private static Assets instance;

    public final AssetManager manager;

    private Assets() {
        manager = new AssetManager();
    }

    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    public void load() {

    }

    public void dispose() {
        manager.dispose();
    }
}
