package de.bdh.krimtd;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;


public class configManager {
	
	protected static Main plugin;
    protected static YamlConfiguration conf;
    
    // features
    public static String Type = "TD";
    private static File confFile;
    
	
    public configManager(Main main) {
    	plugin = main;
    	File theDir = new File(plugin.getDataFolder(),"");
		if (!theDir.exists())
		{
			theDir.mkdir();
		}
    	setupconf();
    	load();
    }
    
	private static void load() 
	{
		try {
			conf.load(confFile);
		} catch (Exception e) {
			e.printStackTrace();		
		}
		
		Type = conf.getString("Gametype", Type);

        
        try {
        	if (!confFile.exists())
        		confFile.createNewFile();
			conf.save(confFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void reload() {
		load();
	}
	
    private static void setupconf() {
        confFile = new File(plugin.getDataFolder(), "config.yml");
        conf = null;
        
        if (confFile.exists())
        {
            conf = new YamlConfiguration();
            try {
				conf.load(confFile);
			} catch (Exception e) {
				e.printStackTrace();
			}       
        }
        else {
            File confFile;
            confFile = new File(plugin.getDataFolder(), "config.yml");
            conf = new YamlConfiguration();

            conf.set("Gametype", Type);
            try {
                confFile.createNewFile();
				conf.save(confFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
}
