import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Class that runs the solver against the server
 * 
 * @author Brute Force. Created Oct 2, 2012.
 */
public class Client {
	/**
	 * 
	 * @param host
	 * @param port
	 * @param puzzleNumber
	 */
	public Client(String host, int port, String puzzleNumber) {

		try {
			Socket lSocket = new Socket(host, port);
			PrintWriter lOut = new PrintWriter(lSocket.getOutputStream());
			BufferedReader lIn = new BufferedReader(new InputStreamReader(lSocket.getInputStream()));

			lOut.println(puzzleNumber);
			lOut.flush();

			ArrayList<String> list = new ArrayList<String>();
			// System.out.println();

			String lLine = lIn.readLine();
			System.out.println("Original:");
			System.out.println(lLine);
			int rows = Integer.parseInt(lLine);
			for (int i = 0; i < rows; i++) {
				lLine = lIn.readLine();
				list.add(lLine);
				System.out.println(lLine);
			}

			Solver solver = new Solver(list);
			String solution = solver.solve();

			System.out.println("Solution: " + solution);
			System.out.println("Time: " + (double) (System.currentTimeMillis() - Solver.starttime) / 1000);

			lOut.println(solution);
			lOut.flush();

			// read answer from the server
			lLine = lIn.readLine();

			System.out.println(lLine);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * main
	 * 
	 * @param pArgs
	 */
	public static void main(String[] pArgs) {
		if (pArgs.length < 3) {
			System.out.println("usage: java Client host port boardnum");
			return;
		}

		new Client(pArgs[0], Integer.parseInt(pArgs[1]), pArgs[2]);
	}

}
