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
	
	public int getType()
	{
		if(this.b.getData() == 11) //BLUE - EISTOWER
		{
			return 1;
		} else if(this.b.getData() == 14) //ROT - Flammentower
		{
			return 2;
		} else if(this.b.getData() == 15) //SCHWARZ - AEDD
		{
			return 3;
		} else if(this.b.getData() == 8) // GRAU - Granaten
		{
			return 4;
		} else //Pfeiltower
		{
			return 0;
		}
	}
	
	public void tick()
	{
		
	}
	
	public void attack()
	{
		
	}
}
