package de.bdh.krimtd;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
		} else if(tp == 1) // ORANGE - LevelTower
		{
			return 5;
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
	 
	
	public void fireArrow(Location to)
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
		this.m.shots.put(ar, 1);
	}
	
	public void fireSnowBall(Location l)
	{
		
	}
	
	public void fireCharge(Location l)
	{
		Block from = this.b.getWorld().getHighestBlockAt(this.b.getLocation());
		Vector direction = l.getDirection().multiply(5);
		Fireball fball = this.b.getWorld().spawn(from.getLocation().add(direction.getX(), direction.getY(), direction.getZ()),Fireball.class);
        fball.setIsIncendiary(false);
        this.m.shots.put(fball,1);
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
			this.attack();
		}
		//TODO: Tick inkl Level zu Attack
	}
	
	public void attack()
	{
		/*if(this.m.debug == true)
			System.out.println("Tower FIRE");*/
		
		List<LivingEntity> ent = this.b.getWorld().getLivingEntities();
		for (LivingEntity e: ent)
    	{
			if(e.getLocation().distance(this.b.getLocation()) < 10)
			{
				//this.fireCharge(e.getLocation());
				this.fireArrow(e.getLocation());
			}
    	}
		//TODO: Attack implementieren
	}
}
