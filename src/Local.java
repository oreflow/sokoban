import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Local {

	public Local(int pPuzzleNumber) {
		BufferedReader lIn;
		try {
			lIn = new BufferedReader(new FileReader("all.slc"));
			Solver s = new Solver();
			System.out.println(s.solve(readBoard(lIn, pPuzzleNumber)));

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] pArgs) {
		if (pArgs.length < 1) {
			System.out.println("usage: java Local Boardnumber");
			return;
		}
		Local client = new Local(Integer.parseInt(pArgs[0]));
	}


	/*
	 * Reads a board from the file
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

	/*
	 * adds the read board to a Graph object and returns said object
	 */
	public char [] [] buildBoard(char[][] board) {
		// Builds a set of walkable squares
		HashSet<Character> walkableCharSet = new HashSet<Character>() {
			{
				add('@');
				add('+');
				add('.');
				add(' ');
				add('*');
				add('$');
			}
		};
		ArrayList<Character> blockCharSet = new ArrayList<Character>() {
			{
				add('*');
				add('$');
			}
		};
		ArrayList<Character> goalCharSet = new ArrayList<Character>() {{
			add('.');
			add('*');
			add('+');
		}};
		
		char [] [] sboard = new char [xMax] [yMax]; 
		
		
		for (int y = 0; y < this.yMax; y++) {
			for (int x = 0; x < this.xMax; x++) {
				
				// Add block to the block set
				if(blockCharSet.contains(board[x][y])) {
					this.startBoxSet.add(new Coord(x,y));
				}
				if(goalCharSet.contains(board[x][y])) {
					this.startGoalSet.add(new Coord(x,y));
				}
				
				if(board[x][y] == '@' || board[x][y] == '+' ) {
					this.playerStart = new Coord(x,y);
				}
				
				if (walkableCharSet.contains(board[x][y])) {
						sboard[x][y] = ' ';
				}
				else if(board[x][y] == '#'){
					sboard[x][y] = '#';
				}
			}
		}
		return sboard;
	}
	
	
}
