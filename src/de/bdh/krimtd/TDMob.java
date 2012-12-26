package de.bdh.krimtd;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftEntity;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
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
	public boolean redo = false;
	public boolean nodrop = false;
	Location target;
	public TDTower fireDamageFrom = null;
	LivingEntity e;
	public Player p = null;
	public TDTeam team = null;
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
		int price = 10;
		if(typ == 1)
			price = 10;
		else if(typ == 2)
			price = 20;
		else if(typ == 3)
			price = 40;
		else if(typ == 4)
			price = 100;
		else if(typ == 5)
			price = 250;
		else if(typ == 6)
			price = 500;
		else if(typ == 7)
			price = 750;
		else if(typ == 8)
			price = 1000;
		else if(typ == 9)
			price = 1250;
		else if(typ == 10)
			price = 2000;
		else if(typ == 11)
			price = 5000;
		else if(typ == 12)
			price = 10000;
		else if(typ == 13)
			price = 50000;
		return price * level;
	}
	
	public void dropMoney()
	{
		if(this.nodrop == true)
			return;
		
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
	
	public static int getIncomeHeight(int typ,int lvl)
	{
		int price = 1;
		if(typ == 1)
			price = 1;
		else if(typ == 2)
			price = 2;
		else if(typ == 3)
			price = 4;
		else if(typ == 4)
			price = 8;
		else if(typ == 5)
			price = 20;
		else if(typ == 6)
			price = 40;
		else if(typ == 7)
			price = 60;
		else if(typ == 8)
			price = 90;
		else if(typ == 9)
			price = 110;
		else if(typ == 10)
			price = 150;
		else if(typ == 11)
			price = 300;
		else if(typ == 12)
			price = 900;
		else if(typ == 13)
			price = 1000;
		return price * lvl;
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
		else if(typ == 13)
			hp = 100000;
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
		else if(oldDur == 64)
		{
			return 13;
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
		else if(e instanceof Wither)
			return 13;
		return 0;
	}
	
	public static EntityType getBukkitType(int typ)
	{
		if(typ == 1)
			return EntityType.CHICKEN;
		else if(typ == 2)
			return EntityType.SHEEP;
		else if(typ == 3)
			return EntityType.PIG;
		else if(typ == 4)
			return EntityType.COW;
		else if(typ == 5)
			return EntityType.WOLF;
		else if(typ == 6)
			return EntityType.VILLAGER;
		else if(typ == 7)
			return EntityType.SKELETON;
		else if(typ == 8)
			return EntityType.ZOMBIE;
		else if(typ == 9)
			return EntityType.MUSHROOM_COW;
		else if(typ == 10)
			return EntityType.CREEPER;
		else if(typ == 11)
			return EntityType.WITCH;
		else if(typ == 12)
			return EntityType.IRON_GOLEM;
		else if(typ == 13)
			return EntityType.WITHER;
		else
			return EntityType.CHICKEN;
	}
	
	public float getSpeed()
	{
		float spd = 0.3f;
		if(this.typ == 3 || this.typ == 8)
			spd = spd / 2;
		else if(this.typ == 5)
			spd = spd * 2;
		else if(this.typ == 12 || this.typ == 13)
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
			
			if(this.team != null)
				this.team.remMob(this);
			
		} else
		{
			this.e.playEffect(EntityEffect.HURT);
		}
	}
	
	boolean tck = false;
	int t = 0;
	public void Tick(EffectUtil ef)
	{
		if(this.e instanceof Creature)
		{
			((Creature)this.e).setTarget(null);
		}
		
		//Sekundenticker
		if(tck == false)
			tck = true;
		else
			tck = false;
		
		if(t > 10)
		{
			this.redo = true;
			t = 0;
		}
		else
			++t;
		//Mushroom HP Regen
		if(this.typ == 9)
		{
			this.hp += 5;
		}
		
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
