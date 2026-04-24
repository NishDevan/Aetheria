package com.devan.aetheria.inventory;

public class Inventory {
    private final ItemStack[] slots;
    private final int maxStackPerSlot;

    public Inventory(int slotCount, int maxStackPerSlot) {
        this.slots = new ItemStack[Math.max(1, slotCount)];
        this.maxStackPerSlot = Math.max(1, maxStackPerSlot);
    }

    public int add(ItemType type, int amount) {
        int remaining = amount;
        if (amount <= 0) return 0;

        for (int i = 0; i < slots.length && remaining > 0; i++) {
            ItemStack stack = slots[i];
            if (stack != null && stack.getType() == type) {
                remaining = stack.add(remaining, maxStackPerSlot);
            }
        }

        for (int i = 0; i < slots.length && remaining > 0; i++) {
            if (slots[i] == null) {
                int put = Math.min(maxStackPerSlot, remaining);
                slots[i] = new ItemStack(type, put);
                remaining -= put;
            }
        }

        return remaining;
    }

    public int getTotal(ItemType type) {
        int total = 0;
        for (ItemStack stack : slots) {
            if (stack != null && stack.getType() == type) {
                total += stack.getAmount();
            }
        }
        return total;
    }

    public boolean removeOneFromSlot(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= slots.length) return false;

        ItemStack stack = slots[slotIndex];
        if (stack == null || stack.isEmpty()) return false;

        stack.remove(1);
        if (stack.isEmpty()) {
            slots[slotIndex] = null;
        }
        return true;
    }

    public ItemStack getSlot(int index) {
        if (index < 0 || index >= slots.length) return null;
        return slots[index];
    }

    public int getSlotCount() {
        return slots.length;
    }

    public int getMaxStackPerSlot() {
        return maxStackPerSlot;
    }
}
