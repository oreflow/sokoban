import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that represents a state in the game
 * 
 * @author Brute Force. Created Sep 20, 2012.
 */
final public class State {
	/** position of the player in this state */
	final Coord player;
	/** positions of the boxes in this state */
	final Set<Coord> boxes;
	/** pointer to the board this state is in */
	final Board board;

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
				else if (isGoal)
					System.out.print(".");
				else if (isGoal && isPlayer)
					System.out.print("+");
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
		List<State> neighbours = new ArrayList<State>();

		Coord[] allDirs = { Coord.LEFT, Coord.UP, Coord.RIGHT, Coord.DOWN };

		// Loops through all the boxes of this state.
		for (Coord box : boxes) {
			Coord[] boxNeighbours = getBoxNeighbours(box);
			for (int i = 0; i < 4; i++) {
				Coord possibleNeighbour = boxNeighbours[i];
				if (possibleNeighbour == null)
					continue;
				if (board.hasPath(player, possibleNeighbour)) {
					neighbours.add(new State(this, board.lowestReachable(boxNeighbours[4 + i]), box, allDirs[i]));
				}
			}
		}

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
	}

	public Coord[] getBoxNeighbours(Coord box) {
		Coord[] neighbours = new Coord[8];
		Coord[] allDirs = { new Coord(box, Coord.LEFT), new Coord(box, Coord.UP), new Coord(box, Coord.RIGHT), new Coord(box, Coord.DOWN) };
		Coord[] allDirs2 = { new Coord(allDirs[0], Coord.LEFT), new Coord(allDirs[1], Coord.UP), new Coord(allDirs[2], Coord.RIGHT),
				new Coord(allDirs[3], Coord.DOWN) };
		for (int i = 0; i < 4; i++) {
			if (board.isWalkable(allDirs[i]) && board.isWalkable(allDirs2[i]) && !boxes.contains(allDirs[i]) && !boxes.contains(allDirs2[i])) {
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
		code = code + player.hashCode() << 4;
		return code;
	}

	public boolean equals(Object o) {
		if (!(o instanceof State)) {
			return false;
		}
		State other = (State) o;
		if (!player.equals(other.player)) {
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
}
