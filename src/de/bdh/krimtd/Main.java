package de.bdh.krimtd;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	public Economy econ = null;
	public TDListener TDListener = null;
	public Main()
    {
 		
    }

    public void onDisable()
    {
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
        
    }
    
    public void Tick()
    {
    	
    }
    
    public static PluginDescriptionFile pdf;
    public static String name;
    public static String cmdName;
    public static String version;
    public static String author;
}
