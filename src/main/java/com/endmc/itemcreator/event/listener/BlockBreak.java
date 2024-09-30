package com.endmc.itemcreator.event.listener;

import com.endmc.itemcreator.item.CustomItemService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public final class BlockBreak implements Listener {

    private final CustomItemService itemService;

    public BlockBreak(final CustomItemService itemService) {
        this.itemService = itemService;
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item != null) {
            itemService.callEvent(event, item);
        }
    }
}
