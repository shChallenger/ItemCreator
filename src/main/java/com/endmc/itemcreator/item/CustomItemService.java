package com.endmc.itemcreator.item;

import com.endmc.itemcreator.utils.ItemWrapper;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.Set;

public final class CustomItemService {

    private final CustomItemRegistry itemRegistry;

    public CustomItemService(final CustomItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    public Set<String> getRegisteredIds() {
        return itemRegistry.getItems().keySet();
    }

    public Optional<CustomItem> getItem(final Class<? extends CustomItem> clazz) {
        return itemRegistry.getItems().values().stream()
                .filter(item -> item.getClass() == clazz)
                .findAny();
    }

    public Optional<CustomItem> getItem(final String id) {
        return Optional.ofNullable(itemRegistry.getItems().get(id));
    }

    public void callEvent(final Event event, final ItemStack itemstack) {
        final ItemWrapper wrapper = new ItemWrapper((CraftItemStack) itemstack);
        final Optional<String> itemId = wrapper.getOption(CustomItem.BasicOption.ID);

        // Item is not a custom one
        if (itemId.isEmpty())
            return;

        final CustomItem item = itemRegistry.getItems().get(itemId.get());

        // No Item with this id is registered
        if (item == null)
            return;

        final Method[] methods = item.getClass().getDeclaredMethods();

        /* Find and call methods that match the following conditons :
            - Annoted with @ItemEventHandler
            - 2 arguments : BukkitEvent, ItemWrapper
        */
        for (final Method method : methods) {
            if (!method.isAnnotationPresent(ItemEventHandler.class)) {
                continue ;
            }
            final Parameter[] parameters = method.getParameters();
            if (parameters.length == 2 && parameters[0].getType() == event.getClass()
                && parameters[1].getType() == ItemWrapper.class) {
                try {
                    method.invoke(item, event, wrapper);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
