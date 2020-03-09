package net.ingen084.bukkit.ingencustomcartspeed;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class VehicleSpeedChecker implements Listener {

    private Map<Minecart, Double> CartSpeeds;
    private Map<Minecart, Location> CartPrevPos;

    public VehicleSpeedChecker(IngenCustomCartSpeed iccs) {
        CartSpeeds = new HashMap<>();
        CartPrevPos = new HashMap<>();

        for (World world : iccs.getServer().getWorlds()) {
            for (Minecart cart : world.getEntitiesByClass(Minecart.class)) {
                CartSpeeds.put(cart, 0d);
                CartPrevPos.put(cart, cart.getLocation());
            }
        }

        new BukkitRunnable() {
            public void run() {
                for (Minecart cart : CartSpeeds.keySet()) {
                    World world = cart.getWorld();
                    if (CartPrevPos.containsKey(cart) && world == CartPrevPos.get(cart).getWorld())
                        CartSpeeds.put(cart, CartPrevPos.get(cart).distance(cart.getLocation()));
                    else
                        CartSpeeds.put(cart, 0d);

                    CartPrevPos.put(cart, cart.getLocation());
                }
            }
        }.runTaskTimer(iccs, 0L, 1L);

        new BukkitRunnable() {
            public void run() {
                for (Minecart cart : CartSpeeds.keySet()) {
                    if (cart.getPassengers() == null)
                        continue;
                    for (Entity pass : cart.getPassengers()) {
                        if (!(pass instanceof Player))
                            continue;
                        double speed = ((CartSpeeds.get(cart) * 20 * 60 * 60) / 1000);
                        ChatColor color = ChatColor.WHITE;
                        if ((int) speed >= 107.9999)
                            color = ChatColor.RED;
                        else if (speed >= 99.9999)
                            color = ChatColor.GOLD;
                        sendActionBar((Player) pass, "SPEED: " + color + MessageFormat.format("{0,number,000.000} km/h", speed));
                    }
                }
            }
        }.runTaskTimer(iccs, 0L, 2L);
    }

    @EventHandler
    public void OnCreateMinecart(VehicleCreateEvent e) {
        if (!(e.getVehicle() instanceof Minecart))
            return;
        
        if (!CartSpeeds.containsKey(e.getVehicle()))
            CartSpeeds.put((Minecart) e.getVehicle(), 0d);
        if (!CartPrevPos.containsKey(e.getVehicle()))
            CartPrevPos.put((Minecart) e.getVehicle(), e.getVehicle().getLocation());
        for (Entity pass : e.getVehicle().getPassengers()) {
            if (!(pass instanceof Player))
                continue;
            sendActionBar((Player) pass, "");
        }
    }

    @EventHandler
    public void OnEnterMinecart(VehicleEnterEvent e) {
        if (!(e.getVehicle() instanceof Minecart))
            return;

        if (!CartSpeeds.containsKey(e.getVehicle()))
            CartSpeeds.put((Minecart) e.getVehicle(), 0d);
        if (!CartPrevPos.containsKey(e.getVehicle()))
            CartPrevPos.put((Minecart) e.getVehicle(), e.getVehicle().getLocation());
    }

    @EventHandler
    public void OnEnterPortalMinecart(EntityPortalEvent e) {
        if (!(e.getEntity() instanceof Minecart))
            return;
        
        Minecart cart = (Minecart)e.getEntity();

        if (CartSpeeds.containsKey(cart))
            CartSpeeds.remove(cart);
        if (CartPrevPos.containsKey(cart))
            CartPrevPos.remove(cart);
    }

    @EventHandler
    public void OnDestroyMinecart(VehicleDestroyEvent e) {
        if (!(e.getVehicle() instanceof Minecart))
            return;
        
        if (CartSpeeds.containsKey(e.getVehicle()))
            CartSpeeds.remove(e.getVehicle());
        if (CartPrevPos.containsKey(e.getVehicle()))
            CartPrevPos.remove(e.getVehicle());
        for (Entity pass : e.getVehicle().getPassengers())
            if (pass instanceof Player)
                sendActionBar((Player) pass, "");
    }

    @EventHandler
    public void OnExitMinecart(VehicleExitEvent e) {
        if (!(e.getVehicle() instanceof Minecart))
            return;

        if (CartSpeeds.containsKey(e.getVehicle()))
            CartSpeeds.remove(e.getVehicle());
        if (CartPrevPos.containsKey(e.getVehicle()))
            CartPrevPos.remove(e.getVehicle());
        for (Entity pass : e.getVehicle().getPassengers())
            if (pass instanceof Player)
                sendActionBar((Player) pass, "");
    }

    private static void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }
}
