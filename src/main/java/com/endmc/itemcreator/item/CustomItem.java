package com.endmc.itemcreator.item;

import com.endmc.itemcreator.utils.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public abstract class CustomItem {

    private final String itemId;
    private final Material material;
    private final String defaultDisplayName;
    private final List<String> defaultLore;

    public CustomItem(final String itemId, final Material material) {
        this(itemId, material, null, null);
    }

    public CustomItem(final String itemId, final Material material,
                      final String defaultDisplayName, final List<String> defaultLore) {
        this.itemId = itemId;
        this.material = material;
        this.defaultDisplayName = defaultDisplayName;
        this.defaultLore = defaultLore;
    }

    public final String getItemId() {
        return itemId;
    }

    public Material getMaterial() {
        return material;
    }

    public final void give(final Player target) {
        // By default, ItemStack is not a CraftItemStack when created
        final CraftItemStack craftItem = CraftItemStack.asCraftCopy(new ItemStack(material));
        final ItemWrapper itemWrapper = new ItemWrapper(craftItem);

        if (defaultDisplayName != null) {
            itemWrapper.setDisplayName(defaultDisplayName);
        }
        if (defaultLore != null) {
            itemWrapper.setLore(defaultLore);
        }

        itemWrapper.setOption(BasicOption.ID, itemId);

        target.getInventory().addItem(itemWrapper.getItem());
    }

    public interface Option {
        String getName();
        <T> Class<T> getValueClass();
    }

    /* Here is a way to implement Option with ItemWrapper.
        It adds the possibility to use named options instead of String.
     */
    public enum BasicOption implements CustomItem.Option {
        ID("item_creator_id", String.class),
        OWNER("item_owner", UUID.class),
        LAST_USED("item_last_used", Long.class);

        private final String name;
        private final Class<?> clazz;

        BasicOption(final String name, final Class<?> clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public <T> Class<T> getValueClass() {
            return (Class<T>) clazz;
        }
    }

}
