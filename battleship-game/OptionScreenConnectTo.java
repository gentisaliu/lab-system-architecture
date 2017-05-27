import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class OptionScreenConnectTo extends JFrame implements ActionListener
{
	private JTextField playerName;
	private JTextField ipTextField;
	private JTextField portTextField;
	private JLabel pName;
	private JLabel enterIp;
	private JLabel enterPort;
	private JButton okButton;
	private JButton cancelButton;
	private Container windowContainer;
	private JPanel userInput;
	private JPanel buttons;
	private NetDaemon nd;
	
	public OptionScreenConnectTo()
	{
		setLayout(new GridLayout(2, 1));
		userInput = new JPanel();
		userInput.setLayout(new FlowLayout());
		pName = new JLabel("Player name:");
		userInput.add(pName);
		playerName = new JTextField();
		playerName.setPreferredSize(new Dimension(150, 20));
		userInput.add(playerName);
		enterIp = new JLabel("IP Address:   ");
		userInput.add(enterIp);
		ipTextField = new JTextField();
		ipTextField.setPreferredSize(new Dimension(150, 20));
		userInput.add(ipTextField);
		enterPort = new JLabel("Port Number:");
		userInput.add(enterPort);
		portTextField = new JTextField("1111");
		portTextField.setPreferredSize(new Dimension(150, 20));
		userInput.add(portTextField);
		buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
		buttons.add(Box.createRigidArea(new Dimension(40, 0)));
		okButton = new JButton("Connect");
		okButton.addActionListener(this);
		buttons.add(okButton);
		buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		buttons.add(cancelButton);
		add(userInput);
		add(buttons);
		setTitle("Connect to computer...");
		setSize(250, 175);
		setResizable(false);
		setVisible(true);
		WindowQuit wq = new WindowQuit();
		addWindowListener(wq);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == okButton) 
		{
			ConnectionInProgress c = new ConnectionInProgress(this, ipTextField.getText(), Integer.parseInt(portTextField.getText()), playerName.getText(), false, null, null);
			new Thread(c).run();
		}
		if(e.getSource() == cancelButton)
		{
			System.exit(0);
		}
	}
}

class WindowQuit extends WindowAdapter
{
	public void windowClosing( WindowEvent e )
	{
		System.exit( 0 );
	}
} 
