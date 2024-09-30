package com.endmc.itemcreator.item.defaults;

import com.endmc.itemcreator.item.CustomItem;
import com.endmc.itemcreator.item.ItemEventHandler;
import com.endmc.itemcreator.utils.ItemWrapper;
import com.endmc.itemcreator.utils.ActionBarUtils;
import com.google.common.base.Preconditions;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Comparator;
import java.util.Optional;

public final class RobberHoe extends CustomItem {

    private final Economy economy;

    public RobberHoe() {
        super("endmc-robber-hoe", Material.GOLD_HOE, "§eRobber's Hoe", null);

        final RegisteredServiceProvider<Economy> registration = Bukkit.getServer().getServicesManager()
                .getRegistration(Economy.class);

        Preconditions.checkNotNull(registration, "Cannot get Vaut registration");

        economy = registration.getProvider();

        Preconditions.checkNotNull(economy, "Cannot get Vaut Economy");
    }

    private String getRemainingTime(final long timeLag) {
        final long remainingMilis = (1000 * 60 * 10) - timeLag;

        long minutes = (remainingMilis / 1000) / 60;
        long seconds = (remainingMilis / 1000) % 60;

        final StringBuilder timeBuilder = new StringBuilder();

        if (minutes != 0) {
            timeBuilder.append(minutes).append(" mins ");
        }

        if (seconds != 0) {
            timeBuilder.append(seconds).append(" secs ");
        }

        return timeBuilder.toString();
    }

    private Optional<Player> getRichestPlayer(final Player centerPlayer, final double radius) {
        final Location centerLocation = centerPlayer.getLocation();

        return centerLocation.getWorld().getPlayers().stream()
                .filter(current -> current != centerPlayer && current.getLocation().distance(centerLocation) <= radius)
                .max(Comparator.comparing(economy::getBalance));
    }

    @ItemEventHandler
    public void onInteract(final PlayerInteractEvent event, final ItemWrapper wrapper) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR
                && event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        final Optional<Long> lastUsed = wrapper.getOption(BasicOption.LAST_USED);
        final Player player = event.getPlayer();

        if (lastUsed.isPresent()) {
            final long timeLag = System.currentTimeMillis() - lastUsed.get();
            if (timeLag < (1000 * 60 * 10)) {
                ActionBarUtils.send(player, "§cYou must wait " + getRemainingTime(timeLag) + "!");
                return;
            }
        }

        wrapper.setOption(BasicOption.LAST_USED, System.currentTimeMillis());

        final Optional<Player> richest = getRichestPlayer(player, 50);

        if (richest.isEmpty()) {
            ActionBarUtils.send(player, "§cNo nearest player was found !");
            return;
        }

        final Player richestPlayer = richest.get();

        ActionBarUtils.send(player, "§6Richest Player : " + richestPlayer.getName() + ", distance : "
                + Math.round(100.0 * player.getLocation().distance(richestPlayer.getLocation())) / 100.0);
    }
}
