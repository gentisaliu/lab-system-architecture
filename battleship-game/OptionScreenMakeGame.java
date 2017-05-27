import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class OptionScreenMakeGame extends JFrame implements ActionListener
{
	private JLabel text;
	private JButton classic;
	private JButton extended;
	
	public OptionScreenMakeGame()
	{
		setLayout(new FlowLayout());
		text = new JLabel("<html>Select the classic or extended edition<br>from below; the classic edition contains<br>the standard game features and is<br>suitable for playing with other clients</html>");
		add(text);
		classic = new JButton("Classic Edition");
		classic.addActionListener(this);
		add(classic);
		extended = new JButton("Extended Edition");
		extended.addActionListener(this);
		add(extended);
		setTitle("Start Options");
		setSize(250, 170);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == classic) 
		{
			dispose();
			new ClassicEditionGUI();
		}
		if(e.getSource() == extended) 
		{
			dispose();
			new ExtendedEditionGUI();
		}
	}
}