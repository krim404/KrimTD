package de.bdh.krimtd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.EntityCreature;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;



public class Main extends JavaPlugin
{
	public Economy econ = null;
	public TDListener TDListener = null;
	public boolean debug = true;

	
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
        
        TDAttackTimer kt = new TDAttackTimer(this);
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, kt, 1, 1);
        
    }
    
    public void killMob(LivingEntity e)
    {
    	this.ll.remove(e);
    	this.mob.remove(e);
    	e.setHealth(0);
    }
    
    public void TickTD()
    {
    	for (Map.Entry<Block,TDTower> entry : this.Tower.entrySet())
    	{
    		entry.getValue().tick();
    	}
    }
    
    HashMap<LivingEntity, TDMob> mob = new HashMap<LivingEntity,TDMob>();
    HashMap<Block, TDTower> Tower = new HashMap<Block,TDTower>();
    HashMap<LivingEntity, Location> ll = new HashMap<LivingEntity,Location>();
    HashMap<Entity, Integer> shots = new HashMap<Entity,Integer>();
    
    public void Tick()
    {
    	//Garbage Collector
    	for (Map.Entry<Entity,Integer> sub : this.shots.entrySet())
		{
			if(sub.getKey().getTicksLived() > 100)
				this.shots.remove(sub.getKey());
		}
    	
    	List<World> wlds = Bukkit.getWorlds();
    	Block s = null;
    	Location to = null;
    	Boolean rego = false;
    	for (World w: wlds)
    	{
    		List<LivingEntity> ent = w.getLivingEntities();
    		for (LivingEntity e: ent)
        	{
    			if(e instanceof Creature)
    			{
	    			if(!e.isDead())
	    			{
	    				rego = false;
    					if(this.ll.get(e) == null)
    					{
    						rego = true;
    					} else if(this.ll.get(e).distance(e.getLocation()) < 1)
    					{
    						if(debug == true)
		    					System.out.println("Mob not moving. Reregister");
    						rego = true;
    					}
    					
	    				if(this.mob.get(e) == null || rego == true || (this.mob.get(e) != null && e.getLocation().distance(this.mob.get(e).target) < 2.0))
	    				{
			    			s = getSpongeBelow(e.getLocation(),4);
			    			if(s != null)
			    			{
			    				Block sign = getBlockAround(s,Material.SIGN_POST);
			    				if(sign != null)
			    				{
			    					if(debug == true)
			    						System.out.println("Found FINISH");
			    					
			    					//Ziel gefunden
			    					int count = 0;
			    					count = this.lives(s, Integer.parseInt(this.readSign(sign,2)), 1);
			    					--count;
			    					
			    					String name = this.readSign(sign,1);
			    					if(count == 0)
			    					{
			    						this.sendall("The Team "+name+" has lost the game!");
			    						//Verloren
			    					} else if(count > 0)
			    					{
			    						this.sendall("The Team "+name+" has "+count+" lives left");
			    						//Leben abziehen
			    					}
			    					this.killMob(e);
			    					
			    				} else
			    				{
				    				//Goto next Point
				    				to = this.findNextPoint(e.getLocation());
				    				if(to != null)
				    				{
				    					TDMob m;
				    					
				    					//Mob Importer - Stufe 1
				    					if(this.mob.get(e) == null)
				    					{
				    						m = new TDMob(this,e,1);
				    						this.mob.put(e, m);
				    						if(this.debug == true)
				    							System.out.println("New Mob registered");
				    						
				    					} else
				    						 m = this.mob.get(e);
				    					
				    					m.target = to;
				    					this.moveMob(e, to, TDMob.getSpeed(e));
				    				}
				    				//Keine weiteren vorhanden. Kill
				    				else if(debug == true)
				    				{
				    					System.out.println("Cannot find next Point. Killing");
				    					this.killMob(e);
				    				}
			    				}
			    			} else
			    			{
		    					//Killing Mobs out of Range
			    				this.killMob(e);
			    			}
	    				}
	    				this.ll.put(e,e.getLocation());
	    			}
    			}
        	}
    	}
    }
    
    public void stopMob(LivingEntity mob)
    {
    	EntityCreature notchMob = ((CraftCreature)mob).getHandle();
    	notchMob.getNavigation().g();
    	
    	if(debug == true)
    	{
    		System.out.println("Mob has command to stop moving");	
    	}
    	
    }
    
    public void moveMob(LivingEntity mob, Location to,float speed)
    {
    	
    	EntityCreature notchMob = ((CraftCreature)mob).getHandle();
        notchMob.getNavigation().a(to.getX(), to.getY(), to.getZ(), speed);
    	notchMob.getNavigation().d(true);
    	
    	if(debug == true)
    	{
    		System.out.println("Mob has command to move on");	
    	}
    }
    
    public Block getSpongeBelow(Location l,int rad)
    {
    	Block b = l.getBlock();
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
                    	if(debug == true)
                    		System.out.println("Found Sponge below for Loc '"+l.getX()+","+l.getY()+","+l.getZ()+"':"+temp.getX()+","+temp.getY()+","+temp.getZ());
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
    	int len = 8;
    	int minlen = 1;
    	double dist = 0, dist2 = 0;
    	Block temp = null;
    	Block b = l.getWorld().getHighestBlockAt(l);
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
	                    		Location to = temp.getWorld().getHighestBlockAt(temp.getLocation()).getLocation();
	                    		if(debug == true)
	                    		{
		                    		System.out.println("Found Next Point:"+to.getX()+","+to.getY()+","+to.getZ());
	                    			System.out.println("Distanz:"+dist+",Gold:"+dist2);
	                    		}
	                    		//FOUND
	                    		return to;
	                    	}
                    	}
                	}
                }
            }
        	
        }
        
    	return null;
    }
    
    public int lives(Block b,int typeid, int rem)
    {
    	int rad = 5;
    	int am = 0;
    	Block t;
    	for(int i$ = (rad * -1); i$ < rad; i$++)
        {
        	for(int j$ = (rad * -1); j$ < rad; j$++)
            {
        		for(int k$ = (rad * -1); k$ < rad; k$++)
                {
        			t = b.getRelative(i$, j$, k$);
        			if(t.getTypeId() == typeid)
        			{
        				if(rem > 0)
        				{
        					t.setType(Material.AIR);
        					--rem;
        				}
        				++am;
        			}
                }
            }
        }
    	return am;
    }
    
    
    HashMap<Player, Integer> MaxMobLevelPerPlayer = new HashMap<Player,Integer>();
    public int calcMaxMobLevel(Location l)
    {
    	int rad = 5;
    	int am = 0;
    	Block t;
    	Block b = l.getBlock();
    	for(int i$ = (rad * -1); i$ < rad; i$++)
        {
        	for(int j$ = (rad * -1); j$ < rad; j$++)
            {
        		for(int k$ = (rad * -1); k$ < rad; k$++)
                {
        			t = b.getRelative(i$, j$, k$);
        			if(t.getType() == Material.WOOL && t.getData() == 1) //ORANGE
        			{
        				++am;
        			}
        			
                }
            }
        }
    	
    	if(debug == true)
    		System.out.println("Max Mob Level is: "+am);
    	return am;
    }
    
    public void registerTower(Block b,int lvl,Player owner)
    {
    	if(debug == true)
    		System.out.println("Register Tower Level "+lvl+" for player: "+owner.getDisplayName());
    	
    	this.Tower.put(b,new TDTower(this, b, lvl, owner));
    }
    
    public void unregisterTower(Block b)
    {
    	if(debug == true && this.Tower.get(b) != null)
	    	System.out.println("Unregistered Level "+this.Tower.get(b)+" Tower of player: " + this.Tower.get(b).owner.getDisplayName());
	    	
	    this.Tower.remove(b);
    }
    
    public void rePayPlayer(Block b,int lvl)
    {
    	//TODO
    	if(this.Tower.get(b) != null)
    	{
    		int money = this.Tower.get(b).getPrice();
    		money = (int) (money * 0.9);
    	}
    }
    
    public String readSign(Block b,int line)
    {
    	String s = "";
    	--line;
    	if(b.getType() == Material.SIGN || b.getType() == Material.SIGN_POST)
    	{
    		if(b.getState() instanceof Sign)
    		{
    			Sign sign = (Sign)b.getState();
    			s = sign.getLine(line);
    		}
    	}
    	return s;
    }
    
    public void sendall(String msg)
    {
    	for (Player p: Bukkit.getServer().getOnlinePlayers()) 
    	{
    		p.sendMessage(msg);
    	}
    }
    
    public Block getBlockAround(Block b,Material m)
    {
    	for (BlockFace f: faces)
    	{
    		if(b.getRelative(f) != null && b.getRelative(f).getType() == m)
    			return b.getRelative(f);
    	}
    	return null;
    	
    }
    
    public Block getGold(Block b)
    {
    	return getBlockAround(b,Material.GOLD_BLOCK);
    }
    
    public static PluginDescriptionFile pdf;
    public static String name;
    public static String cmdName;
    public static String version;
    public static String author;
}
