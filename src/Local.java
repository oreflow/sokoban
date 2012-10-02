import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Local {
	int xMax = 0,yMax = 0;
	HashSet<Coords> startBoxSet;
	HashSet<Coords> startGoalSet;
	Coords playerStart;
	Board b;
	
	class Tuple{
		GameState gs;
		int costlimit;
		public Tuple(GameState g,int cl){
			gs = g;
			costlimit = cl;
		}
	}
	public Local(int pPuzzleNumber) {
		BufferedReader lIn;
		try {
			startBoxSet = new HashSet<Coords>();
			startGoalSet = new HashSet<Coords>();
			lIn = new BufferedReader(new FileReader("all.slc"));
			char[][] board = readBoard(lIn, pPuzzleNumber);
			
			b = new Board(xMax,yMax,buildBoard(board),startGoalSet);
			System.out.println(startBoxSet);
			b.printBoard(startBoxSet,playerStart);
			GameState gs = new GameState(null,startBoxSet);
			Tuple t = IDA(gs);
			
			//System.out.println(solver(b,startBoxSet,startGoalSet, playerStart));
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	public Local(char [] [] board) {


			yMax = board.length-1;
			
			for(int i=0;i<yMax;i++){
				if (board[i].length > xMax)
					xMax = board[i].length;
			}
			
			boxSet = new HashSet<Integer>();
			goalSet = new HashSet<Integer>();
			g = new Graph(buildGraph(board),xMax,yMax);
			g.setGoal(goalSet);
			gs = new GameState(g, boxSet, playerStart);
			g.addNeighbours();
			
			Path p;
			//HashSet<Integer> hs = new HashSet<Integer>();
			//p = g.playerBFS(2,2,1,6,a);
			//System.out.println(p);
			//System.out.println();
			
			

			
			//p = g.boxBFS(gs.pPos,'.',gs.boxes);
			//System.out.println(p);
			//System.out.println(gs.pushBox(p));

	}

*/


	public static void main(String[] pArgs) {
		if (pArgs.length < 1) {
			System.out.println("usage: java Local Boardnumber");
			return;
		}
		Local client = new Local(Integer.parseInt(pArgs[0]));
	}
	public boolean finished(HashSet<Coords> boxes){
		for(Coords c : boxes){
			if(!startGoalSet.contains(c)){
				return false;
			}
		}
		return true;
	}
	public Tuple IDA(GameState gs){
		int cost_limit=heuristik(gs);
		while (true){
		Tuple s = depth_limited_search(0,gs,cost_limit);
				if (s != null)
					return s;
				if(cost_limit == 1000)
					return null;
		}
	}
	public Tuple depth_limited_search(int start_cost, GameState path_so_far,int cost_limit){
		int new_cost_limit = Integer.MAX_VALUE, next_cost_limit;
		GameState current = path_so_far;
		int minimum_cost = start_cost + heuristik(current);
		if(minimum_cost>cost_limit){
			return new Tuple(null,minimum_cost);
		}
		if(finished(current.boxes)){
			return new Tuple(path_so_far,cost_limit);
		}
		next_cost_limit = Integer.MAX_VALUE;
		LinkedList<HashSet<Coords>> list = neighbourStates(current);
		for(HashSet<Coords> g : list){
		int new_start_cost  = start_cost + 1;
		Tuple gs = depth_limited_search(new_start_cost,new GameState(path_so_far, g), cost_limit);
		GameState tmp1 = gs.gs;
		new_cost_limit = gs.costlimit;
				if(tmp1 != null){
					return gs;
				}
		}
		next_cost_limit = Math.min(next_cost_limit, new_cost_limit);
				 
		return new Tuple(null,next_cost_limit);
		
	}
	
	public int heuristik(GameState gs){
		int sum = 0;
		for(Coords c : gs.boxes){
			sum += b.boxBFS(c,gs.boxes).length();
		}
		return sum;
	}
	public LinkedList<HashSet<Coords>> neighbourStates(GameState gs){
		LinkedList<HashSet<Coords>> l = new LinkedList<HashSet<Coords>>();
		for(Coords c : gs.boxes){
			for(char k : b.canBeMoved(c,gs.boxes).toCharArray()){
				HashSet<Coords> tmp =new HashSet<Coords>();
				for(Coords c2 : gs.boxes){
					if(c2 != c){
						tmp.add(c2);
					}
				}
				if(k == 'R'){
				tmp.add(c.R());
				}
				else if(k == 'L'){
					tmp.add(c.L());
					}
				else if(k == 'U'){
					tmp.add(c.U());
					}
				else if(k == 'D'){
					tmp.add(c.D());
					}
				l.add(tmp);
				
			}
		}
		return l;
		
	}
	
	/*
	  algorithm IDA*(start):
    cost_limit = h(start)
    loop:
        solution, cost_limit = depth_limited_search(0, [start], cost_limit)
        if solution != None:
            return solution
        if cost_limit == ∞:
            return None
 
# returns (solution-sequence or None, new cost limit)
algorithm depth_limited_search(start_cost, path_so_far, cost_limit):
    node = last_element(path)
    minimum_cost = start_cost + h(node)
    if minimum_cost > cost_limit:
        return None, minimum_cost
    if is_goal(node):
        return path_so_far, cost_limit
 
    next_cost_limit = ∞
    for s in successors(node):
        new_start_cost = start_cost + edge_cost(node, s)
        solution, new_cost_limit = depth_limited_search(new_start_cost, extend(path_so_far, s), cost_limit)
        if solution != None:
            return solution, new_cost_limit
        next_cost_limit = min(next_cost_limit, new_cost_limit)
 
    return None, next_cost_limit
	 */

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
					this.startBoxSet.add(new Coords(x,y));
				}
				if(goalCharSet.contains(board[x][y])) {
					this.startGoalSet.add(new Coords(x,y));
				}
				
				if(board[x][y] == '@' || board[x][y] == '+' ) {
					this.playerStart = new Coords(x,y);
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
