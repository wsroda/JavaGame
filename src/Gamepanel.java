import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;

import javax.swing.JPanel;
import javax.swing.Timer;

//poprawic przechodzenie przez sciany 50%
//dodac drugiego playera + kolizje z nim DONE
//dodac odstepy w liniach DONE

public class Gamepanel extends JPanel implements ActionListener
{
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 500, HEIGHT = 500;
	private final int DELAY = 15;

	ArrayList<Player> Players;
	ArrayList<Bonus> bonusList;
	BonusSpawner bs;
	BonusTick bt;

	Player player1;
	Player player2;

	private boolean running;
	private int won;

	private int lineticks;
	private int holeticks;

	private Timer timer;

	public Gamepanel()
	{

		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		addKeyListener(new Controls());
		setFocusable(true);

		Players = new ArrayList<Player>();
		bonusList = new ArrayList<Bonus>();
		start();
	}

	public void start()
	{
		running = true;

		timer = new Timer(DELAY, this);
		timer.start();

		player1 = new Player(HEIGHT / 8, 7 * WIDTH / 8, 0, 1);
		player2 = new Player(7 * HEIGHT / 8, WIDTH / 8, 180, 2);
		Players.add(player1);
		Players.add(player2);
		bs = new BonusSpawner(bonusList, WIDTH, HEIGHT);
		bt = new BonusTick(Players);
		player1.start();
		player2.start();

		player1.canMoveThroughWalls = true; //// TEST
		player2.canMoveThroughWalls = true;
	}

	public void stop()
	{
		running = false;

	}

	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		super.paintComponent(g);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);

		g2d.setStroke((new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)));
		for (int counter = 0; counter < Players.size(); counter++)
		{
			Player player = Players.get(counter);

			if (player.id == 1)
				g.setColor(Color.BLUE);
			else if (player.id == 2)
				g.setColor(Color.GREEN);
			for (int i = 0; i < player.lineList.size(); i++)
			{
				g2d.drawLine((int) Math.round(player.lineList.get(i).getX1()),
						(int) Math.round(player.lineList.get(i).getY1()),
						(int) Math.round(player.lineList.get(i).getX2()),
						(int) Math.round(player.lineList.get(i).getY2()));
			}

			if (player.lineList.size() != 0 && !(holeticks >= 15))
			{
				g.drawLine((int) Math.round(player.headx), (int) Math.round(player.heady),
						(int) Math.round(player.lastHeadx), (int) Math.round(player.lastHeady));
			}

			if (!player.isRunning)
			{ // wizualizacja kolizji
				g2d.setColor(Color.RED);
			} else
			{
				if (player.id == 1)
					g.setColor(Color.BLUE);
				else if (player.id == 2)
					g.setColor(Color.GREEN);
			}

			g.fillOval((int) Math.round(player.headx) - 2, (int) Math.round(player.heady) - 2, 4, 4);

		}
//		g.setColor(Color.YELLOW);
//		for (int i = 0; i < bonusList.size(); i++)
//		{
//			g2d.fill(bonusList.get(i));
//		}

		for (int i = 0; i < bonusList.size(); i++)
		{
			g2d.setColor(Color.YELLOW);
			Bonus tb = bonusList.get(i);
			g2d.fill(tb);
			g2d.setColor(Color.BLACK);
			if (tb.type == 0)
				g2d.drawString("⚡  ", (int) tb.getMinX() + 6, (int) tb.getCenterY() + 4);
			else if (tb.type == 1)
				g2d.drawString("🐌", (int) tb.getMinX() + 4, (int) tb.getCenterY() + 4);
		}
		
		if (!running)
		{
			g2d.setColor(Color.WHITE);
			if (won==0)
			{
				g2d.drawString("Wygrał gracz niebieski", HEIGHT/2, WIDTH/2);
			} else if (won == 1)
			{
				g2d.drawString("Wygrał gracz zielony", HEIGHT/2, WIDTH/2);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (running)
		{
			if (player1.turningRight)
			{
				player1.rotateR();
			}
			if (player1.turningLeft)
			{
				player1.rotateL();
			}

			double multiplier;
			
			multiplier = 1;
			
			
			if (player1.bonusTimers[0]>0)
				multiplier*=1.5;
			else if (player2.bonusTimers[1]>0)
				multiplier /= 1.5;
				
			player1.move(multiplier);
			
			if (player2.turningRight)
			{
				player2.rotateR();
			}
			if (player2.turningLeft)
			{
				player2.rotateL();
			}

			multiplier = 1;
			
			if (player2.bonusTimers[0]>0)
				multiplier*=1.5;
			else if (player1.bonusTimers[1]>0)
				multiplier /= 1.5;
			player2.move(multiplier);

			if (lineticks >= 10)
			{

				if ((player1.checkCollision(player1.lineList) || (player1.checkCollision(player2.lineList)))
						&& player1.isRunning)
				{
					player1.hit();
					if (running)
					{
						System.out.println("Wygrał gracz zielony");
						won = 1;
						bs.running = false;
						bt.running = false;
						running = false;
					}

				}
				if ((player2.checkCollision(player1.lineList) || (player2.checkCollision(player2.lineList)))
						&& player2.isRunning)
				{
					player2.hit();
					if (running)
					{
						System.out.println("Wygrał gracz niebieski");
						won = 0;
						bs.running = false;
						bt.running = false;
						running = false;
					}
				}
				if (holeticks >= 15)
				{
					if (holeticks >= 16)
					{
						holeticks = 0;
					}

				} else
				{
					player1.addLine();
					player2.addLine();
				}
				player1.storeOldHead();
				player2.storeOldHead();

				holeticks++;
				lineticks = 0;

				for (int counter = 0; counter < Players.size(); counter++)
				{
					Player player = Players.get(counter);
					for (int i = 0; i < bonusList.size(); i++)
					{
						Bonus tb = bonusList.get(i);
						if (tb.contains(player.headx, player.heady))
						{
							
							player.bonusTimers[tb.type] += 5;
							bonusList.remove(i);
							break;
						}

					}
				}

			}
			lineticks++;

			repaint();
		}
	}

	private class Controls extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_RIGHT)
			{ // player 1
				player1.turningRight = true;
//			System.out.println("R");
			}
			if (key == KeyEvent.VK_LEFT)
			{
				player1.turningLeft = true;
//			System.out.println("L");
			}

			if (key == KeyEvent.VK_D)
			{ // player 2
				player2.turningRight = true;
//			System.out.println("R");
			}
			if (key == KeyEvent.VK_A)
			{
				player2.turningLeft = true;
//			System.out.println("L");
			}

		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_RIGHT)
			{ // player 1
				player1.turningRight = false;
//			System.out.println("released R");
			}
			if (key == KeyEvent.VK_LEFT)
			{
				player1.turningLeft = false;
//			System.out.println("released L");
			}
			if (key == KeyEvent.VK_D)
			{ // player 2
				player2.turningRight = false;
//			System.out.println("released R");
			}
			if (key == KeyEvent.VK_A)
			{
				player2.turningLeft = false;
//			System.out.println("released L");
			}
		}

	}
}
