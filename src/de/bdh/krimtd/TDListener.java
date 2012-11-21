package de.bdh.krimtd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
		this.m.findNextPoint(event.getBlock().getLocation());
    }
}
