import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * This server is the main method for creating the Server side of this 
 * program. This class is responsible for the GUI and it also listens
 * on the given port. If someone connects the server, this class forwards the 
 * clients to the ServerThread class.
 */
public class Server extends JFrame {
	
	/**
	 * Serial VersionUID. 
	 */
	private static final long serialVersionUID = 773220890763045944L;
	
	/*
	 * Declaring the GUI variables.
	 */
	private JPanel top, center;
	private JLabel portName;
	private JTextField port;
	private JTextArea chat, logs;
	private JButton startButton, stopButton;
	
	/*
	 * Server related variable declarations.
	 */
	private boolean listening ;
	private int portNumber = 1500; 
	private ServerSocket serverSocket;
	private ChatProtocol chatProtocol;
	
	/*
	 * Default constructor.
	 */
	public Server() {
		super("Server");
		initialize();
		chatProtocol = new ChatProtocol();
	}
	
	/*
	 * initializing the GUI properties.
	 */
	private void initialize() {
		
		/*
		 * Top side of the main frame.
		 */
		top = new JPanel(new GridLayout(1, 4));
		portName = new JLabel("Port: ");
		top.add(portName);
		port = new JTextField("1500");
		top.add(port);
		startButton = new JButton("Start");
		top.add(startButton);
		stopButton = new JButton("Stop");
		stopButton.setEnabled(false);
		top.add(stopButton);
		setButtonProperties();
		add(top, BorderLayout.NORTH);
		
		/*
		 * Center of the main frame.
		 */
		center = new JPanel(new GridLayout(2, 1));
		chat = new JTextArea(50, 50);
		chat.append("Chat Room");
		center.add(new JScrollPane(chat));
		logs = new JTextArea(50, 50);
		logs.append("Logs");
		center.add(new JScrollPane(logs));
		add(center, BorderLayout.CENTER);
		
		/*
		 * Set properties of the main frame.
		 */
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400,500);
		setVisible(true);
		
	}
	
	/*
	 * All button properties written in this method.
	 */
	private void setButtonProperties() {
		
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listening = true;
				StartServer startServer = new StartServer();
				startServer.start();
			}
		});
		
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {	
				try {
					listening = false;
					serverSocket.close();
					chatProtocol.clean();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
			}
		});
		
	}
	
	/*
	 * Server thread which listens a port, accepts the communication and 
	 * creates ServerThread object for each client.
	 */
	class StartServer extends Thread{
		@Override
		public void run() {
			portNumber = Integer.parseInt(port.getText());
			try{
				serverSocket = new ServerSocket(portNumber);
				logs.append("\n> Server started to listen on the port " + portNumber);
				stopButton.setEnabled(true);
				startButton.setEnabled(false);
				
				while(listening) {
					new ServerThread(getServer(), serverSocket.accept(), chatProtocol).start();
				}
				logs.append("\nServer Stopped.");
			} catch(IOException e) { 
				stopButton.setEnabled(false);
				startButton.setEnabled(true);
				System.err.println("Server stopped on port " + portNumber);
				logs.append("\n> Could not listen on port " + portNumber);
			}
		}
	}
	
	/*
	 * Main method.
	 */
	public static void main(String[] args) {
		new Server();
	}
	
	/*
	 * Encapsulated variables.
	 */
	private Server getServer() {
		return this;
	}
	protected JTextArea getChat() {
		return chat;
	}
	
}
