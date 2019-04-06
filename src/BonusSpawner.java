import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BonusSpawner extends Thread
{

	ArrayList<Bonus> bonusList;
	int width, height;
	boolean running;

	BonusSpawner(ArrayList<Bonus> ab, int width, int height)
	{
		running = true;
		this.bonusList = ab;
		this.width = width;
		this.height = height;

		start();
	}

	public void run()
	{
		try
		{
			while (running)
			{
				int type = new Random().nextInt(2);
				
				bonusList.add(new Bonus(Math.random() * (width - 20), Math.random() * (height-20), type));
				//System.out.println(bonusList.get(bonusList.size()-1).getMinX());
				TimeUnit.SECONDS.sleep(8);
			}
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
