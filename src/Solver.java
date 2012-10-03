import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
		
		return "";
	}
	
	private void initialize(ArrayList<String> list){
		
		int xSize = 0;
		int ySize = list.size();
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
	
	/**
	 * Returns a set of states that are reachable from a selected state.
	 * 
	 * @param origin
	 * @return
	 */
	public Set<State> getNeighbours(State origin) {
		// TODO Implement
		return null;
	}

	/**
	 * Measures the distance from a state to the goal state.
	 * 
	 * @param origin
	 * @return
	 */
	public int distanceToGoal(State origin) {
		Board board = origin.getBoard();
		int total = 0;
		for (Coord box : origin.getBoxes()) {
			total += board.board[box.y][box.x];
		}
		return total;
	}

	/**
	 * An IDA* Search from a state to its goal.
	 * 
	 * @param origin
	 * @return The list of states that will reach the goal.
	 */
	@SuppressWarnings("unchecked")
	public List<State> frIDAStarSearch(State origin) {

		boolean keepLooping = true;
		List<State> goalPath = new ArrayList<State>();
		goalPath.add(origin);
		int costLimit = distanceToGoal(origin);

		while (keepLooping) {
			Object[] result = idaStarHelp(0, goalPath, costLimit);

			// Search failed, returns null.
			if ((Integer) result[1] == Integer.MAX_VALUE) {
				return null;
			}

			// Search succeeded, returns the solution
			if (result[0] != null)
				return (List<State>) result[0];
		}
		return null;
	}

	/**
	 * Help method of IDA*. Will perform a search from the last element in a
	 * path to its goal, provided a starting cost and an upper bound of the cost.	 
	 *  
	 * @return Array of {Solution (List<State>), Cost limit (Integer)}
	 */
	private Object[] idaStarHelp(int startCost, List<State> goalPath, int costLimit) {
		State currentState = goalPath.get(goalPath.size() - 1);
		int minCost = startCost + distanceToGoal(currentState);

		// Search exceeded the limit.
		if (minCost > costLimit) {
			// Good java practice.
			return new Object[] { null, minCost };
		}

		// Search finalized!
		if (distanceToGoal(currentState) == 0)
			return new Object[] { goalPath, costLimit };

		// Keep searching...
		int nextCostLimit = Integer.MAX_VALUE;
		Set<State> neighbours = getNeighbours(currentState);
		for (State state : neighbours) {
			int newStartCost = startCost + 1;
			List<State> newGoalPath = new ArrayList<State>();
			newGoalPath.addAll(goalPath);
			Object[] result = idaStarHelp(newStartCost, newGoalPath, costLimit);

			// Search finalized!
			if (result[0] != null) {
				return result;
			}

			nextCostLimit = Math.min(nextCostLimit, (Integer) result[1]);
		}

		// Branch failed.
		return new Object[] { null, nextCostLimit };

	}
}
