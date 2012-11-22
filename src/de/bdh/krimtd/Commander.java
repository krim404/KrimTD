package de.bdh.krimtd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class Commander implements CommandExecutor {
	
	Main plugin;
	public Commander(Main plugin)
	{
		this.plugin = plugin;
	}
	

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		if(!(sender instanceof Player))
			return true;
		
		if(this.plugin.Money.get((Player)sender) != null)
		{
			sender.sendMessage("Your Money: "+this.plugin.Money.get((Player)sender));
			sender.sendMessage("Your Income: "+this.plugin.Income.get((Player)sender));
		}
		return true;
	}
	
}
