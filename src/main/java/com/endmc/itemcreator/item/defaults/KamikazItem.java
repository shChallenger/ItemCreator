package com.endmc.itemcreator.item.defaults;

import com.endmc.itemcreator.item.CustomItem;
import com.endmc.itemcreator.item.ItemEventHandler;
import com.endmc.itemcreator.utils.ItemWrapper;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;

public final class KamikazItem extends CustomItem {

    public KamikazItem() {
        super("endmc-kamikaz", Material.IRON_AXE, "§dKamikaz's Axe !", null);
    }

    @ItemEventHandler
    public void onBlockBreak(final BlockBreakEvent event, final ItemWrapper wrapper) {
        final Location loc = event.getBlock().getLocation();
        loc.getWorld().createExplosion(loc, 2);
    }
}
