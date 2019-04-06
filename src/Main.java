import javax.swing.JFrame;

public class Main
{

	public Main()
	{

		JFrame frame = new JFrame();
		Gamepanel gamepanel = new Gamepanel();

		frame.add(gamepanel);
		frame.setTitle("GRA");
		frame.setLocationRelativeTo(null);

		frame.setResizable(false);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

	}

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		new Main();
	}

}
