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
	public Location l;
	public TDTower(Main m, Block b, int Level, Player owner,Location l)
	{
		this.m = m;
		this.b = b;
		this.Level = Level;
		this.owner = owner;
		this.l = l;
	}
	
	public static int getPrice(int tp, int lvl)
	{
		int price = 0;
		int mult = 1;
		
		if(lvl == 5)
			mult = 10*25*5;
		else if(lvl == 4)
			mult = 10*25;
		else if(lvl == 3)
			mult = 10*5;
		else if(lvl == 2)
			mult = 5;
		
		if(tp == 1) //EIS
		{
			price = 100;
		} else if(tp == 2) //Flame
		{
			price = 80;
		} else if(tp == 3) //PBAOE
		{
			price = 1000;
		} else if(tp == 4) //AE
		{
			price = 200;
		} else if(tp == 5) //Block
		{
			price = 5;
		} else if(tp == 6) //Level
		{
			price = 10;
		} else if(tp == 7) //HeavyDamage
		{
			price = 500;
		} else if(tp == 0) //Arrow
		{
			price = 50;
		}
		return price * mult;
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
		} else if(tp == 11) // Purple - HeavyDamage
		{
			return 7;
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
	 
	
	public Entity fireArrow(Location to,boolean burning,int rangeMultip)
	{
		to.setY(to.getY()+1);
		Location spawn = this.b.getWorld().getHighestBlockAt(this.b.getLocation()).getLocation();  
	 	spawn.setX(spawn.getX()+0.5);  
	 	spawn.setZ(spawn.getZ()+0.5);  
	 	spawn.setY(spawn.getY()+rangeMultip);  
	 	Vector delta = to.subtract(spawn).toVector();  
	 	spawn.setPitch(getLookAtPitch(delta));   
	 	spawn.setYaw(getLookAtYaw(delta));  
	 	Location tmp = to.subtract(spawn);  
	 	tmp.setPitch(getLookAtPitch(delta));   
	 	tmp.setYaw(getLookAtYaw(delta));  
	 		  
	 	Arrow ar = b.getWorld().spawn(spawn, Arrow.class);  
	 	ar.setVelocity(tmp.getDirection().multiply(rangeMultip));  
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
		//In Range?
		if(e.getLocation().distance(this.b.getLocation()) < (2.5 + this.Level * 0.5))
		{
			//DAMAGE
			List<Location> smokeLocations = new ArrayList<Location>();
            smokeLocations.add(e.getLocation());
            smokeLocations.add(e.getLocation().clone().add(0.0D, 1.0D, 0.0D));
            SmokeUtil.spawnCloudRandom(smokeLocations,(float)0.5);
            this.doDamage(this.m.mob.get(e));
		}
	}
	
	public boolean doDamage(TDMob m)
	{
		int dmg = 1;
		int tp = this.getType();
		
		if(tp == 1) //EIS
		{
			dmg = 0;
			if(this.Level == 2)
				dmg = 0;
			else if(this.Level == 3)
				dmg = 10;
			else if(this.Level == 4)
				dmg = 50;
			else if(this.Level == 5)
				dmg = 200;
		} else if(tp == 2) //Flame per Fire Tick
		{
			dmg = 1;
			if(this.Level == 2)
				dmg = 2;
			else if(this.Level == 3)
				dmg = 20;
			else if(this.Level == 4)
				dmg = 120;
			else if(this.Level == 5)
				dmg = 700;
		} else if(tp == 3) //PBAOE
		{
			dmg = 2;
			if(this.Level == 2)
				dmg = 10;
			else if(this.Level == 3)
				dmg = 50;
			else if(this.Level == 4)
				dmg = 250;
			else if(this.Level == 5)
				dmg = 1200;
		} else if(tp == 4) //AE
		{
			dmg = 10;
			if(this.Level == 2)
				dmg = 50;
			else if(this.Level == 3)
				dmg = 300;
			else if(this.Level == 4)
				dmg = 1500;
			else if(this.Level == 5)
				dmg = 4000;
		} else if(tp == 5) //Block
		{
			dmg = 0;
		} else if(tp == 6) //Level
		{
			dmg = 0;
		} else if(tp == 7) //HeavyDamage
		{
			dmg = 100;
			if(this.Level == 2)
				dmg = 600;
			else if(this.Level == 3)
				dmg = 3000;
			else if(this.Level == 4)
				dmg = 16000;
			else if(this.Level == 5)
				dmg = 100000;
		} else if(tp == 0) //Arrow
		{
			dmg = 5;
			if(this.Level == 2)
				dmg = 20;
			else if(this.Level == 3)
				dmg = 100;
			else if(this.Level == 4)
				dmg = 500;
			else if(this.Level == 5)
				dmg = 2000;
		}
		
		m.doDamage(dmg);
		return true;
	}
	
	public int getType()
	{
		return TDTower.getType(this.b.getData());
	}
	
	public int getSlowTime()
	{
		int slow = 2;
		if(this.Level == 3)
			slow = 3;
		if(this.Level >= 4)
			slow = 4;
		return slow;
	}
	
	public int getFireTicks()
	{
		if(this.Level == 1)
			return 10;
		else if(this.Level == 5)
			return 20;
		else return 15;
	}
	
	public float getAERange() //Range vom AE Tower
	{
		return (float) (this.Level * 0.5 + 1.5);
	}
	
	private int ticker = 0;
	public void tick()
	{
		++ticker;
		int everytick = 10;
		int range = 10;
		int amount = 1;
		
		//Angriffsgeschwindigkeit und Menge
		if(this.getType() == 1) //Slow
		{
			amount = this.Level;
			everytick = 30;
			if(Level == 3)
				everytick = 20;
			else if(Level > 3)
				everytick = 10;
		}
		else if(this.getType() == 2) //DoT
		{
		}
		else if(this.getType() == 4) //AEDD
		{
			//Range in Listener
			everytick = 20;
		}
		else if(this.getType() == 0) //ARROW
		{
			if(this.Level == 3)
				amount = 2;
			else if(this.Level == 4)
				amount = 3;
			if(this.Level == 5)
				amount = 5;
		} 
		else if(this.getType() == 3) //PBAEDD
		{
			amount = 1000;
			everytick = 2;
		}
		else if(this.getType() == 7) //HeavyDamage
		{
			everytick = 50;
			range = 100;
			amount = 1;
		}

		if(ticker > everytick)
		{
			ticker = 0;
			Entity c;
			int gone = 0;
			
			List<LivingEntity> ent = this.b.getWorld().getLivingEntities();
			for (LivingEntity e: ent)
	    	{
				//Registrierter Mob
				if(this.m.mob.get(e) != null)
				{
					//Damage
					if(e.getLocation().distance(this.b.getLocation()) <= range)
					{
						c = null;
						if(this.getType() == 1) //Slow
						{
							c = this.fireSnowBall(e.getLocation());
						}
						else if(this.getType() == 2) //DoT
						{
							c = this.fireArrow(e.getLocation(),true,1);
						}
						else if(this.getType() == 4) //AEDD
						{
							c = this.fireCharge(e.getLocation());
						}
						else if(this.getType() == 0) //ARROW
						{
							c = this.fireArrow(e.getLocation(),false,1);
						} 
						else if(this.getType() == 3) //PBAEDD
						{
							this.doAEDamage(e);
						}
						else if(this.getType() == 7) //HeavyDamage
						{
							c = this.fireArrow(e.getLocation(),false,5);
						}
						
						if(c != null)
							this.m.shots.put(c, this);
						
						++gone;
						if(amount <= gone)
							return;
					}
				}
	    	}
		} 
	}
}
