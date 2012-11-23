package de.bdh.krimtd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.EntityCreature;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	public boolean debug = false;

	
	public BlockFace faces[] = 
	{
        BlockFace.NORTH, BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST
    };
	
	public Main()
    {
 		
    }

    public void onDisable()
    {
    	this.restart(false);
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
        
        Commander c = new Commander(this);
        getCommand("td").setExecutor(c); 
        getCommand("setmoney").setExecutor(c);
        
        this.restart(true);
        
    }
    
    public void restart(boolean msg)
    {
        for (Player p: Bukkit.getServer().getOnlinePlayers()) 
    	{
        	this.Money.put(p, 200);
        	this.Income.put(p, 30);
        	if(msg == true)
        		p.sendMessage(ChatColor.AQUA+"You've got 200$ to start");
    	}
        
        for(Map.Entry<LivingEntity, TDMob> m: this.mob.entrySet())
    	{
        	m.getKey().setHealth(0);
    	}
        this.mob.clear();
        this.ll.clear();

        for(Entry<Block, TDTower> t: this.Tower.entrySet())
    	{
        	this.removeTower(t.getKey(), false, null);
    	}
        
        this.Tower.clear();
    }
    
    
    public boolean removeTower(Block e,boolean giveMoney,Player by)
    {
    	
    	Block twr = e;
		while(twr.getRelative(BlockFace.DOWN).getData() == e.getData() && twr.getRelative(BlockFace.DOWN).getType() == e.getType())
		{
			twr = twr.getRelative(BlockFace.DOWN);
		}
		
    	if(this.Tower.get(twr) != null)
		{	
    		if(by == null || this.Tower.get(twr).owner.getName().equalsIgnoreCase(by.getName()))
    		{
		    	int lvl = 0;
		    	Block tmp = e;
				while(tmp.getRelative(BlockFace.UP).getData() == e.getData() && tmp.getRelative(BlockFace.UP).getType() == e.getType())
				{
					tmp = tmp.getRelative(BlockFace.UP);
					tmp.setType(Material.AIR);
					++lvl;
				}
				tmp = e;
				while(tmp.getRelative(BlockFace.DOWN).getData() == e.getData() && tmp.getRelative(BlockFace.DOWN).getType() == e.getType())
				{
					tmp = tmp.getRelative(BlockFace.DOWN);
					tmp.setType(Material.AIR);
					++lvl;
				}
				
				e.setType(Material.AIR);
	
				if(giveMoney == true)
					this.rePayPlayer(twr, lvl, this.Tower.get(twr).owner);
				
				if(by != null)
					this.unregisterTower(twr);
				return true;
    		} 
		}
    	return false;
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
    HashMap<Entity, TDTower> shots = new HashMap<Entity,TDTower>();
    
    public void TickIncome()
    {
    	int oldMoney = 0;
    	for (Player p: Bukkit.getServer().getOnlinePlayers()) 
    	{
    		oldMoney = 0;
    		if(this.Money.get(p) != null)
    			oldMoney = this.Money.get(p);
    		if(this.Income.get(p) != null)
    		{
    			this.Money.put(p,(oldMoney+this.Income.get(p)));
    			p.sendMessage(ChatColor.AQUA+"You've got your Income of: "+this.Income.get(p));
    		}
    	}
    }
    
    int tck = 0;
    public void Tick()
    {
    	//Jede 30 Sek
    	if(tck >= 60)
    	{
    		this.TickIncome();
    		tck = 0;
    	} else
    		++tck;
    	
    	
    	EffectUtil ef = new EffectUtil(this);
    	for(Map.Entry<LivingEntity, TDMob> m: this.mob.entrySet())
    	{
    		m.getValue().Tick(ef);
    	}
    	
    	//Garbage Collector
    	for (Map.Entry<Entity,TDTower> sub : this.shots.entrySet())
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
		    					System.out.println(ChatColor.YELLOW+"Mob not moving. Reregister");
    						rego = true;
    					} else
    					{
    						this.ll.put(e, e.getLocation());
    					}
    					
	    				if(this.mob.get(e) == null || rego == true || (this.mob.get(e) != null && this.mob.get(e).redo == true) || (this.mob.get(e) != null && e.getLocation().distance(this.mob.get(e).target) < 2.0))
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
			    						this.sendall(ChatColor.RED+"The Team "+name+" has lost the game!");
			    						//Verloren
			    					} else if(count > 0)
			    					{
			    						this.sendall(ChatColor.RED+"The Team "+name+" has "+count+" lives left");
			    						//Leben abziehen
			    					}
			    					this.killMob(e);
			    					
			    				} else
			    				{
				    				//Goto next Point
				    				to = this.findNextPoint(e.getLocation(),s);
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
				    					this.moveMob(e, to, m.getSpeed());
				    					m.redo = false;
				    				}
				    				//Keine weiteren vorhanden. Kill
				    				else
				    				{
				    					if(debug == true)
				    						System.out.println(ChatColor.RED+"Cannot find next Point. Killing");
				    					this.killMob(e);
				    				}
			    				}
			    			} else
			    			{
		    					//Killing Mobs out of Range
			    				this.killMob(e);
			    			}
	    				}
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
        	for(int j$ = -4; j$ < 4; j$++)
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
    
    public boolean isAboveWayPoint(Block b)
    {
    	Block tmp;
    	for(int i = 0; i > -5; --i)
    	{
    		tmp = b.getRelative(0, i, 0);
    		if(tmp != null)
    		{
    			if(tmp.getType() == Material.SPONGE)
    				return true;
    		}
    	}
    	return false;
    }
    public boolean closeToPoint(Location l, int rad)
    {
    	if(l.getBlock() != null)
    	{
	    	Block temp = null;
	    	for(int i$ = (rad * -1); i$ < rad; i$++)
	        {
	        	for(int j$ = (rad * -1); j$ < rad; j$++)
	            {
	        		for(int k$ = (rad * -1); k$ < rad; k$++)
	                {
	        			temp = l.getBlock().getRelative(i$, j$, k$);
	        			if(temp != null && temp.getType() == Material.SPONGE)
	        			{
	        				return true;
	        			}
	                }
	            }
	        }
    	}
    	return false;
    }
    
    public Location findNextPoint(Location l, Block actualBlock)
    {
    	int rad = 10;
    	int len = 8;
    	int minlen = 2;
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
	                    	if(dist < len && dist > minlen && dist2 > dist)
	                    	{
	                    		boolean brk = false;
	                    		Location to = temp.getWorld().getHighestBlockAt(temp.getLocation()).getRelative(BlockFace.UP).getLocation();

	                    		if(actualBlock != null)
	                    		{
		                    		Location old = temp.getWorld().getHighestBlockAt(actualBlock.getLocation()).getRelative(BlockFace.UP).getLocation();
		                    		if(old.distance(to) < 3)
		                    			brk = true;
	                    		}
	                    		
	                    		if(brk == false)
	                    		{
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
    HashMap<Player, Integer> Money = new HashMap<Player,Integer>();
    HashMap<Player, Integer> Income = new HashMap<Player,Integer>();
    
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
    	
    	if(am > 5)
    		am = 5;
    	
    	if(debug == true)
    		System.out.println("Max Mob Level is: "+am);
    	return am;
    }
    
    public void registerTower(Block b,int lvl,Player owner)
    {
    	if(debug == true)
    		System.out.println("Register Tower Level "+lvl+" for player: "+owner.getDisplayName());
    	
    	this.Tower.put(b,new TDTower(this, b, lvl, owner,b.getLocation()));
    }
    
    public void unregisterTower(Block b)
    {
    	if(debug == true && this.Tower.get(b) != null)
	    	System.out.println("Unregistered Level "+this.Tower.get(b)+" Tower of player: " + this.Tower.get(b).owner.getDisplayName());
	    	
	    this.Tower.remove(b);
    }
    
    public void rePayPlayer(Block b,int lvl,Player p)
    {
    	if(this.Tower.get(b) != null)
    	{
    		int money = this.Tower.get(b).getPrice();
    		money = (int) (money * 0.7);
    		p.sendMessage(ChatColor.AQUA+"You've got "+money+" for selling the tower");
    		int oldMon = 0;
    		if(this.Money.get(p) != null)
    			oldMon = this.Money.get(p);
    		
    		oldMon += money;
    		this.Money.put(p, oldMon);
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
