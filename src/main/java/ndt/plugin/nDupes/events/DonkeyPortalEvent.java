package ndt.plugin.nDupes.events;

import ndt.plugin.nDupes.NDupes;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().getType() == EntityType.DONKEY) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                recentlyFell.add(event.getEntity().getUniqueId());

                // Delete after 5s
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    recentlyFell.remove(event.getEntity().getUniqueId());
                }, 100L);
            }
        }
    }

    @EventHandler
    public void PortalEvent(EntityPortalEvent event) {
        if (plugin.getConfig().getBoolean("donkey-dupe.enabled")) {
            if (event.getEntity().getType() == EntityType.DONKEY) {
                Donkey donkey = (Donkey) event.getEntity();
                assert event.getTo() != null;
                if (event.getTo().getWorld().getEnvironment() == World.Environment.THE_END) {
                    if (recentlyFell.contains(donkey.getUniqueId())) {
                        donkey.setHealth(0.0);
                        AbstractHorseInventory inventory = (AbstractHorseInventory) donkey.getInventory();
                            for (ItemStack item : inventory) {
                                donkey.getWorld().dropItemNaturally(donkey.getLocation(), item.clone());
                            }
                    }
                }
            }
        }
    }
}
