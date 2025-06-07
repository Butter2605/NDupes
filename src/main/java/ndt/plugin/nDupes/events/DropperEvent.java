package ndt.plugin.nDupes.events;

import ndt.plugin.nDupes.NDupes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Dropper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DropperEvent implements Listener {
    private final NDupes plugin;
    private final Random random = new Random();
    private final ArrayList<Location> cldown;

    public DropperEvent(NDupes instance) {
        this.plugin = instance;
        this.cldown = new ArrayList<Location>();
    }

    @EventHandler
    public void onDropper(BlockDispenseEvent event) {
        if (!plugin.getConfig().getBoolean("dropper-dupe.enabled")) return;
        if (!(event.getBlock().getState() instanceof Dropper)) return;

        final Location l = event.getBlock().getLocation();
        if (this.cldown.contains(l)) return;

        final int PROBABILITY = plugin.getConfig().getInt("dropper-dupe.probability");
        final int COOLDOWN = plugin.getConfig().getInt("dropper-dupe.cooldown");

        if (random.nextInt(100) <= PROBABILITY) {
            this.cldown.add(l);
            event.getBlock().getWorld().dropItemNaturally(l, event.getItem());
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> this.cldown.remove(l), 20L * COOLDOWN);
        }
    }
}
