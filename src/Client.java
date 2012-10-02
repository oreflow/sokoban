import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * TODO Put here a description of what this class does.
 *
 * @author Brute Force.
 *         Created Oct 2, 2012.
 */
public class Client {
	/**
	 * TODO Put here a description of what this constructor does.
	 *
	 * @param host
	 * @param port
	 * @param puzzleNumber
	 */
	public Client(String host, int port, String puzzleNumber) {

		try {
			Socket lSocket = new Socket(host, port);
			PrintWriter lOut = new PrintWriter(lSocket.getOutputStream());
			BufferedReader lIn = new BufferedReader(new InputStreamReader(
					lSocket.getInputStream()));

			lOut.println(puzzleNumber);
			lOut.flush();

			ArrayList<String> list = new ArrayList<String>();
			System.out.println();
			String lLine = lIn.readLine();
			while((lLine = lIn.readLine()) != null)
				
			Solver solver = new Solver(list);
			System.out.println(s.solve());
			
			String lLine = pStream.readLine();

			//printBoard(board);
			//Local l = new Local(board);
			String lMySol="";
			lOut.println(lMySol);
			lOut.flush();

			// read answer from the server
			lLine = lIn.readLine();

			System.out.println(lLine);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	

	public static void main(String[] pArgs) {
		if (pArgs.length < 3) {
			System.out.println("usage: java Client host port boardnum");
			return;
		}
		
		Client client = new Client(pArgs[0], Integer.parseInt(pArgs[1]), pArgs[2]);
	}

	

	
}
