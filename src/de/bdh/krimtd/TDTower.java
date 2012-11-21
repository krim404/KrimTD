package de.bdh.krimtd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.util.Vector;

public class TDTower 
{
	Main m;
	Block b;
	int Level;
	public Player owner;
	public TDTower(Main m, Block b, int Level, Player owner)
	{
		this.m = m;
		this.b = b;
		this.Level = Level;
		this.owner = owner;
	}
	
	public static int getPrice(int tp, int lvl)
	{
		//TODO: Preise festlegen
		return 1;
	}
	
	public int getPrice()
	{
		int price = 0;
		for(int i = this.Level; i > 0; --i)
		{
			price += TDTower.getPrice(this.getType(), i);
		}
		return price;
	}
	
	public static int getRadius(int lvl)
	{
		//TODO: Radius balancen
		return lvl * 2;
	}
	
	public int getRadius()
	{
		return TDTower.getRadius(this.Level);
	}
	
	public static int getType(int tp)
	{
		if(tp == 11) //BLUE - EISTOWER
		{
			return 1;
		} else if(tp == 14) //ROT - Flammentower
		{
			return 2;
		} else if(tp == 15) //SCHWARZ - AEDD
		{
			return 3;
		} else if(tp == 8) // GRAU - Granaten
		{
			return 4;
		} else if(tp == 13) // GRÜN - BlockTower
		{
			return 5;
		} else if(tp == 1) // ORANGE - LevelTower
		{
			return 6;
		} else //Pfeiltower
		{
			return 0;
		}
		
	}
	
	public static final float DEGTORAD = 0.017453293F;
    public static final float RADTODEG = 57.29577951F;
    
	public static float getLookAtPitch(Vector motion) {
        return -atan(motion.getY() / length(motion.getX(), motion.getZ()));
    }
    public static float atan(double value) {
        return RADTODEG * (float) Math.atan(value);
    }
    
    public static double lengthSquared(double... values) {
        double rval = 0;
        for (double value : values) {
            rval += value * value;
        }
        return rval;
    }
    public static double length(double... values) {
        return Math.sqrt(lengthSquared(values));
    }	
    
    public static float getLookAtYaw(Vector motion) 
	{
	        double dx = motion.getX();
	        double dz = motion.getZ();
	        double yaw = 0;
	        // Set yaw
	        if (dx != 0) {
	            // Set yaw start value based on dx
	            if (dx < 0) {
	                yaw = 1.5 * Math.PI;
	            } else {
	                yaw = 0.5 * Math.PI;
	            }
	            yaw -= Math.atan(dz / dx);
	        } else if (dz < 0) {
	            yaw = Math.PI;
	        }
	        return (float) (-yaw * 180 / Math.PI);
	}
	 
	
	public Entity fireArrow(Location to,boolean burning)
	{
		to.setY(to.getY()+1);
		Location spawn = this.b.getWorld().getHighestBlockAt(this.b.getLocation()).getLocation();  
	 	spawn.setX(spawn.getX()+0.5);  
	 	spawn.setZ(spawn.getZ()+0.5);  
	 	spawn.setY(spawn.getY()+1);  
	 	Vector delta = to.subtract(spawn).toVector();  
	 	spawn.setPitch(getLookAtPitch(delta));   
	 	spawn.setYaw(getLookAtYaw(delta));  
	 	Location tmp = to.subtract(spawn);  
	 	tmp.setPitch(getLookAtPitch(delta));   
	 	tmp.setYaw(getLookAtYaw(delta));  
	 		  
	 	Arrow ar = b.getWorld().spawn(spawn, Arrow.class);  
	 	ar.setVelocity(tmp.getDirection());  
		if(burning == true)
			ar.setFireTicks(60);
		
		return ar;
	}
	
	public Entity fireSnowBall(Location to)
	{
		to.setY(to.getY()+1);
		Location spawn = this.b.getWorld().getHighestBlockAt(this.b.getLocation()).getLocation();  
	 	spawn.setX(spawn.getX()+0.5);  
	 	spawn.setZ(spawn.getZ()+0.5);  
	 	spawn.setY(spawn.getY()+1);  
	 	Vector delta = to.subtract(spawn).toVector();  
	 	spawn.setPitch(getLookAtPitch(delta));   
	 	spawn.setYaw(getLookAtYaw(delta));  
	 	Location tmp = to.subtract(spawn);  
	 	tmp.setPitch(getLookAtPitch(delta));   
	 	tmp.setYaw(getLookAtYaw(delta));  
	 	
		Snowball s = b.getWorld().spawn(spawn, Snowball.class);
		s.setVelocity(tmp.getDirection());
		return s;
	}
	
	public Entity fireCharge(Location to)
	{
		Location from = this.b.getWorld().getHighestBlockAt(this.b.getLocation()).getLocation();
		Location spawn = this.makeSpawn(from,to);
		Location direction = this.makeVelocity(from,to);
		
		Fireball fball = this.b.getWorld().spawn(spawn.add(direction.getDirection()),Fireball.class);
        fball.setIsIncendiary(false);
        return fball;
	}
	
	public Location makeVelocity(Location spawn,Location to)
	{
		Location tmp = to.subtract(spawn);
		Vector delta = to.subtract(spawn).toVector();
		tmp.setPitch(getLookAtPitch(delta)); 
		tmp.setYaw(getLookAtYaw(delta));
		return tmp;
	}
	
	public Location makeSpawn(Location spawn,Location to)
	{
		spawn.setX(spawn.getX()+0.5);
		spawn.setZ(spawn.getZ()+0.5);
		spawn.setY(spawn.getY()+1);
		Vector delta = to.subtract(spawn).toVector();
		spawn.setPitch(getLookAtPitch(delta)); 
		spawn.setYaw(getLookAtYaw(delta));
		return spawn;
	}
	
	public void doAEDamage(LivingEntity e)
	{
		//Registrierter Mob
		if(this.m.mob.get(e) != null)
		{
			//In Range?
			if(e.getLocation().distance(this.b.getLocation()) < (1 + this.Level * 1.2))
			{
				//TODO: Balanciere Damage
				this.m.mob.get(e).doDamage(10 * this.Level);
				List<Location> smokeLocations = new ArrayList<Location>();
                smokeLocations.add(e.getLocation());
                smokeLocations.add(e.getLocation().clone().add(0.0D, 1.0D, 0.0D));
                SmokeUtil.spawnCloudRandom(smokeLocations,(float)0.5);
			}
		}
	}
	
	public int getType()
	{
		return TDTower.getType(this.b.getData());
	}
	
	private int ticker = 0;
	public void tick()
	{
		++ticker;
		if(ticker > 10)
		{
			ticker = 0;
			
			//TODO: Tick inkl Level des Towers zu Attackspeed addieren
			
			Entity c;
			List<LivingEntity> ent = this.b.getWorld().getLivingEntities();
			for (LivingEntity e: ent)
	    	{
				if(e.getLocation().distance(this.b.getLocation()) < 10)
				{
					c = null;
					if(this.getType() == 1)
					{
						c = this.fireSnowBall(e.getLocation());
					}
					else if(this.getType() == 2)
					{
						c = this.fireArrow(e.getLocation(),true);
					}
					else if(this.getType() == 4)
					{
						c = this.fireCharge(e.getLocation());
					}
					else if(this.getType() == 0)
					{
						c = this.fireArrow(e.getLocation(),false);
					} 
					else if(this.getType() == 3)
					{
						this.doAEDamage(e);
					}
					
					if(c != null)
						this.m.shots.put(c, this);
				}
	    	}
		} 
	}
}
