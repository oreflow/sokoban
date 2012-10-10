import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Class that represents a state in the game
 * 
 * @author Brute Force. Created Sep 20, 2012.
 */
final public class State implements Comparable<State> {
	/** position of the player in this state */
	final Coord player;
	/** positions of the boxes in this state */
	final Set<Coord> boxes;
	/** pointer to the board this state is in */
	final Board board;

	final int distance;
	
	final int goalCount;

	final Set<Coord> allPlayer;

	public Board getBoard() {
		return board;
	}

	public Coord getPlayer() {
		return player;
	}

	public Set<Coord> getBoxes() {
		return boxes;
	}

	/**
	 * 
	 * @param player
	 *            player position
	 * @param boxes
	 *            the boxes positions
	 * @param board
	 *            used game board
	 */
	public State(Coord player, Set<Coord> boxes, Board board) {
		this.player = player;
		this.boxes = boxes;
		this.board = board;
		this.allPlayer = allAdjacent(player);
		this.distance = distanceToGoal();
		this.goalCount = goalCount();
	}
	
	private int goalCount(){
		int goalCount = 0;
		for(Coord c : boxes){
			if(board.goals.contains(c)){
				goalCount++;
			}
		}
		return goalCount;
	}

	public boolean canReach(Coord c) {
		if (allPlayer.size() > 0) {
			return allPlayer.contains(c);
		}
		return true;
	}

	private Set<Coord> allAdjacent(Coord origin) {
		Set<Coord> visited = new HashSet<Coord>();

		if (origin == null)
			return visited;
		visited.add(origin);
		Queue<Coord> q = new LinkedList<Coord>();
		q.add(origin);
		while (!q.isEmpty()) {
			Coord c = q.poll();
			for (Coord neighbour : c.getNeighbors()) {
				if (!visited.contains(neighbour) && board.isWalkable(neighbour) && !boxes.contains(neighbour)) {
					q.add(neighbour);
					visited.add(neighbour);
				}
			}
		}
		return visited;
	}

	/**
	 * Measures the distance from a state to the goal state.
	 * 
	 * @param origin
	 * @return
	 */
	private int distanceToGoal() {
		int total = 0;
		for (Coord box : boxes) {
			total += board.board[box.y][box.x];
		}
		return total;
	}

	/**
	 * Prints a state to the terminal.
	 */
	public void printState() {
		for (int y = 0; y < board.board.length; y++) {
			for (int x = 0; x < board.board[y].length; x++) {
				Coord c = new Coord(y, x);
				boolean isBox = boxes.contains(c);
				boolean isGoal = board.goals.contains(c);
				boolean isPlayer = c.equals(player);
				if (isBox && isGoal)
					System.out.print("*");
				else if (isBox)
					System.out.print("$");
				else if (isGoal && isPlayer)
					System.out.print("+");
				else if (isGoal)
					System.out.print(".");
				else if (isPlayer)
					System.out.print("@");
				else if (board.board[y][x] < 0)
					System.out.print("#");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}

	/**
	 * Retrieves the neighbours from a state (all states that can be reachable
	 * with one box pull).
	 * 
	 * @return
	 */
	public List<State> getNeighbours() {

		if (Solver.stateCache.containsKey(this)) {
			return Solver.stateCache.get(this);
		}

		List<State> neighbours = new ArrayList<State>();

		Coord[] allDirs = { Coord.LEFT, Coord.UP, Coord.RIGHT, Coord.DOWN };

		// Loops through all the boxes of this state.
		for (Coord box : boxes) {
			Coord[] boxNeighbours = getBoxNeighbours(box);
			for (int i = 0; i < 4; i++) {
				Coord possibleNeighbour = boxNeighbours[i];
				if (possibleNeighbour == null)
					continue;
				Set<Coord> newBoxSet = new HashSet<Coord>();
				newBoxSet.addAll(boxes);
				newBoxSet.remove(box);
				newBoxSet.add(boxNeighbours[i]);
				State s = new State(board.lowestReachable(boxNeighbours[i + 4], newBoxSet), newBoxSet, board);
				if(!Solver.stateCache.containsKey(this))
					neighbours.add(s);
			}
		}

		Solver.stateCache.put(this, neighbours);
		return neighbours;
	}

	/**
	 * Creates a new state, with specified player position and a box that has
	 * moved in a (relative) direction.
	 * 
	 * @param playerPos
	 * @param movingBox
	 * @param direction
	 * @return
	 */
	public State(State s, Coord player, Coord oldBox, Coord relBoxPos) {
		this.boxes = new HashSet<Coord>();
		this.boxes.addAll(s.boxes);
		this.boxes.remove(oldBox);
		this.boxes.add(new Coord(oldBox.y + relBoxPos.y, oldBox.x + relBoxPos.x));
		this.player = player;
		this.board = s.board;
		this.allPlayer = allAdjacent(player);
		this.distance = distanceToGoal();
		this.goalCount = goalCount();
	}

	public Coord[] getBoxNeighbours(Coord box) {
		Coord[] neighbours = new Coord[8];
		Coord[] allDirs = { new Coord(box, Coord.LEFT), new Coord(box, Coord.UP), new Coord(box, Coord.RIGHT), new Coord(box, Coord.DOWN) };
		Coord[] allDirs2 = { new Coord(allDirs[0], Coord.LEFT), new Coord(allDirs[1], Coord.UP), new Coord(allDirs[2], Coord.RIGHT),
				new Coord(allDirs[3], Coord.DOWN) };
		for (int i = 0; i < 4; i++) {
			if (board.isWalkable(allDirs[i]) && board.isWalkable(allDirs2[i]) && !boxes.contains(allDirs[i]) && !boxes.contains(allDirs2[i])
					&& canReach(allDirs[i])) {
				neighbours[i] = allDirs[i];
				neighbours[4 + i] = allDirs2[i];
			}
		}

		return neighbours;
	}

	public int hashCode() {
		int code = 0;
		for (Coord box : boxes) {
			code = code + box.hashCode();
		}
		if (player != null)
			code = code + player.hashCode() << 4;
		return code;
	}

	public boolean equals(Object o) {
		if (!(o instanceof State)) {
			return false;
		}
		State other = (State) o;
		if (player != null && other.player != null && !player.equals(other.player)) {
			return false;
		}
		if (other.boxes.size() != boxes.size())
			return false;
		for (Coord box : boxes) {
			if (!other.boxes.contains(box))
				return false;
		}
		return true;
	}

	/**
	 * @param s
	 *            The state to search for deadlocks
	 * @return if there is any deadlock in the state
	 * 
	 *         to see description of the different deadlocks, see:
	 *         http://www.sokobano.de/wiki/index.php?title=Deadlocks
	 */
	public boolean hasDeadlock(State start) {
		if (isFreezeLocked(start)
				|| // pointless method?
				isCorralLocked() || isBipartiteLocked() || isLockedBecauseOfOtherFrozenBoxesAndIsThereForeLockedThemselvesWhichIsAnnoying()
				|| closedDiagonalLock()) {
			return true;
		}
		return false;
	}

	/**
	 * @param s
	 *            the state to check
	 * @return true if a box in the state is Freeze-locked on a non-goal square
	 *         false if no box in the state is Freeze-locked on a non-goal
	 *         square
	 */
	public boolean isFreezeLocked(State start) {

		for (Coord box : this.boxes) {
			if (this.board.goals.contains(box)) {
				continue; // if the box is on a goal square it is irrelevant to
							// check for this kind of deadlock
			}

			if (isFreezeLocked(box, new HashSet<Coord>(), start)) {
				System.out.println(box);
				return true;
			}

		}
		return false;
	}

	/**
	 * 
	 * Help method for the above, so that it can be used recursively
	 * 
	 * counts on the fact that a box is locked in X-position if 1 or more case
	 * of 3 is true * The box has a wall to the right or left of it * The box
	 * has a frozen box to the left of it * The box has a frozen box to the
	 * right of it and a box is locked in Y-position if 1 or more case of 3 is
	 * true * The box has a wall above or below it * The box has a frozen box
	 * above it * The box has a frozen box below it
	 * 
	 * @param c
	 * @param visited
	 * @return if the box is frozen
	 */
	public boolean isFreezeLocked(Coord c, Set<Coord> visited, State start) {

		if (visited.contains(c)) {
			return true;
		}
		visited.add(c); // adds itself to the list of visited boxes to avoid
						// endless loops
		if (start.boxes.contains(c))
			return false;

		boolean xLocked = false, yLocked = false;
		Coord L = c.relL();
		Coord R = c.relR();
		if (this.board.get(L) == -1 || this.board.get(R) == -1) {
			xLocked = true; // box is next to a wall
		}
		if (this.boxes.contains(L) && isFreezeLocked(L, visited, start)) {
			xLocked = true; // there is a box to the left which is freezelocked
		}
		if (this.boxes.contains(R) && isFreezeLocked(R, visited, start)) {
			xLocked = true; // there is a box to the right which is freezelocked
		}

		Coord U = c.relU();
		Coord D = c.relD();
		if (this.board.get(U) == -1 || this.board.get(D) == -1) {
			yLocked = true; // box is above or below a wall
		}
		if (this.boxes.contains(U) && isFreezeLocked(U, visited, start)) {
			yLocked = true; // there is a box above which is freezelocked
		}
		if (this.boxes.contains(D) && isFreezeLocked(D, visited, start)) {
			yLocked = true; // there is a box to the right which is freezelocked
		}

		if (xLocked && yLocked)
			return true;

		return false;

	}

	/**
	 * @param s
	 *            the state to check
	 * @return true if a box in the state is Corral-locked on a non-goal square
	 *         false if no box in the state is Corral-locked on a non-goal
	 *         square
	 */
	public boolean isCorralLocked() {
		return false;
	}

	/**
	 * @param s
	 *            the state to check
	 * @return true if a box in the state is Bipartite-locked on a non-goal
	 *         square false if no box in the state is Bipartite-locked on a
	 *         non-goal square
	 */
	public boolean isBipartiteLocked() {
		return false;
	}

	/**
	 * @param s
	 *            the state to check
	 * @return true if a box in the state is locked false if no box in the state
	 *         is locked
	 */
	public boolean isLockedBecauseOfOtherFrozenBoxesAndIsThereForeLockedThemselvesWhichIsAnnoying() {
		return false;
	}

	/**
	 * @param s
	 *            the state to check
	 * @return true if a box in the state is locked false if no box in the state
	 *         is locked
	 */
	public boolean closedDiagonalLock() {
		return false;
	}

	@Override
	public int compareTo(State other) {
		return (other.goalCount - this.goalCount)<<20 + (other.distance - this.distance);
	}

}
