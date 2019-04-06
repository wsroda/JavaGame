import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Player
{
	public double headx;
	public double heady;
	public double speed;
	private double movx;
	private double movy;
	private double alpha;

	private double rotspeed;

	public double lastHeadx;
	public double lastHeady;

	public boolean turningRight;
	public boolean turningLeft;

	public boolean isRunning;

	int id;

	public ArrayList<Line2D.Double> lineList = new ArrayList<Line2D.Double>();

	public int[] bonusTimers =
	{ 0, 0 };

	// BONUSY

	public boolean canMoveThroughWalls;

	Player(double x, double y, double alpha, int id)
	{
		headx = x;
		heady = y;
		speed = 1.;
		this.alpha = alpha;
		rotspeed = 5.25;
		this.id = id;
	}

	void move(double multiplier)
	{
		if (isRunning)
		{
			movx = speed*multiplier * Math.cos(Math.toRadians(alpha));
			movy = speed*multiplier * Math.sin(Math.toRadians(alpha));
				

			headx = headx + movx;
			heady = heady + movy;
			if (canMoveThroughWalls)
			{
				if (headx < 0)
				{
					headx = 500;
					lastHeadx = 500;
				}
				if (headx > 500)
				{
					headx = 0;
					lastHeadx = 0;
				}
				if (heady < 0)
				{
					heady = 500;
					lastHeady = 500;
				}
				if (heady > 500)
				{
					heady = 0;
					lastHeady = 0;
				}
			}
		}
	}

	void rotateL()
	{
		alpha -= rotspeed;
		alpha = alpha % 360;
	}

	void rotateR()
	{
		alpha += rotspeed;
		alpha = alpha % 360;
	}

	boolean checkCollision(ArrayList<Line2D.Double> List)
	{
		Point2D np1;
		Point2D np2;
		Point2D op1;
		Point2D op2;
		Line2D.Double currentLine;
		Line2D.Double nextLine = new Line2D.Double(headx, heady, lastHeadx, lastHeady);
		np1 = nextLine.getP1();
		np2 = nextLine.getP2();
		for (int i = 0; i < List.size(); i++)
		{
			currentLine = List.get(i);
			op1 = List.get(i).getP1();
			op2 = List.get(i).getP2();
			if (nextLine.intersectsLine(currentLine))
			{
				if (!(np1.equals(op1) || np2.equals(op2) || np1.equals(op2) || np2.equals(op1)))
				{
					return true;
				}

			}
		}
		return false;
	}

	void start()
	{
		isRunning = true;
		storeOldHead();
	}

	void hit()
	{
		isRunning = false;
		System.out.println("Kolizja");
	}

	void storeOldHead()
	{ // zapisanie wartosci aby dodac linie
		lastHeadx = headx;
		lastHeady = heady;
	}

	void addLine()
	{
		lineList.add(new Line2D.Double(headx, heady, lastHeadx, lastHeady));
	}

	void giveBonus(int type, int time)
	{
		bonusTimers[type] += time;
	}

}
