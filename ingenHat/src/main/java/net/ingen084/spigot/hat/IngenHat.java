package net.ingen084.spigot.hat;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public final class IngenHat extends JavaPlugin {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
    	if(cmd.getName().equalsIgnoreCase("hat"))
    	{
    		if(!(sender instanceof Player))
    		{
    			sender.sendMessage("プレイヤー以外はブロックをかぶることはできません。");
    			return false;
    		}
    		PlayerInventory inv = ((Player)sender).getInventory();
    		ItemStack armor = inv.getHelmet();

    		inv.setHelmet(inv.getItemInMainHand());
    		inv.setItemInMainHand(armor);
    		return true;
    	}
    	return false;
    }
}
