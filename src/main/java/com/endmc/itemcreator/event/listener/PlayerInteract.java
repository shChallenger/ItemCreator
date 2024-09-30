package com.endmc.itemcreator.event.listener;

import com.endmc.itemcreator.item.CustomItemService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class PlayerInteract implements Listener {

    private final CustomItemService itemService;

    public PlayerInteract(final CustomItemService itemService) {
        this.itemService = itemService;
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final ItemStack item = event.getItem();

        if (item != null) {
            itemService.callEvent(event, item);
        }
    }
}
