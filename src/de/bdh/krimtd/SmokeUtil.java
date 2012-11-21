package de.bdh.krimtd;

import java.util.*;
import org.bukkit.*;

@SuppressWarnings("rawtypes")
public class SmokeUtil
{

    public SmokeUtil()
    {
    }

    public static void spawnSingle(Location location, int direction)
    {
        if(location == null)
        {
            return;
        } else
        {
        	location.getWorld().playEffect(location, Effect.SMOKE, direction);
            return;
        }
    }

    public static void spawnSingle(Location location)
    {
        spawnSingle(location, 4);
    }

    public static void spawnSingleRandom(Location location)
    {
        spawnSingle(location, random.nextInt(9));
    }

    public static void spawnCloudSimple(Location location)
    {
        for(int i = 0; i <= 8; i++)
            spawnSingle(location, i);

    }

	public static void spawnCloudSimple(Collection locations)
    {
        Location location;
        for(Iterator i$ = locations.iterator(); i$.hasNext(); spawnCloudSimple(location))
            location = (Location)i$.next();

    }

    public static void spawnCloudRandom(Location location, float thickness)
    {
        int singles = (int)Math.floor(thickness * 9F);
        for(int i = 0; i < singles; i++)
            spawnSingleRandom(location);

    }

    public static void spawnCloudRandom(Collection locations, float thickness)
    {
        Location location;
        for(Iterator i$ = locations.iterator(); i$.hasNext(); spawnCloudRandom(location, thickness))
            location = (Location)i$.next();

    }

    public static Random random = new Random();

}
