package de.bdh.krimtd;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

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
}
