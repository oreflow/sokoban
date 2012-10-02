import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * TODO Put here a description of what this class does.
 *
 * @author tim.
 *         Created Oct 2, 2012.
 */
public class Local {

	/**
	 * TODO Put here a description of what this constructor does.
	 *
	 * @param pPuzzleNumber
	 */
	public Local(int pPuzzleNumber) {
		BufferedReader lIn;
		try {
			lIn = new BufferedReader(new FileReader("all.slc"));
			Solver s = new Solver(readBoard(lIn, pPuzzleNumber));
			System.out.println(s.solve());

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	/**
	 * TODO Put here a description of what this method does.
	 *
	 * @param pArgs
	 */
	public static void main(String[] pArgs) {
		if (pArgs.length < 1) {
			System.out.println("usage: java Local Boardnumber");
			return;
		}
		new Local(Integer.parseInt(pArgs[0]));
	}



	/**
	 * TODO Put here a description of what this method does.
	 *
	 * @param pStream
	 * @param pPuzzleNumber
	 * @return list of strings representing the board
	 * @throws IOException
	 */
	public ArrayList<String> readBoard(BufferedReader pStream, int pPuzzleNumber) throws IOException {
		
		String lLine = pStream.readLine();
		ArrayList<String> boardArray = new ArrayList<String>();

		while(!lLine.equals(";LEVEL " + pPuzzleNumber)){
			lLine = pStream.readLine();
		}
		
		do {
			lLine = pStream.readLine();
			boardArray.add(lLine);
		} while (lLine.charAt(0) != ';');
		
		return boardArray;
	}
	
	
}
