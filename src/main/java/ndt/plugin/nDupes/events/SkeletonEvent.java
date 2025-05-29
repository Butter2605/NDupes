package ndt.plugin.nDupes.events;

import ndt.plugin.nDupes.NDupes;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class SkeletonEvent implements Listener {
    private final NDupes plugin;
    private final Random random = new Random();

    public SkeletonEvent(NDupes instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onEntityHit(EntityDamageByEntityEvent event) {
        if (plugin.getConfig().getBoolean("skeleton-dupe.enabled")) {
            if (event.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) event.getDamager();

                if (event.getEntity() instanceof Player) {
                    Player player = (Player) event.getEntity();
                    PotionEffect resistance = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);

                    if (arrow.getShooter() instanceof Skeleton) {
                        if (player.isGliding()) {
                            Skeleton skeleton = (Skeleton) arrow.getShooter();

                            skeleton.damage(0, player);
                            arrow.setKnockbackStrength(0);
                            arrow.setDamage(0);
                            arrow.remove();

                            final int PROBABILITY = plugin.getConfig().getInt("skeleton-dupe.chance", 100);
                            int randomSuccess = random.nextInt(100);
                            if (randomSuccess >= PROBABILITY) return;

                            ItemStack itemInHand = player.getInventory().getItemInMainHand();
                            if (itemInHand.getType() != Material.AIR) {
                                ItemStack duplicatedItem = itemInHand.clone();
                                player.getWorld().dropItemNaturally(player.getLocation(), duplicatedItem);
                            }
                        }
                    }
                }
            }
        }
    }
}