package net.ingen084.bukkit.ingencustomcartspeed;

import java.text.MessageFormat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class CustomCartSpeed implements Listener {

    private IngenCustomCartSpeed plugin = null;

    public CustomCartSpeed(IngenCustomCartSpeed plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCartMove(VehicleMoveEvent event)
    {
        // Minecartのみで動作させる
        if(!(event.getVehicle() instanceof Minecart))
            return;

        var speed = 28.8D;
        var toLocation = event.getTo();
        var railBlock = toLocation.getBlock();

        // 座標･レールチェック
        if (toLocation.getY() < 2 || railBlock.getType() != Material.DETECTOR_RAIL)
            return;
        // 看板チェック
        var signBlock = railBlock.getRelative(0, -2, 0);
        if (!signBlock.getType().equals(Material.OAK_WALL_SIGN) && !signBlock.getType().equals(Material.OAK_SIGN))
            return;
        
        // 看板から読み取り
        var lines = ((Sign)signBlock.getState()).getLines();
        if (!lines[0].equalsIgnoreCase("[cart speed]"))
            return;
        
        // パースして最大値ごにょごにょして書き込み
        try {
            speed = Double.parseDouble(lines[1]);
        } catch (Exception ex) {
            return;
        }
        if (speed >= plugin.maxSpeed)
            speed = plugin.maxSpeed;
        speed /= 72;
        if (speed == ((Minecart) event.getVehicle()).getMaxSpeed())
            return;
        ((Minecart) event.getVehicle()).setMaxSpeed(speed);

        // メッセージ生成
        var color = ChatColor.GOLD;
        var kmhSpeed = speed * 72;
        if (kmhSpeed >= 107.9999)
            color = ChatColor.RED;
        else if ((int)kmhSpeed >= 99.9999)
            color = ChatColor.GOLD;

        // 搭乗者全員にメッセージを送信
        for (var entity : event.getVehicle().getPassengers())
            if (entity instanceof Player)
                ((Player)entity).sendTitle("",ChatColor.BLUE + "MAX SPEED: " + color + MessageFormat.format("{0,number,000.000} km/h", kmhSpeed), 5, 10, 5);
    }
}