package de.bdh.krimtd;

import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TDMob 
{
	public int level;
	public int hp;
	public int slowed;
	Location target;
	LivingEntity e;
	Main m;
	
	public TDMob(Main m, LivingEntity e,int level)
	{
		this.m = m;
		this.e = e;
		this.level = level;
		this.hp = TDMob.getHP(TDMob.getType(e), level);
		
	}
	
	public static int getPrice(int typ)
	{
		//TODO Preis
		return 1;
	}
	
	public void dropMoney()
	{
		//TODO Droppe Goldbarren/Ignots oder Blöcke je nach Level und Typ
		this.e.getWorld().dropItemNaturally(e.getEyeLocation(), new ItemStack(Material.GOLD_INGOT, 1));
	}
	
	public static int getHP(int typ,int lvl)
	{
		//TODO HPBestimmung nach Typ und Level
		return 10;
	}
	
	public static int getType(LivingEntity e)
	{
		//TODO Typbestimmung
		return 1;
	}
	
	public float getSpeed()
	{
		//TODO Speedbestimmung nach Typ
		float spd = 0.3f;
		
		if(slowed == 1)
		{
			slowed = 0;
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
	
	public void Tick(EffectUtil ef)
	{
		if(this.slowed == 1)
		{
			for (Player p: Bukkit.getServer().getOnlinePlayers()) 
	    	{
				ef.playPotionEffect(p,(CraftEntity)this.e, 0x0000FF, 200);
	    	}
		}
	}

}
