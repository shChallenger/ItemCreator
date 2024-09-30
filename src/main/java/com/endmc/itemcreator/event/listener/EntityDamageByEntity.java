package com.endmc.itemcreator.event.listener;

import com.endmc.itemcreator.item.CustomItemService;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public final class EntityDamageByEntity implements Listener {

    private final CustomItemService itemService;

    public EntityDamageByEntity(final CustomItemService itemService) {
        this.itemService = itemService;
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event) {
        if (event.getDamager().getType() != EntityType.PLAYER)
            return;

        final ItemStack item = ((Player) event.getDamager()).getInventory().getItemInMainHand();

        if (item != null) {
            itemService.callEvent(event, item);
        }
    }
}
