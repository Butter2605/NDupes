package ndt.plugin.nDupes.events;

import ndt.plugin.nDupes.NDupes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DonkeyPortalEvent implements Listener {
    private final NDupes plugin;
    private final Set<UUID> recentlyFell = new HashSet<>();


    public DonkeyPortalEvent(NDupes plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getType() == EntityType.DONKEY || event.getEntity().getType() == EntityType.MULE || event.getEntity().getType() == EntityType.LLAMA) {
            Entity entity = event.getEntity();
            if (plugin.getConfig().getBoolean("donkey-dupe.enabled")) {
                int times = 2;
                if (plugin.getConfig().getBoolean("donkey-dupe.triple-drops")) {
                    times = 3;
                }

                for (int i = 0; i < times; i++) {
                    for (int x = 0; x < event.getDrops().size(); x++) {
                        entity.getWorld().dropItemNaturally(entity.getLocation(), event.getDrops().get(x));
                    }
                }
            }
        }
    }
}
