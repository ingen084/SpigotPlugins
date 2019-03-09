package net.ingen084.bukkit.ingencustomcartspeed;

import org.bukkit.plugin.java.JavaPlugin;

public final class IngenCustomCartSpeed extends JavaPlugin {
    protected double maxSpeed;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        maxSpeed = getConfig().getDouble("MaxCartSpeed", 28.8);
        getServer().getPluginManager().registerEvents(new CustomCartSpeed(this), this);
        getServer().getPluginManager().registerEvents(new VehicleSpeedChecker(this), this);
    }
}
