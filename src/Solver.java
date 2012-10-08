import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class plays the game
 * 
 * @author Brute Force. Created Oct 2, 2012.
 */
public class Solver {

	public static final long TIMEOUT = 60000;
	public static long starttime;

	public static Map<State, List<State>> stateCache;

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

	Coord playerOrigin;

	Coord playerPos;

	// public static void main(String[] args) {
	// Solver solver = new Solver(null);
	// solver.testState();
	// }

	/**
	 * Initializes the variables
	 * 
	 * @param input
	 *            String representation of the starting board
	 */
	public Solver(ArrayList<String> input) {
		starttime = System.currentTimeMillis();
		initialize(input, true);
	}

	public void testPlayerMovement() {

		ArrayList<String> arrBoard = createArrBoard();

		initialize(arrBoard, false);

		ArrayList<State> states = createTestStates();

		String solution = playerMovements(states);
		System.out.println("Solution: " + solution);
	}

	public ArrayList<String> createArrBoard() {
		ArrayList<String> arrBoard = new ArrayList<String>();
		arrBoard.add("#########");
		arrBoard.add("# @     #");
		arrBoard.add("#   ..  #");
		arrBoard.add("#  $$   #");
		arrBoard.add("#       #");
		arrBoard.add("#########");
		return arrBoard;
	}

	public void testState() {
		ArrayList<String> arrBoard = createArrBoard();
		initialize(arrBoard, true);

		System.out.println("Start: ");
		start.printState();
		System.out.println("Goal: ");
		goal.printState();

		List<State> states = frIDAStarSearch(start, goal);
		System.out.println("States in solution: ");
		for (State s : states) {
			s.printState();
		}
		List<State> reverseStates = new LinkedList<State>();
		for (State s : states) {
			reverseStates.add(0, s);
		}
		String solution = playerMovements(reverseStates);
		System.out.println(solution);
	}

	private List<State> reverse(List<State> input) {
		LinkedList<State> output = new LinkedList<State>();
		for (State o : input) {
			output.addFirst(o);
		}
		return output;
	}

	/**
	 * The method that does it all
	 * 
	 * @return String representation of the players' movements to complete the
	 *         game
	 */
	public String solve() {
		System.out.println("Starting state:");
		start.printState();
		System.out.println("Goal state:");
		goal.printState();
		
		List<State> backwardSolution = frIDAStarSearch(start, goal);
		if (backwardSolution == null) {
			return "";
		}
		State temp = backwardSolution.remove(backwardSolution.size() - 1);
		backwardSolution.add(new State(playerOrigin, temp.boxes, temp.board));
		System.out.println("Backwards: ");
		for (State s : backwardSolution) {
			s.printState();
		}
		List<State> solution = reverse(backwardSolution);
		return playerMovements(solution);
	}

	private void initialize(ArrayList<String> list, boolean reverse) {

		stateCache = new HashMap<State, List<State>>();
		int xSize = 0;
		int ySize = list.size();
		for (String str : list) {
			if (str.length() > xSize)
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
		ArrayList<Character> goalCharSet = new ArrayList<Character>() {
			{
				add('.');
				add('*');
				add('+');
			}
		};

		Set<Coord> startBoxes = new HashSet<Coord>();
		Set<Coord> goalBoxes = new HashSet<Coord>();
		Set<Coord> goalSet = new HashSet<Coord>();
		Coord player = null;

		byte[][] b = new byte[ySize][xSize];

		for (int y = 0; y < ySize; y++) {
			String row = list.get(y);
			for (int x = 0; x < xSize; x++) {
				char c = ' ';
				if (x < row.length())
					c = row.charAt(x);

				if (blockCharSet.contains(c)) {
					startBoxes.add(new Coord(y, x));
				}
				if (goalCharSet.contains(c)) {
					goalSet.add(new Coord(y, x));
					goalBoxes.add(new Coord(y, x));
				}
				if (c == '@' || c == '+') {
					player = new Coord(y, x);
				}

				if (walkableCharSet.contains(c)) {
					b[y][x] = Byte.MAX_VALUE;
				} else if (c == '#') {
					b[y][x] = -1;
				}
			}
		}

		if (!reverse) {
			this.board = new Board(b, goalSet, player);
			this.start = new State(player, startBoxes, board);
			this.goal = new State(player, goalSet, board);
		} else {
			this.board = new Board(b, startBoxes, player);
			this.start = new State(null, goalSet, board);
			this.goal = new State(board.lowestReachable(player, goalSet), startBoxes, board);
		}
		playerOrigin = player;
	}

	/**
	 * Returns a set of states that are reachable from a selected state.
	 * 
	 * @param origin
	 * @return
	 */
	public List<State> getNeighbours(State origin) {
		return origin.getNeighbours();
	}

	private State getLowest(Collection<State> states, Map<State, Integer> values) {
		int lowest = Integer.MAX_VALUE;
		State low = null;
		for (State s : states) {
			int value = values.get(s);
			if (value < lowest) {
				lowest = value;
				low = s;
			}
		}
		return low;
	}

	/**
	 * An A* Search from a state to its goal.
	 */
	public List<State> aStarSearch(State origin, State goal) {
		Set<State> visited = new HashSet<State>();
		Collection<State> openSet = new HashSet<State>();
		openSet.add(origin);
		Map<State, State> parents = new HashMap<State, State>();

		// Cost from start along best known path.
		Map<State, Integer> gScore = new HashMap<State, Integer>();
		gScore.put(origin, 0);

		Map<State, Integer> fScore = new HashMap<State, Integer>();

		// Estimated total cost from start to goal through y.
		fScore.put(origin, gScore.get(origin) + origin.distance);

		while (!openSet.isEmpty()) {
			if (System.currentTimeMillis() - TIMEOUT > starttime) {
				return null;
			}
			State current = getLowest(openSet, fScore);

			// Reached our goal.
			if (current.distance == 0)
				return reconstructPath(parents, current);

			openSet.remove(current);
			visited.add(current);
			List<State> neighbours = getNeighbours(current);
			for (State neighbour : neighbours) {
				if (visited.contains(neighbour)) {
					continue;
				}

				int tentativeGScore = gScore.get(current) + 1;

				if (!openSet.contains(neighbour) || tentativeGScore < gScore.get(neighbour)) {
					parents.put(neighbour, current);
					gScore.put(neighbour, tentativeGScore);
					fScore.put(neighbour, tentativeGScore + neighbour.distance);
					openSet.add(neighbour);
				}
			}
		}
		return null;
	}

	/**
	 * Constructs a path from this stuff. Very good indeed. Already backwards.
	 * 
	 * @param parents
	 * @param finalState
	 * @return
	 */
	private List<State> reconstructPath(Map<State, State> parents, State finalState) {
		List<State> path = new LinkedList<State>();
		while (finalState != null) {
			path.add(finalState);
			finalState = parents.get(finalState);
		}
		return path;
	}

	/**
	 * An IDA* Search from a state to its goal.
	 * 
	 * @param origin
	 * @return The list of states that will reach the goal.
	 */
	@SuppressWarnings("unchecked")
	public List<State> frIDAStarSearch(State origin, State goal) {

		boolean keepLooping = true;
		List<State> goalPath = new ArrayList<State>();
		goalPath.add(origin);
		int costLimit = origin.distance;
		int oldCost = costLimit - 1;

		while (keepLooping) {
			if (oldCost < costLimit) {
				System.out.println("Searching with depth " + costLimit);
				oldCost = costLimit;
			}
			Set<State> visited = new HashSet<State>();
			visited.add(origin);
			Object[] result = depthLimitedSearch(0, goalPath, goal, costLimit, visited);

			List<State> solution = (List<State>) result[0];
			costLimit = (Integer) result[1];

			// Search failed, returns null.
			if (costLimit == Integer.MAX_VALUE) {
				return null;
			}

			// Search succeeded, returns the solution
			if (solution != null)
				return solution;

			// costLimit++;

		}
		return null;
	}

	/**
	 * Help method of IDA*. Will perform a search from the last element in a
	 * path to its goal, provided a starting cost and an upper bound of the
	 * cost.
	 * 
	 * @return Array of {Solution (List<State>), Cost limit (Integer)}
	 */
	private Object[] depthLimitedSearch(int startCost, List<State> goalPath, State goal, int costLimit, Set<State> visited) {
		// Exit on timeout
		if (System.currentTimeMillis() - TIMEOUT > starttime) {
			return new Object[] { null, Integer.MAX_VALUE };
		}

		// DEBUG
		// System.out.println("IDA* Help, limit " + costLimit + ", visited " +
		// visited.size());
		State currentState = goalPath.get(goalPath.size() - 1);
		visited.add(currentState);
		int minCost = startCost + currentState.distance;

		// Search exceeded the limit.
		if (minCost > costLimit) {
			// Good java practice.
			// System.out.println("Limit exceeded");
			return new Object[] { null, minCost };
		}

		// Search finalized!
		if (currentState.distance == 0)
			return new Object[] { goalPath, costLimit };

		// Keep searching...
		int nextCostLimit = Integer.MAX_VALUE;
		List<State> neighbours = getNeighbours(currentState);
		// System.out.println("Looking in state:");
		// currentState.printState();
		for (State state : neighbours) {
			if (visited.contains(state)) {
				// System.out.println("Visited already.");
				continue;
			}
			if(state.hasDeadlock(this.start)){
				visited.add(state);
				state.printState();
				continue;
			}
			// System.out.println("Neighbour: ");
			// state.printState();
			int newStartCost = startCost + 1;
			List<State> newGoalPath = new ArrayList<State>();
			newGoalPath.addAll(goalPath);
			newGoalPath.add(state);
			Object[] result = depthLimitedSearch(newStartCost, newGoalPath, goal, costLimit, visited);

			// Search finalized!
			if (result[0] != null) {
				return result;
			}

			nextCostLimit = Math.min(nextCostLimit, (Integer) result[1]);
		}

		// Branch failed.
		return new Object[] { null, nextCostLimit };

	}

	// Creates some test states for playerMovements
	ArrayList<State> createTestStates() {

		ArrayList<State> states = new ArrayList<State>();
		Coord player = new Coord(1, 2);
		Coord box = new Coord(3, 3);
		Set<Coord> boxes = new HashSet<Coord>();
		boxes.add(box);
		box = new Coord(2, 6);
		boxes.add(box);
		State state = new State(player, boxes, this.board);
		this.board.printBoard(state);
		states.add(state);

		box = new Coord(4, 3);
		boxes = new HashSet<Coord>();
		boxes.add(box);
		box = new Coord(2, 6);
		boxes.add(box);
		state = new State(player, boxes, this.board);
		this.board.printBoard(state);
		states.add(state);

		player = new Coord(4, 7);
		box = new Coord(4, 3);
		boxes = new HashSet<Coord>();
		boxes.add(box);
		box = new Coord(3, 6);
		boxes.add(box);
		state = new State(player, boxes, this.board);
		this.board.printBoard(state);
		states.add(state);

		return states;
	}

	// Creates string representation of the solution
	public static String createSolStr(Coord goal, Map<Coord, Coord> parents) {
		String solTmp = "";
		Coord coord = goal;
		Coord parent;
		while (parents.get(coord) != null) {
			parent = parents.get(coord);

			int x = coord.x;
			int y = coord.y;
			int x2 = parent.x;
			int y2 = parent.y;

			if (x > x2) {// right
				solTmp += "R";
			} else if ((x < x2)) {// left
				solTmp += "L";
			} else if (y > y2) {// down
				solTmp += "D";
			} else if (y < y2) {// up
				solTmp += "U";
			}
			coord = parent;
		}
		String sol = new StringBuffer(solTmp).reverse().toString();
		return sol;
	}

	// Gets a list of states and calculates the movement of the player between
	// these
	// Returns the string representation of the solution
	String playerMovements(List<State> states) {

		Coord boxBef = null;
		Coord boxAft = null;
		playerPos = null;
		Coord playerPosAft = null;
		String solution = "";

		for (int i = 0; i < states.size() - 1; i++) {
			if (i == 0) { // First state, get players position
				playerPos = states.get(i).player;
			}

			Set<Coord> boxes1 = states.get(i).boxes;
			Set<Coord> boxes2 = states.get(i + 1).boxes;

			// Find out which box has moved between state i and i+1
			Iterator<Coord> it = boxes1.iterator();
			Coord box1 = null;
			while (it.hasNext()) {
				box1 = (Coord) it.next();
				if (!boxes2.contains(box1)) {
					boxBef = box1;
				}

			}
			it = boxes2.iterator();
			Coord box2 = null;
			while (it.hasNext()) {
				box2 = (Coord) it.next();
				if (!boxes1.contains(box2)) {
					boxAft = box2;
				}

			}

			String part = null;
			if (boxBef.relU().y == boxAft.y) { // Box goes up
				playerPosAft = boxBef.relD();
				part = "U";
			} else if (boxBef.relD().y == boxAft.y) { // Box goes down
				playerPosAft = boxBef.relU();
				part = "D";
			} else if (boxBef.relL().x == boxAft.x) { // Box goes left
				playerPosAft = boxBef.relR();
				part = "L";
			} else if (boxBef.relR().x == boxAft.x) { // Box goes right
				playerPosAft = boxBef.relL();
				part = "R";
			}

			// do bfs
			String solutionPart = bfs(playerPos, playerPosAft, states.get(i));
			solution = solution + solutionPart + part;
			playerPos = boxBef;

		}

		// Last state, make player go to that states player position
		String solutionPart = bfs(playerPos, states.get(states.size() - 1).player, states.get(states.size() - 1));
		solution = solution + solutionPart;
		return solution.replace("null", "");
	}

	// Makes an bfs of the players movement between two states
	public String bfs(Coord start, Coord goal, State state) {
		LinkedList<Coord> qu = new LinkedList<Coord>();
		Map<Coord, Coord> parents = new HashMap<Coord, Coord>();
		parents.put(start, null);
		qu.add(start);
		HashSet<Coord> visited = new HashSet<Coord>();
		visited.add(start);
		Set<Coord> boxes = state.boxes;

		while (!qu.isEmpty()) {
			Coord head = qu.poll();
			if (head.equals(goal)) {
				// System.out.println("found goal");
				return createSolStr(head, parents);
			}
			Coord[] neighs = head.getNeighbors();
			for (int j = 0; j < neighs.length; j++) {
				Coord neighbour = neighs[j];
				if (!boxes.contains(neighbour) && this.board.board[neighbour.y][neighbour.x] != -1) {
					if (!visited.contains(neighbour)) {
						visited.add(neighbour);
						parents.put(neighbour, head);
						qu.add(neighbour);
					}
				}
			}

		}
		return null;
	}

	
}
