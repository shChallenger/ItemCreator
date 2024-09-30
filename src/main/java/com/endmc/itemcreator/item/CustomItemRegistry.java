package com.endmc.itemcreator.item;

import java.util.HashMap;
import java.util.Map;

/* This class is used jointly to ItemClassLoader to store created instances
    It separates Service & Loading, as we want to load items at the start
 */
public final class CustomItemRegistry {

    private final Map<String, CustomItem> items;

    public CustomItemRegistry(final int length) {
        this.items = new HashMap<>(length);
    }

    public void addItem(final CustomItem item) {
        this.items.put(item.getItemId(), item);
    }

    public Map<String, CustomItem> getItems() {
        return this.items;
    }
}
