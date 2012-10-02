import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Local {
	int xMax = 0,yMax = 0;
	HashSet<Coord> startBoxSet;
	HashSet<Coord> startGoalSet;
	Coord playerStart;
	Board b;
	
	class Tuple{
		State gs;
		int costlimit;
		public Tuple(State g,int cl){
			gs = g;
			costlimit = cl;
		}
	}
	public Local(int pPuzzleNumber) {
		BufferedReader lIn;
		try {
			startBoxSet = new HashSet<Coord>();
			startGoalSet = new HashSet<Coord>();
			lIn = new BufferedReader(new FileReader("all.slc"));
			char[][] board = readBoard(lIn, pPuzzleNumber);
			
			b = new Board(xMax,yMax,buildBoard(board),startGoalSet);
			System.out.println(startBoxSet);
			b.printBoard(startBoxSet,playerStart);
			State gs = new State(null,startBoxSet);
			Tuple t = IDA(gs);
			
			//System.out.println(solver(b,startBoxSet,startGoalSet, playerStart));
			
			
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
	public char[][] readBoard(BufferedReader pStream, int pPuzzleNumber) throws IOException {
		
		String lLine = pStream.readLine();

		ArrayList<String> BoardArray = new ArrayList<String>();

		int maxLen = 0;
		
		while(!lLine.equals(";LEVEL " + pPuzzleNumber)){
			lLine = pStream.readLine();
		}
		do {
			lLine = pStream.readLine();
			BoardArray.add(lLine);
			maxLen = Math.max(maxLen, lLine.length());
		} while (lLine.charAt(0) != ';');

		char[][] board = new char[maxLen][BoardArray.size()-1];
		xMax = maxLen;
		yMax = BoardArray.size()-1;

		System.out.println(xMax + "  " + yMax);
		// read each row
		for (int y = 0; y < yMax; y++) {
			lLine = BoardArray.get(y);
			char [] c = lLine.toCharArray();
			for(int x=0;x <xMax;x++){
				if(x<c.length)
					board[x][y] = c[x];
				else
					board[x][y] = ' ';
					
			}
		}
		return board;
	}
/*
 * prints a board
 */
	private void printBoard(char[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j]);
			}
			System.out.println();
		}

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
