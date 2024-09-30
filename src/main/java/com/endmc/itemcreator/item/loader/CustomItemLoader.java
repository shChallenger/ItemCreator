package com.endmc.itemcreator.item.loader;

import com.endmc.itemcreator.item.CustomItem;
import com.endmc.itemcreator.item.CustomItemRegistry;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public final class CustomItemLoader {

    private final List<Class<? extends CustomItem>> customItems;

    public CustomItemLoader() {
        this.customItems = new ArrayList<>();
    }

    private void searchItems(final JarFile jarFile) {
        final Enumeration<JarEntry> entries = jarFile.entries();

        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            final String entryName = entry.getName();

            // Check for .class files
            if (!entryName.endsWith(".class")) {
                continue;
            }

            // Remove .class suffix
            final String className = entryName.replace('/', '.').substring(0, entryName.length() - 6);
            final Class<? > clazz;

            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found: " + className, e);
            }

            // Verify class extends CustomItem
            if (clazz != CustomItem.class && CustomItem.class.isAssignableFrom(clazz)) {
                customItems.add((Class<? extends CustomItem>) clazz);
            }
        }
    }

    public void searchItems(final String packageName) {
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final String packagePath = packageName.replace('.', '/');
        final Enumeration<URL> resources;

        try {
            resources = classLoader.getResources(packagePath);
        } catch (IOException e) {
            throw new RuntimeException("Error loading resources from package: " + packageName, e);
        }

        while (resources.hasMoreElements()) {
            final URL resource = resources.nextElement();

            // Checking we are in jar file
            if (!resource.getProtocol().equals("jar")) {
                continue;
            }

            final JarURLConnection jarConn;
            final JarFile jarFile;

            try {
                jarConn = (JarURLConnection) resource.openConnection();
                jarFile = jarConn.getJarFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            searchItems(jarFile);

            try {
                jarFile.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public CustomItemRegistry loadItems() {
        final List<Class<? extends CustomItem>> itemClasses = customItems;
        final CustomItemRegistry itemRegistry = new CustomItemRegistry(itemClasses.size());

        for (final Class<? extends CustomItem> itemClass : itemClasses) {
            final CustomItem itemInstance;

            try {
                /* Get instance with default constructor. We assume for the exercise that CustomItem
                classes have 1 constructor with no parameters */
                itemInstance = itemClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

            Bukkit.getLogger().log(Level.INFO, "Item " + itemClass.getSimpleName() + " instantiated!");

            itemRegistry.addItem(itemInstance);
        }

        return itemRegistry;
    }

}