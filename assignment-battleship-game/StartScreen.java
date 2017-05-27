import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;

public class StartScreen extends JFrame implements ActionListener
{
	private Container cont;
	private JLabel description;
	private JButton createGame;
	private JButton connectToGame;
	private JPanel cg;
	
	public StartScreen()
	{
		cg = new JPanel();
		cont = getContentPane();
		cg.setLayout(new FlowLayout());
		URL imageURL = this.getClass().getResource("battleship.jpg");
		JLabel imageHolder = new JLabel(new ImageIcon(imageURL));
		cg.add(imageHolder);
		cg.add(Box.createRigidArea(new Dimension(300, 0)));
		description = new JLabel("Start a new game by choosing to:");
		cg.add(description);
		createGame = new JButton("Create a new game...");
		createGame.addActionListener(this);
		cg.add(createGame);
		connectToGame = new JButton("Connect to game...");
		connectToGame.addActionListener(this);
		cg.add(connectToGame);
		cont.add(cg);
		setTitle("Battleship Launcher");
		setSize(250, 250);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == createGame)
		{
			dispose();
			new OptionScreenMakeGame();
		}
		if(e.getSource() == connectToGame) 
		{
			dispose();
			new OptionScreenConnectTo();
		}
	}
	
	public static void main(String[] args)
	{
		new StartScreen();
	}
}