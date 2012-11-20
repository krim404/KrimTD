package de.bdh.krimtd;

import java.util.List;

import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.EntityCreature;
import net.minecraft.server.PathEntity;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin
{
	public Economy econ = null;
	public TDListener TDListener = null;
	
	public BlockFace faces[] = 
	{
        BlockFace.NORTH, BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
    };
	
	public Main()
    {
 		
    }

    public void onDisable()
    {
        getServer().getScheduler().cancelTasks(this);
        System.out.println((new StringBuilder(String.valueOf(cmdName))).append("by ").append(author).append(" version ").append(version).append(" disabled.").toString());
    }

    public void onEnable()
    {	
    	pdf = getDescription();
        name = pdf.getName();
        cmdName = (new StringBuilder("[")).append(name).append("] ").toString();
        version = pdf.getVersion();
        author = "Krim";
    	
        System.out.println((new StringBuilder(String.valueOf(cmdName))).append("by ").append(author).append(" version ").append(version).append(" enabled.").toString());
        
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) 
        {
        	System.out.println((new StringBuilder()).append("[KX] unable to hook iconomy").toString()); 
        } else
        	econ = rsp.getProvider();
        
        this.TDListener = new TDListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(TDListener, this);
        
        TDTimer k = new TDTimer(this);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, k, 1, 1);
        
    }
    
    public void Tick()
    {
    	List<World> wlds = Bukkit.getWorlds();
    	Block s = null;
    	Location to = null;
    	for (World w: wlds)
    	{
    		List<LivingEntity> ent = w.getLivingEntities();
    		for (LivingEntity e: ent)
        	{
    			if(e instanceof Creature)
    			{
	    			if(!e.isDead())
	    			{
		    			s = getSpongeBelow(e.getLocation());
		    			if(s != null)
		    			{
		    				to = this.findNextPoint(e.getLocation());
		    				this.moveMob((Creature)e, to, 3.0f);
		    			}
	    			}
    			}
        	}
    	}
    }
    
    public void moveMob(Creature mob, Location to, float speed)
    {
	  	EntityCreature Mob = ((CraftCreature)mob).getHandle();
	  	PathEntity path = Mob.world.a(Mob, to.getBlockX(), to.getBlockY(), to.getBlockZ(), 100.0F, true, false, false, true);
	  	Mob.setPathEntity(path);
	  	Mob.getNavigation().a(path, speed);
    }
    
    public Block getSpongeBelow(Location l)
    {
    	Block b = l.getBlock();
    	int rad = 2;
    	Block temp = null;
    	for(int i$ = (rad * -1); i$ < rad; i$++)
        {
        	for(int j$ = -4; j$ < 0; j$++)
            {
        		for(int k$ = (rad * -1); k$ < rad; k$++)
                {
        			temp = b.getRelative(i$, j$, k$);
                    if(temp != null && temp.getType() == Material.SPONGE)
                    {
                    	return temp;
                    }
                }
            }
        }
    	
    	return null;
    }
    
    public Location findNextPoint(Location l)
    {
    	int rad = 10;
    	int len = 6;
    	int minlen = 2;
    	double dist = 0, dist2 = 0;
    	Block temp = null;
    	Block b = l.getBlock();
        for(int i$ = (rad * -1); i$ < rad; i$++)
        {
        	for(int j$ = (rad * -1); j$ < rad; j$++)
            {
        		for(int k$ = (rad * -1); k$ < rad; k$++)
                {
        			temp = b.getRelative(i$, j$, k$);
                    if(temp != null && temp.getType() == Material.SPONGE)
                    {
                    	Block g = this.getGold(temp);
                    	if(g != null)
                    	{
	                    	dist = temp.getWorld().getHighestBlockAt(temp.getLocation()).getLocation().distance(b.getLocation());
	                    	dist2 = g.getWorld().getHighestBlockAt(g.getLocation()).getLocation().distance(b.getLocation());
	                    	if(dist < len && dist > minlen && dist2 >= dist)
	                    	{
	                    		//FOUND
	                    		return b.getWorld().getHighestBlockAt(b.getLocation()).getRelative(BlockFace.UP).getLocation();
	                    	}
                    	}
                    	g = null;
                	}
                }
            }
        	
        }
        
    	return null;
    }
    
    public Block getGold(Block b)
    {
    	for (BlockFace f: faces)
    	{
    		if(b.getRelative(f) != null && b.getRelative(f).getType() == Material.GOLD_BLOCK)
    			return b;
    	}
    	return null;
    }
    
    public static PluginDescriptionFile pdf;
    public static String name;
    public static String cmdName;
    public static String version;
    public static String author;
}
