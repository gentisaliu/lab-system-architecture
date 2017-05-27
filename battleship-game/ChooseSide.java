import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class ChooseSide extends JFrame implements ActionListener, Runnable, ItemListener
{
	private JRadioButton serverSide;
	private JRadioButton opponentSide;
	private ButtonGroup buttons;
	private JPanel options;
	private JPanel okCancelButtons;
	private JPanel lists;
	private JButton okButton;
	private JButton cancelButton;
	private JList serverSidePlayers;
	private JList opponentSidePlayers;
	private Controller c;
	private NetDaemon nd;
	private boolean mayRun;
	private String playerName;
	private String side;
	private Game gameData;
	
	public ChooseSide(NetDaemon nd, Game gameData)
	{
		setLayout(new FlowLayout());
		this.playerName = playerName;
		mayRun = true;
		this.nd = nd;
		this.gameData = gameData;
		options = new JPanel();
		options.setLayout(new GridLayout(1, 2));
		serverSide = new JRadioButton("Host Side");
		serverSide.addItemListener(this);
		options.add(serverSide);
		opponentSide = new JRadioButton("Guest Side");
		opponentSide.addItemListener(this);
		options.add(opponentSide);
		buttons = new ButtonGroup();
		buttons.add(serverSide);
		nd.setSideChooser(this);
		buttons.add(opponentSide);
		this.c = c;
		lists = new JPanel();
		lists.setLayout(new FlowLayout());
		serverSidePlayers = new JList(nd.getServerSidePlayers());
		serverSidePlayers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		serverSidePlayers.setLayoutOrientation(JList.VERTICAL_WRAP);
		serverSidePlayers.setVisibleRowCount(-1);
		JScrollPane serverPlayersScroller = new JScrollPane(serverSidePlayers);
		serverPlayersScroller.setPreferredSize(new Dimension(150, 80));
		lists.add(serverPlayersScroller);
		opponentSidePlayers = new JList(nd.getOpponentSidePlayers());
		opponentSidePlayers.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		opponentSidePlayers.setLayoutOrientation(JList.VERTICAL_WRAP);
		opponentSidePlayers.setVisibleRowCount(-1);
		JScrollPane opponentPlayersScroller = new JScrollPane(opponentSidePlayers);
		opponentPlayersScroller.setPreferredSize(new Dimension(150, 80));
		lists.add(opponentPlayersScroller);
		okCancelButtons = new JPanel();
		okCancelButtons.setLayout(new FlowLayout());
		okButton = new JButton("OK");
		okButton.addActionListener(this);
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		okCancelButtons.add(okButton);
		okCancelButtons.add(cancelButton);
		add(options);
		add(lists);
		add(okCancelButtons);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Side Select");
		setSize(350, 220);
		setResizable(true);
		setVisible(true);
	}
	
	public void run()
	{
		while(mayRun)
		{
			try
			{
				Thread.sleep(1000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			serverSidePlayers.setListData(nd.getServerSidePlayers());
			serverSidePlayers.repaint();
			opponentSidePlayers.setListData(nd.getOpponentSidePlayers());
			opponentSidePlayers.repaint();
		}
	}
	
	public void playerNotAdded()
	{
		okButton.setEnabled(false);
		JOptionPane.showMessageDialog(this, "Player was not added. Side is full.", "Player not added", JOptionPane.ERROR_MESSAGE);
	}
	
	public void playerAdded()
	{
		serverSide.setEnabled(false);
		gameData.setPlayerSide(side);
		opponentSide.setEnabled(false);
		okButton.setEnabled(true);
	}
	
	public void itemStateChanged(ItemEvent e)
	{
		if(serverSide.isSelected())
		{
			side = "host";
			nd.addServerSidePlayers(gameData.getPlayerName());
		}
		if(opponentSide.isSelected())
		{
			side = "guest";
			nd.addOpponentSidePlayers(gameData.getPlayerName());
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == okButton) 
		{
			dispose();
			MainWindow m = new MainWindow(gameData, nd);
			new Thread(m).start();
			mayRun = false;
		}
		if(e.getSource() == cancelButton) System.exit(0);
	}
}