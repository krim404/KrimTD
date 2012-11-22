package de.bdh.krimtd;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Mushroom;

public class TDMob 
{
	public int level;
	public int hp;
	public int slowed;
	public int typ;
	Location target;
	public TDTower fireDamageFrom = null;
	LivingEntity e;
	Main m;
	
	public TDMob(Main m, LivingEntity e,int level)
	{
		this.m = m;
		this.e = e;
		this.level = level;
		this.typ = TDMob.getType(e);
		this.hp = TDMob.getHP(this.typ, level);
		
		
	}
	
	public static int getPrice(int typ, int level)
	{
		//TODO Preis
		return 1;
	}
	
	public void dropMoney()
	{
		int price = TDMob.getPrice(this.typ, this.level) / 2;
		int paid = 0;
		int t = 0;
		while(paid < price)
		{
			if((price - paid) > 64*10)
			{
				t = (price - paid) / 100;
				this.e.getWorld().dropItemNaturally(e.getEyeLocation(), new ItemStack(Material.GOLD_INGOT, t));
				paid += t*100;
			}
			else if((price - paid) > 64)
			{
				t = (price - paid) / 10;
				this.e.getWorld().dropItemNaturally(e.getEyeLocation(), new ItemStack(Material.GOLD_INGOT, t));
				paid += t*10;
			}
			else if((price - paid) < 64)
			{
				this.e.getWorld().dropItemNaturally(e.getEyeLocation(), new ItemStack(Material.GOLD_NUGGET, (price - paid)));
				paid = price;
			}
		}
	}
	
	public static int getHP(int typ,int lvl)
	{
		int hp = 10;
		if(typ == 1)
			hp = 10;
		else if(typ == 2)
			hp = 25;
		else if(typ == 3)
			hp = 100;
		else if(typ == 4)
			hp = 150;
		else if(typ == 5)
			hp = 100;
		else if(typ == 6)
			hp = 250;
		else if(typ == 7)
			hp = 350;
		else if(typ == 8)
			hp = 1000;
		else if(typ == 9)
			hp = 500;
		else if(typ == 10)
			hp = 1000;
		else if(typ == 11)
			hp = 2000;
		else if(typ == 12)
			hp = 10000;
		return hp * lvl;
	}
	
	public static int getType(Short oldDur)
	{
		if(oldDur == 93)
		{
			return 1;
		}
		else if(oldDur == 91)
		{
			return 2;
		}
		else if(oldDur == 90)
		{
			return 3;
		}
		else if(oldDur == 92)
		{
			return 4;
		}
		else if(oldDur == 95)
		{
			return 5;
		}
		else if(oldDur == 120)
		{
			return 6;
		}
		else if(oldDur == 51)
		{
			return 7;
		}
		else if(oldDur == 54)
		{
			return 8;
		}
		else if(oldDur == 96)
		{
			return 9;
		}
		else if(oldDur == 50)
		{
			return 10;
		}
		else if(oldDur == 66)
		{
			return 11;
		}
		else if(oldDur == 99)
		{
			return 12;
		}
		return 0;
	}
	
	public static int getType(LivingEntity e)
	{
		if(e instanceof Chicken)
			return 1;
		else if(e instanceof Sheep)
			return 2;
		else if(e instanceof Pig)
			return 3;
		else if(e instanceof Cow)
			return 4;
		else if(e instanceof Wolf)
			return 5;
		else if(e instanceof Villager)
			return 6;
		else if(e instanceof Skeleton)
			return 7;
		else if(e instanceof Zombie)
			return 8;
		else if(e instanceof Mushroom)
			return 9;
		else if(e instanceof Creeper)
			return 10;
		else if(e instanceof Witch)
			return 11;
		else if(e instanceof IronGolem)
			return 12;
		
		return 0;
	}
	
	public float getSpeed()
	{
		float spd = 0.3f;
		if(this.typ == 3 || this.typ == 8)
			spd = spd / 2;
		else if(this.typ == 5)
			spd = spd * 2;
		else if(this.typ == 12)
			spd = spd / 3;
		
		if(slowed >= 1)
		{
			--slowed;
			return spd / 3;
		} else
			return spd;
	}
	
	public void doDamage(int dmg)
	{
		this.hp -= dmg;
		if(this.hp <= 0)
		{
			this.e.playEffect(EntityEffect.DEATH);
			this.dropMoney();
			this.m.killMob(e);
			
		} else
		{
			this.e.playEffect(EntityEffect.HURT);
		}
	}
	
	boolean tck = false;
	public void Tick(EffectUtil ef)
	{
		//Sekundenticker
		if(tck == false)
			tck = true;
		else
			tck = false;
		
		
		if(this.slowed == 1 && tck == true)
		{
			for (Player p: Bukkit.getServer().getOnlinePlayers()) 
	    	{
				ef.playPotionEffect(p,(CraftEntity)this.e, 0x0000FF, 200);
	    	}
		}
		
		if(this.e.getFireTicks() > 0 && this.tck == false)
		{
			if(this.fireDamageFrom != null)
			{
				this.fireDamageFrom.doDamage(this);
			} else	
				this.doDamage(1);
		}
	}

}
