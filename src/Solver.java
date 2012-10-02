import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * This class plays the game
 *
 * @author Brute Force.
 *         Created Oct 2, 2012.
 */
public class Solver {

	/**
	 * The board that the game is played on
	 */
	Board board;
	/**
	 * State containing the starting positions of the boxes
	 */
	State start;
	/**
	 * State containing the goal positions of the boxes
	 */
	State goal;
	
	/**
	 * Initializes the variables
	 *
	 * @param input String representation of the starting board
	 */
	public Solver(ArrayList<String> input){
		initialize(input);
	}
	/**
	 * The method that does it all
	 *
	 * @return String representation of the players' movements to complete the game
	 */
	public String solve(){
		this.board.printBoard();
		
		return "";
	}
	
	private void initialize(ArrayList<String> list){
		
		int xSize = 0;
		int ySize = list.size()-1;
		for(String str : list){
			if(str.length() > xSize)
				xSize = str.length();
			
		}
			ArrayList<Character> walkableCharSet = new ArrayList<Character>() {
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
			
			Set<Coord> startBoxes = new HashSet<Coord>();
			Set<Coord> goalBoxes = new HashSet<Coord>();
			Set<Coord> goalSet = new HashSet<Coord>();
			Coord player = null;

			byte [] [] b = new byte [ySize] [xSize];
			
			for (int y = 0; y < ySize; y++) {
				String row = list.get(y);
				for (int x = 0; x < xSize; x++) {
					char c = ' ';
					if(x<row.length())
						c = row.charAt(x);
					
					if(blockCharSet.contains(c)){
						startBoxes.add(new Coord(y,x));
					}
					if(goalCharSet.contains(c)) {
						goalSet.add(new Coord(y,x));
						goalBoxes.add(new Coord(y,x));
					}
					if(c == '@' || c == '+' ) {
						player = new Coord(y,x);
					}
					
					if (walkableCharSet.contains(c)) {
							b[y][x] = Byte.MAX_VALUE;
					}
					else if(c == '#'){
						b[y][x] = -1;
					}
				}
			}

		this.board = new Board(b,goalSet);
		this.start = new State(player,startBoxes);
		this.goal = new State(player,goalSet);
		
	}
}
