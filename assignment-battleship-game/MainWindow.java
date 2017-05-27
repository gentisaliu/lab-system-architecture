import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JTextField;

//--------------------------------------------------
// provide a menu and instanciate a view on demand,
// open options dialog etc
//-------------------------------------------------- 
public class MainWindow extends JFrame implements ActionListener, WindowListener,Runnable
{
	private static final long serialVersionUID = 1L;
	protected JTextField chatLine;
	protected JMenuBar menuBar;
	protected Game game;
	protected NetDaemon nd;
	protected JButton createButton,joinButton;
	public static View v;
	
	public MainWindow(Game g,NetDaemon nd) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		game = g;
		this.nd = nd;
		setLayout(new BorderLayout());
		addWindowListener(this);
		
		v = new View(g,nd);
		add(v,BorderLayout.NORTH);
		chatLine = new JTextField();		
		chatLine.setBackground(Color.darkGray);
		chatLine.setForeground(Color.white);
		chatLine.setEnabled(true);
		add(chatLine, BorderLayout.SOUTH);
		chatLine.addActionListener( this );
		pack();
		setResizable(false);
		setVisible(true);
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource()  == chatLine ) {
			nd.chat("chat "+chatLine.getText());
			chatLine.setText("");
		}
	}

	public void run() {
		v.gameLoop();
	}
	
	public void windowActivated(WindowEvent arg0) {}

	public void windowClosed(WindowEvent arg0) {
		System.exit(0);
	}

	public void windowClosing(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
}
