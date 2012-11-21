package de.bdh.krimtd;



public class TDAttackTimer implements Runnable {
	long time = 0;
	Main main;
	
	public TDAttackTimer(Main main)
    {
        this.main = main;
    }
	
	public void run()
	{
		try
		{
			if (Math.abs(System.currentTimeMillis() - time) < 100)
			{
				return;
			}
			time = System.currentTimeMillis();
			this.main.TickTD();
		} catch (Exception e) {}
		
	}
}