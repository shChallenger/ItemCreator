package com.endmc.itemcreator.event;

import com.endmc.itemcreator.ItemCreatorPlugin;
import com.endmc.itemcreator.event.listener.BlockBreak;
import com.endmc.itemcreator.event.listener.EntityDamageByEntity;
import com.endmc.itemcreator.event.listener.PlayerInteract;
import com.endmc.itemcreator.item.CustomItemService;
import org.bukkit.event.Listener;

public final class EventService {

    private final ItemCreatorPlugin plugin;
    private final CustomItemService itemService;

    public EventService(final ItemCreatorPlugin plugin, final CustomItemService itemService) {
        this.plugin = plugin;
        this.itemService = itemService;
        registerListeners();
    }

    private void registerListeners() {
        registerListener(new BlockBreak(itemService));
        registerListener(new EntityDamageByEntity(itemService));
        registerListener(new PlayerInteract(itemService));
    }

    private void registerListener(final Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
