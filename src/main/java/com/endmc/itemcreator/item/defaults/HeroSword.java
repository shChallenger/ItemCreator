package com.endmc.itemcreator.item.defaults;

import com.endmc.itemcreator.item.CustomItem;
import com.endmc.itemcreator.item.ItemEventHandler;
import com.endmc.itemcreator.utils.ItemWrapper;
import com.endmc.itemcreator.utils.ActionBarUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class HeroSword extends CustomItem {

    public HeroSword() {
        super("endmc-hero-sword", Material.DIAMOND_SWORD, "§bHero's Sword !", null);
    }

    @ItemEventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent event, final ItemWrapper wrapper) {
        final Optional<UUID> heroUuid = wrapper.getOption(BasicOption.OWNER);
        final Player player = (Player) event.getDamager();

        if (heroUuid.isEmpty()) {
            wrapper.setOption(BasicOption.OWNER, player.getUniqueId());
            wrapper.setLore(List.of("§aOwner: " + player.getName()));
        } else if (!heroUuid.get().equals(player.getUniqueId())) {
            event.setCancelled(true);
            ActionBarUtils.send(player, "§cYou can't use this sword !");
        }
    }
}
