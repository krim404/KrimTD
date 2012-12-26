package de.bdh.krimtd;



import net.minecraft.server.v1_4_6.DataWatcher;
import net.minecraft.server.v1_4_6.Packet40EntityMetadata;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer;
import org.bukkit.entity.Player;


public class EffectUtil 
{
	public Main plugin = null;
	public EffectUtil(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public void playPotionEffect(final Player player, final CraftEntity entity, int color, int duration) {
	    final DataWatcher dw = new DataWatcher();
	    dw.a(8, Integer.valueOf(0));
	    dw.watch(8, Integer.valueOf(color));
	    Packet40EntityMetadata packet = new net.minecraft.server.v1_4_6.Packet40EntityMetadata(entity.getEntityId(), dw,true);
	    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	 
	    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	        public void run() {
	        	try
	        	{
		            DataWatcher dwReal = ((CraftEntity)entity).getHandle().getDataWatcher();
		            dw.watch(8, dwReal.getInt(8));
		            Packet40EntityMetadata packet = new Packet40EntityMetadata(entity.getEntityId(), dw,true);
		            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	        	} catch(Exception e) {}
	        }
	    }, duration);
	}
	
	public void playEffect(CraftEntity entity, int color, int duration)
	{
		for (Player p: Bukkit.getServer().getOnlinePlayers()) 
        {
			this.playPotionEffect(p,entity,color,duration);
        }
	}
}
