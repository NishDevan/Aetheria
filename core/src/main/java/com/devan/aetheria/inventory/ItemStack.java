package com.devan.aetheria.inventory;

public class ItemStack {
    private final ItemType type;
    private int amount;

    public ItemStack(ItemType type, int amount) {
        this.type = type;
        this.amount = Math.max(0, amount);
    }

    public ItemType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public int add(int addAmount, int maxStack) {
        if (addAmount <= 0) return 0;
        int space = Math.max(0, maxStack - amount);
        int added = Math.min(space, addAmount);
        amount += added;
        return addAmount - added;
    }

    public int remove(int removeAmount) {
        if (removeAmount <= 0) return 0;
        int removed = Math.min(amount, removeAmount);
        amount -= removed;
        return removed;
    }

    public boolean isEmpty() {
        return amount <= 0;
    }
}
