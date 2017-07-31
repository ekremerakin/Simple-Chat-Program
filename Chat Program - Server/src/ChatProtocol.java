import java.io.PrintWriter;
import java.util.ArrayList;

/*
 * This class responsible for sending the input to all the 
 * connected clients. 
 * 
 * Class contains all the PrintListener objects that were created
 * in ServerThread class for each client connected to the server.
 * While using that objects, programs sends the input to all the 
 * active clients. 
 */
public class ChatProtocol {

	private ArrayList<PrintWriter> outList = new ArrayList<>();
	
	protected void processOutput(String inputLine) {
		for(PrintWriter out:outList) {
			if(out!=null)
				out.println(inputLine);
		}
	}
	
	protected void addOut(PrintWriter out) {
		outList.add(out);
	}
	
	protected void clean() {
		outList.removeAll(outList);
	}
	
}
