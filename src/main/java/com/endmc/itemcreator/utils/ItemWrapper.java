package com.endmc.itemcreator.utils;

import com.endmc.itemcreator.item.CustomItem;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class ItemWrapper {

    // We need this field to apply modification to the current item instance
    private static final Field handleField;

    static {
        try {
            handleField = CraftItemStack.class.getDeclaredField("handle");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        handleField.setAccessible(true);
    }

    private final net.minecraft.server.v1_12_R1.ItemStack nmsItem;
    private final ItemStack bukkitItem;

    public ItemWrapper(final CraftItemStack bukkitItem) {
        this.bukkitItem = bukkitItem;
        try {
            // Get NMS instance from bukkit instance
            this.nmsItem = (net.minecraft.server.v1_12_R1.ItemStack) handleField.get(bukkitItem);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private NBTTagCompound getNBT() {
        if (!nmsItem.hasTag()) {
            nmsItem.setTag(new NBTTagCompound());
        }
        return nmsItem.getTag();
    }

    public void setDisplayName(final String displayName) {
        final ItemMeta itemMeta = bukkitItem.getItemMeta();
        itemMeta.setDisplayName(displayName);

        bukkitItem.setItemMeta(itemMeta);
    }

    public void setLore(final List<String> lore) {
        final ItemMeta itemMeta = bukkitItem.getItemMeta();
        itemMeta.setLore(lore);

        bukkitItem.setItemMeta(itemMeta);
    }

    public <T> void setOption(final String name, final T value) {
        final NBTTagCompound tag = getNBT();

        switch (value.getClass().getSimpleName()) {
            case "Integer":
                tag.setInt(name, (Integer) value);
                break;
            case "Float":
                tag.setFloat(name, (Float) value);
                break;
            case "Double":
                tag.setDouble(name, (Double) value);
                break;
            case "String":
                tag.setString(name, (String) value);
                break;
            case "UUID":
                tag.setString(name, value.toString());
                break;
            case "Boolean":
                tag.setBoolean(name, (Boolean) value);
                break;
            case "Long":
                tag.setLong(name, (Long) value);
                break;
            case "Byte":
                tag.setByte(name, (Byte) value);
                break;
            case "Short":
                tag.setShort(name, (Short) value);
                break;
            default:
                throw new IllegalArgumentException("The given value is not supported by NBT");
        }
    }

    public <T> void setOption(final CustomItem.Option option, final T value) {
        setOption(option.getName(), value);
    }

    public <T> Optional<T> getOption(final String name, final Class<T> clazz) {
        final NBTTagCompound nbt = getNBT();

        // Check presence
        if (!nbt.hasKey(name)) {
            return Optional.empty();
        }

        return switch (clazz.getSimpleName()) {
            case "Integer" -> Optional.of((T) Integer.valueOf(nbt.getInt(name)));
            case "Float" -> Optional.of((T) Float.valueOf(nbt.getFloat(name)));
            case "Double" -> Optional.of((T) Double.valueOf(nbt.getDouble(name)));
            case "String" -> Optional.of((T) nbt.getString(name));
            case "UUID" -> Optional.of((T) UUID.fromString(nbt.getString(name)));
            case "Boolean" -> Optional.of((T) Boolean.valueOf(nbt.getBoolean(name)));
            case "Long" -> Optional.of((T) Long.valueOf(nbt.getLong(name)));
            case "Short" -> Optional.of((T) Short.valueOf(nbt.getShort(name)));
            default -> Optional.empty();
        };
    }

    public <T> Optional<T> getOption(final CustomItem.Option option) {
        return getOption(option.getName(), option.getValueClass());
    }

    public ItemStack getItem() {
        return bukkitItem;
    }
}
