import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;

public class Client {
	public Client(String host, int port, String puzzleNumber) {

		try {
			Socket lSocket = new Socket(host, port);
			PrintWriter lOut = new PrintWriter(lSocket.getOutputStream());
			BufferedReader lIn = new BufferedReader(new InputStreamReader(
					lSocket.getInputStream()));

			lOut.println(puzzleNumber);
			lOut.flush();

			char[][] board = readBoard(lIn);

			//printBoard(board);
			//Local l = new Local(board);
			String lMySol="";
			lOut.println(lMySol);
			lOut.flush();

			// read answer from the server
			String lLine = lIn.readLine();

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

	public char[][] readBoard(BufferedReader pStream) throws IOException {
		String lLine = pStream.readLine();

		// read number of rows
		int lNumRows = Integer.parseInt(lLine);

		char[][] board = null;

		// read each row
		for (int y = 1; y <= lNumRows; y++) {
			lLine = pStream.readLine();
			if(board == null)
				board  = new char[lNumRows+1][lLine.length()];
			int x = 0;
			for (char c : lLine.toCharArray()) {
				board[y][x++] = c;
			}
		}
		return board;
	}
	
	private void printBoard(char[][] board){
			for(int j=0; j<board[0].length; j++){
				for(int i=0; i<board.length; i++){
					System.out.print(board[i][j]);
			}
			System.out.println();
		}
			
	}

	
}
