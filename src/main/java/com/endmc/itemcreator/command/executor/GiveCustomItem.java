package com.endmc.itemcreator.command.executor;

import com.endmc.itemcreator.item.CustomItem;
import com.endmc.itemcreator.item.CustomItemService;
import com.endmc.itemcreator.utils.ItemWrapper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class GiveCustomItem implements CommandExecutor, TabCompleter {

    private final CustomItemService itemService;

    public GiveCustomItem(final CustomItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§cUsage: /" + label + " <Player> <ItemId>");
            return false;
        }

        // Get target player
        final Player target = Bukkit.getPlayerExact(args[0]);

        if (target == null) {
            sender.sendMessage("§cPlayer is not connected !");
            return false;
        }

        // Get target item from id
        final Optional<CustomItem> item = itemService.getItem(args[1]);

        if (item.isEmpty()) {
            sender.sendMessage("§cWrong item id !");
            return false;
        }

        final CustomItem targetItem = item.get();

        // Check if target already got the item
        final boolean hasItem = target.getInventory()
                .all(targetItem.getMaterial()).values().stream()
                .map(itemStack -> new ItemWrapper((CraftItemStack) itemStack))
                .anyMatch(wrapper ->
                        wrapper.getOption(CustomItem.BasicOption.ID)
                                .filter(itemId -> itemId == targetItem.getItemId())
                                .isPresent()
                );

        if (hasItem) {
            sender.sendMessage("§cPlayer has already got this item !");
            return false;
        }

        // Give item to target
        targetItem.give(target);

        sender.sendMessage("§fItem was given to §b" + target.getName() + " §f!");

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, Command cmd, String label, String[] args) {
        return switch (args.length) {
            case 0 -> Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .toList();
            case 1 -> Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(name -> name.startsWith(args[0])).toList();
            case 2 -> itemService.getRegisteredIds().stream()
                    .filter(name -> name.startsWith(args[1])).toList();
            default -> List.of();
        };
    }
}
