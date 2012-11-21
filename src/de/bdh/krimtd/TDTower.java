package de.bdh.krimtd;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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
		//TODO
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
		} else //Pfeiltower
		{
			return 0;
		}
		
	}
	public int getType()
	{
		return TDTower.getType(this.b.getData());
	}
	
	public void tick()
	{
		
	}
	
	public void attack()
	{
		
	}
}
