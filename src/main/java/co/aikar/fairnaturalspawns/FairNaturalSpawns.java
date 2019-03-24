package co.aikar.fairnaturalspawns;

import com.destroystokyo.paper.event.entity.PlayerNaturallySpawnCreaturesEvent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Flying;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.plugin.java.JavaPlugin;

public final class FairNaturalSpawns extends JavaPlugin implements Listener {

    private YamlConfiguration spigotConfig;
    private int defaultSpawnRange;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        this.spigotConfig = Bukkit.spigot().getSpigotConfig();
        this.defaultSpawnRange = spigotConfig.getInt("world-settings.default.mob-spawn-range", 8);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerNaturallySpawnCreatures(PlayerNaturallySpawnCreaturesEvent event) {

        Player player = event.getPlayer();
        World world = player.getWorld();
        int spawnRange = (spigotConfig.getInt("world-settings." + world.getName() + ".mob-spawn-range", defaultSpawnRange)) << 4;
        long count = player.getNearbyEntities(spawnRange, spawnRange, spawnRange).stream()
                .filter(entity -> entity instanceof Flying || entity instanceof Monster || entity instanceof Slime)
                .filter(entity -> entity.getEntitySpawnReason() == SpawnReason.NATURAL)
                .count();
        if (count > world.getMonsterSpawnLimit()) {
            event.setCancelled(true);
        }
    }
}
