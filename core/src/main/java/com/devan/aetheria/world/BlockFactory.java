package com.devan.aetheria.world;

public class BlockFactory {
    public static Block createBlock(String blockType) {
        if (blockType == null) {
            return null;
        }

        switch (blockType.toUpperCase()) {
            case "DIRT":
                return new DirtBlock();
            default:
                throw new IllegalArgumentException("Unknown Block Type: " + blockType);
        }
    }
}
