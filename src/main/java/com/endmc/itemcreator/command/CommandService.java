package com.endmc.itemcreator.command;

import com.endmc.itemcreator.ItemCreatorPlugin;
import com.endmc.itemcreator.command.executor.GiveCustomItem;
import com.endmc.itemcreator.item.CustomItemService;
import org.bukkit.command.CommandExecutor;

public final class CommandService {

    private final ItemCreatorPlugin plugin;
    private final CustomItemService itemService;

    public CommandService(final ItemCreatorPlugin plugin, final CustomItemService itemService) {
        this.plugin = plugin;
        this.itemService = itemService;
        registerCommands();
    }

    private void registerCommands() {
        registerCommand("givecustomitem", new GiveCustomItem(itemService));
    }

    private void registerCommand(final String name, final CommandExecutor executor) {
        plugin.getCommand(name).setExecutor(executor);
    }
}
