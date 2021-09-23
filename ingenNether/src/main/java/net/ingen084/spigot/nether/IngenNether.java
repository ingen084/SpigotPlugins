package net.ingen084.spigot.nether;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class IngenNether extends JavaPlugin implements Listener {
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent event) {
		if (event.getPlayer().hasPlayedBefore())
			return;
		event.getPlayer().teleport(getServer().getWorld("world_nether").getSpawnLocation());
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		if (!event.isAnchorSpawn()) {
			event.setRespawnLocation(getServer().getWorld("world_nether").getSpawnLocation());
		}
	}
}
