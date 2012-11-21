package de.bdh.krimtd;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TDListener implements Listener
{
	Main m;
	public TDListener(Main main) 
	{
		this.m = main;
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
    {
		if(event.getPlayer() == null)
			return;
		
		if(event.getBlock().getType() == Material.WOOL)
		{
			int lvl = 1;
			Block tmp = event.getBlock();
			while(tmp.getRelative(BlockFace.UP).getData() == event.getBlock().getData() && tmp.getRelative(BlockFace.UP).getType() == event.getBlock().getType())
			{
				tmp = tmp.getRelative(BlockFace.UP);
				++lvl;
			}
			while(tmp.getRelative(BlockFace.DOWN).getData() == event.getBlock().getData() && tmp.getRelative(BlockFace.DOWN).getType() == event.getBlock().getType())
			{
				tmp = tmp.getRelative(BlockFace.DOWN);
				++lvl;
			}
			
			if(lvl > 5)
			{
				event.getPlayer().sendMessage("Tower is on max level");
				event.setCancelled(true);
			}
			else
			{
				event.getPlayer().sendMessage("Tower is now level "+ lvl);
				this.m.registerTower(tmp, lvl,event.getPlayer());
			}
		}
    }
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
    {
		if(event.getPlayer() == null)
			return;
		
		if(event.getBlock().getType() == Material.WOOL)
		{
			int lvl = 1;
			Block tmp = event.getBlock();
			while(tmp.getRelative(BlockFace.UP).getData() == event.getBlock().getData() && tmp.getRelative(BlockFace.UP).getType() == event.getBlock().getType())
			{
				tmp = tmp.getRelative(BlockFace.UP);
				tmp.setType(Material.AIR);
				++lvl;
			}
			while(tmp.getRelative(BlockFace.DOWN).getData() == event.getBlock().getData() && tmp.getRelative(BlockFace.DOWN).getType() == event.getBlock().getType())
			{
				tmp = tmp.getRelative(BlockFace.DOWN);
				tmp.setType(Material.AIR);
				++lvl;
			}
			
			this.m.rePayPlayer(tmp, lvl);
			this.m.unregisterTower(tmp);
			
		}
    }
	
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event)
	{
		if(event.getAction() == Action.LEFT_CLICK_AIR)
		{
			int mxl = this.m.calcMaxMobLevel(event.getPlayer().getLocation());
			int nl = 0;
			if(this.m.MaxMobLevelPerPlayer.get(event.getPlayer()) != null)
			{
				nl = this.m.MaxMobLevelPerPlayer.get(event.getPlayer());
			}
			
			if(nl >= mxl)
				nl = 0;
			
			++nl;
			
			this.m.MaxMobLevelPerPlayer.put(event.getPlayer(), nl);
			event.getPlayer().sendMessage("Now hiring mobs with level: "+nl);
		}
		
		
		//TODO: Spawn Mobs manuell und schmeiss sie in die TDMob
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event)
    {
		//Nix wird on Death gedroppt
		event.getDrops().clear();
    }
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		this.m.MaxMobLevelPerPlayer.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerPickupItem(PlayerPickupItemEvent event) 
	{
		if(event.getItem().getItemStack().getType() == Material.GOLD_NUGGET)
		{
			//TODO: Gib spieler Geld
		}
		
		if(event.getItem().getItemStack().getType() == Material.GOLD_INGOT)
		{
			//TODO: Gib spieler Geld
		}
		
		if(event.getItem().getItemStack().getType() == Material.GOLD_BLOCK)
		{
			//TODO: Gib spieler Geld
		}
	}
}
