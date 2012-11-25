package de.bdh.krimtd;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class TDTeam 
{
	Main m;
	List<TDMob> ents = new ArrayList<TDMob>();
	List<Player> players = new ArrayList<Player>();
	
	public TDTeam(Main m)
	{
		this.m = m;
	}
	
	public void enlistPlayer(Player p)
	{
		m.inteam.put(p, this);
		this.players.add(p);
	}
	
	public void removePlayer(Player p)
	{
		m.inteam.remove(p);
		this.players.remove(p);
	}
	
	public void addMob(TDMob m)
	{
		this.ents.add(m);
	}
	
	public void remMob(TDMob m)
	{
		this.ents.remove(m);
	}
}
