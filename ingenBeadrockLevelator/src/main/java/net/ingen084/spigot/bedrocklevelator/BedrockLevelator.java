package net.ingen084.spigot.bedrocklevelator;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class BedrockLevelator extends JavaPlugin implements Listener
{
    List<String> worlds;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig();
        worlds = getConfig().getStringList("Worlds");
    }

    @EventHandler(ignoreCancelled = true)
    public void ChunkGenerate(ChunkPopulateEvent e) {
        final World world = e.getWorld();

        Boolean isFound = false;
        for(String worldName : worlds) {
            if(worldName.equalsIgnoreCase(world.getName())) {
                isFound = true;
                break;
            }
        }
        if(!isFound)
            return;

        final int startX = e.getChunk().getX() * 16;
        final int endX = (e.getChunk().getX() + 1) * 16;
        final int startZ = e.getChunk().getZ() * 16;
        final int endZ = (e.getChunk().getZ() + 1) * 16;

        new BukkitRunnable() {
            public void run() {
                if (world.getEnvironment() == World.Environment.NORMAL) {
                    for (int i = startX; i < endX; i++)
                        for (int j = startZ; j < endZ; j++)
                            for (int k = 1; k <= 4; k++)
                                if (world.getBlockAt(i, k, j).getType() == Material.BEDROCK)
                                    world.getBlockAt(i, k, j).setType(Material.STONE);
                } else if (world.getEnvironment() == World.Environment.NETHER) {
                    for (int i = startX; i < endX; i++) {
                        for (int j = startZ; j < endZ; j++) {
                            for (int k = 1; k <= 4; k++)
                                if (world.getBlockAt(i, k, j).getType() == Material.BEDROCK)
                                    world.getBlockAt(i, k, j).setType(Material.NETHERRACK);
                            for (int k = 123; k <= 126; k++)
                                if (world.getBlockAt(i, k, j).getType() == Material.BEDROCK)
                                    world.getBlockAt(i, k, j).setType(Material.NETHERRACK);
                        }
                    }
                }
            }
        }.runTaskLater(this, 2L);
    }
}

