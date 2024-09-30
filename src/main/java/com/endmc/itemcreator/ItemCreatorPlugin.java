package com.endmc.itemcreator;

import com.endmc.itemcreator.command.CommandService;
import com.endmc.itemcreator.event.EventService;
import com.endmc.itemcreator.item.CustomItemRegistry;
import com.endmc.itemcreator.item.CustomItemService;
import com.endmc.itemcreator.item.loader.CustomItemLoader;
import org.bukkit.plugin.java.JavaPlugin;

public final class ItemCreatorPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        // Load created items
        final CustomItemLoader classLoader = new CustomItemLoader();
        classLoader.searchItems("com.endmc.itemcreator.item.defaults");

        // You can load another package if you want more items
        // classLoader.searchItems("com.endmc.anotherpackage");

        // Create ItemService with loaded items
        final CustomItemRegistry itemRegistry = classLoader.loadItems();
        final CustomItemService itemService = new CustomItemService(itemRegistry);

        // Each event registered in CustomItem must be call from real bukkit event
        new EventService(this, itemService);

        // For giving command
        new CommandService(this, itemService);
    }
}
