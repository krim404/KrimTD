package de.bdh.krimtd;

import org.bukkit.Bukkit;
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
		
		if(sender.hasPermission("td.admin") && cmd.getName().equals("setmoney"))
		{
			Player plx = null;
			int amount = 0;
			if(args.length == 1)
			{
				amount = Integer.parseInt(args[1]);
				plx = (Player)sender;
			}
			else
			{
				amount = Integer.parseInt(args[2]);
				plx = Bukkit.getServer().getPlayer(args[0]);
			}
			if(plx == null)
				sender.sendMessage("Player not found");
			else
			{
				this.plugin.Money.put(plx, amount);
				sender.sendMessage("Money of "+plx.getDisplayName()+" set to "+amount);
			}
		}
		return true;
	}
	
}
