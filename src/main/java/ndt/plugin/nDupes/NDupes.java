package ndt.plugin.nDupes;

import ndt.plugin.nDupes.events.DonkeyPortalEvent;
import ndt.plugin.nDupes.events.SkeletonEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

public final class NDupes extends JavaPlugin {
    FileConfiguration config;
    private static NDupes plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        config = getConfig();
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new SkeletonEvent(this), this);
        getServer().getPluginManager().registerEvents(new DonkeyPortalEvent(this), this);
        Bukkit.getLogger().info("[NDupes] Plugin loaded successfully");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public NDupes getPlugin() {
        return getPlugin(NDupes.class);
    }
}
