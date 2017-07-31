import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * This class is responsible for creating the 
 * I/O variables and make the connecting available
 * until client wants to logout.
 */
public class ServerThread extends Thread{

	/*
	 * Variable declarations for creating server.
	 */
	private Socket socket = null;
	private Server server = null;
	private boolean connection = true;
	private ChatProtocol chatProtocol;
	
	/*
	 * Constructor for this class. 
	 * Takes the server, socket and chatProtocol objects 
	 * from the Server class. 
	 */
	public ServerThread(Server server, Socket socket, ChatProtocol chatProtocol) {
		this.server = server;
		this.socket = socket;
		this.chatProtocol = chatProtocol;
	}
	
	/*
	 * Run method for this thread. Taking input from the client every time
	 * the loop turns.
	 */
	@Override
	public void run() {
		
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			chatProtocol.addOut(out);
			
			Boolean exitCheck = null;
			String inputLine;
			while(connection) {
				while((inputLine = in.readLine())!=null) {
					exitCheck = inputLine.substring(inputLine.length()-4, inputLine.length()).equals("exit");
					if(inputLine.equals(""))
						break;
					if(exitCheck)
						break;
					server.getChat().append("\n> " + inputLine);
					chatProtocol.processOutput(inputLine);
				}
				if(exitCheck)
					connection = false;
			}
			out.println("Disconnected from the server.");
			socket.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
