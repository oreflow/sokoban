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
		this.boxes
				.add(new Coord(oldBox.y + relBoxPos.y, oldBox.x + relBoxPos.x));
		this.player = player;
		this.board = s.board;
		this.allPlayer = allAdjacent(player);
		this.distance = distanceToGoal();
		this.goalCount = goalCount();
	}

	private int goalCount() {
		int goalCount = 0;
		for (Coord c : boxes) {
			if (board.goals.contains(c)) {
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
				if (!visited.contains(neighbour) && board.isWalkable(neighbour)
						&& !boxes.contains(neighbour)) {
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

		// Loops through all the boxes of this state.
		for (Coord box : boxes) {
			Coord[] boxNeighbours = getBoxNeighbours(box);
			for (int i = 0; i < 4; i++) {
				Coord possibleNeighbour = boxNeighbours[i];
				if (possibleNeighbour == null || !canReach(possibleNeighbour))
					continue;
				Set<Coord> newBoxSet = new HashSet<Coord>();
				newBoxSet.addAll(boxes);
				newBoxSet.remove(box);
				newBoxSet.add(boxNeighbours[i]);
				State s = new State(board.lowestReachable(boxNeighbours[i + 4],
						newBoxSet), newBoxSet, board);
				if (!Solver.stateCache.containsKey(this))
					neighbours.add(s);
			}
		}

		Solver.stateCache.put(this, neighbours);
		return neighbours;
	}

	public Coord[] getBoxNeighbours(Coord box) {
		Coord[] neighbours = new Coord[8];
		Coord[] allDirs = { new Coord(box, Coord.LEFT),
				new Coord(box, Coord.UP), new Coord(box, Coord.RIGHT),
				new Coord(box, Coord.DOWN) };
		Coord[] allDirs2 = { new Coord(allDirs[0], Coord.LEFT),
				new Coord(allDirs[1], Coord.UP),
				new Coord(allDirs[2], Coord.RIGHT),
				new Coord(allDirs[3], Coord.DOWN) };
		for (int i = 0; i < 4; i++) {
			if (board.isWalkable(allDirs[i]) && board.isWalkable(allDirs2[i])
					&& !boxes.contains(allDirs[i])
					&& !boxes.contains(allDirs2[i]) && canReach(allDirs[i])) {
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
		if (player != null && other.player != null
				&& !player.equals(other.player)) {
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

	@Override
	public int compareTo(State other) {
		int temp = (other.goalCount - this.goalCount) << 20 + (other.distance - this.distance);
		if(temp == 0){
			return other.player.hashCode() - this.player.hashCode();
		}else{
			return temp;
		}
	}

}
