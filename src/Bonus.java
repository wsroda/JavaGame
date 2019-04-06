import java.awt.geom.Ellipse2D;

public class Bonus extends Ellipse2D.Double
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	// bonusy
//	0 - speed up player
//	1 - slow enemy
	double dy;
	int type;
	
	
	Bonus(double x, double y,int type)
	{
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = 20;
		this.height = 20;
		this.dy = 1;
	}
}