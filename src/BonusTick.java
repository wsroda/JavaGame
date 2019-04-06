import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BonusTick extends Thread
{
	ArrayList<Player> players;
	public boolean running = true;
	
	BonusTick(ArrayList<Player> players)
	{
		this.players = players;
		start();
	}

	public void run()
	{
		try
		{
			while (running)
			{
				for (int counter = 0; counter < players.size(); counter++)
				{
					Player player = players.get(counter);
					
					System.out.println(player.id+ " - " +player.bonusTimers[0] + "  " + player.bonusTimers[1]);

					for (int i = 0; i < player.bonusTimers.length; i++)
					{
						if (player.bonusTimers[i] > 0)
							player.bonusTimers[i]--;
					}
				}
				TimeUnit.SECONDS.sleep(1);
			}

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}