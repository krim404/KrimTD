package de.bdh.krimtd;


public class TDTimer implements Runnable {
	long time = 0;
	Main main;
	
	public TDTimer(Main main)
    {
        this.main = main;
    }
	
	public void run()
	{
		try
		{
			if (Math.abs(System.currentTimeMillis() - time) < 500)
			{
				return;
			}
			time = System.currentTimeMillis();
			this.main.Tick();
		} catch (Exception e) {}
		
	}
}
