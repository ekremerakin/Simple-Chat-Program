import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/* This class is created to connect a server with a given server name,
 * port number and user ID. After connecting to the server, program
 * would send a message when the client write the message in the message
 * TextField and press the send button.
 */
public class Client extends JFrame{
	
	/**
	 * Serial version IUD.
	 */
	private static final long serialVersionUID = -5943375108781975889L;
	
	/*
	 * Declaring the GUI variables.
	 */
	private JPanel top, center, bottom;
	private JLabel serverName, portName, userName, messageName;
	private JTextField server, port, user, message;
	private JTextArea chat;
	private JButton login, send, logout;
	
	/*
	 * Server related variable declarations.
	 */
	private Socket socket;
	private PrintWriter out = null;
	private boolean connection = false;
	
	/*
	 * Default constructor.
	 */
	public Client() {
		super("Client");
		initialize();
	}
	
	/*
	 * initializing the GUI properties.
	 */
	private void initialize() {
		
		/*
		 * Top side of the main frame.
		 */
		top = new JPanel(new GridLayout(4, 2));
		serverName = new JLabel("Server Name: ");
		top.add(serverName);
		server = new JTextField("127.0.0.1");
		top.add(server);
		portName = new JLabel("Port: ");
		top.add(portName);
		port = new JTextField("1500");
		top.add(port);
		userName = new JLabel("User ID:");
		top.add(userName);
		user = new JTextField("unknown");
		top.add(user);
		messageName = new JLabel("Message:");
		top.add(messageName);
		message = new JTextField("");
		message.setEnabled(false);
		top.add(message);
		
		/*
		 * Center of the main frame.
		 */
		center = new JPanel(new GridLayout(1, 1));
		chat = new JTextArea(50, 50);
		chat.append("Chat Room");
		center.add(new JScrollPane(chat));

		/*
		 * Bottom of the main frame.
		 */
		bottom = new JPanel();
		login = new JButton("Login");
		bottom.add(login);
		send = new JButton("Send");
		send.setEnabled(false);
		bottom.add(send);
		logout = new JButton("Logout");
		logout.setEnabled(false);
		bottom.add(logout);
		
		setButtonProperties();
		add(top, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		
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
		
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				connection = true;
				ConnectServer connectServer = new ConnectServer();
				connectServer.start();
			}
		});
		
		send.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(out != null) {
					String username = user.getText();
					out.println(username + ": "  + message.getText());
				}
				message.setText("");
			}
		});
		
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				server.setEditable(true);
				port.setEditable(true);
				user.setEditable(true);
				message.setEnabled(false);
				login.setEnabled(true);
				send.setEnabled(false);
				logout.setEnabled(false);
				connection = false;
				
				out.println(user.getText() + " is no longer in this room.");
				out.println("exit");
			}
		});
		
	}

	/*
	 * Client thread for taking input from the server constantly. 
	 */
	class ConnectServer extends Thread{
		@Override
		public void run() {
			try {
				socket = new Socket(server.getText(), Integer.parseInt(port.getText()));
				chat.append("\n> Connected to server on port " + port.getText());
				out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.println("New user logged into server.");
				
				server.setEditable(false);
				port.setEditable(false);
				user.setEditable(false);
				message.setEnabled(true);
				login.setEnabled(false);
				send.setEnabled(true);
				logout.setEnabled(true);
				
				String inputLine;
				while(connection) {
					while((inputLine = in.readLine())!=null) {
						chat.append("\n> " + inputLine);
					}
				}
				socket.close();
			} catch (ConnectException e) {
				chat.append("\n> Could not find the server on port " + port.getText());
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Main method.
	 */
	public static void main(String[] args) {
		new Client();
	}
	
}
