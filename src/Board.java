import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * 
 * @author Brute Force. Created Sep 20, 2012.
 */
public class Board {
	/** byte representation of the board */
	byte[][] board;
	/** set containing the goals */
	Set<Coord> goals;

	Coord playerStart;

	/**
	 * 
	 * 
	 * @param b
	 * @param g
	 */
	public Board(byte[][] b, Set<Coord> g, Coord player) {
		this.board = b;
		this.goals = g;
		playerStart = player;
		setDistances();
	}

	public boolean isWalkable(Coord c) {
		return this.board[c.y][c.x] >= 0;
	}

	public boolean hasPath(Coord first, Coord second, Set<Coord> visited) {
		// Found it!
		if (second.equals(first))
			return true;

		// Haven't found it yet. Labels this coord as visited.
		visited.add(first);

		// Loops through all neighbours. If there is a path from first to
		// second, there is a path from some neighbour of second to goal.
		for (Coord neighbour : first.getNeighbors()) {
			if (!visited.contains(neighbour) && isWalkable(neighbour)) {
				if (hasPath(neighbour, second, visited))
					return true;
			}
		}

		// No path.
		return false;
	}

	public boolean hasPath(Coord first, Coord second) {
		return hasPath(first, second, new HashSet<Coord>());
	}

	public Coord lowestReachable(Coord origin) {
		return lowestReachable(origin, new HashSet<Coord>());
	}

	public Coord lowestReachable(Coord origin, Set<Coord> visited) {
		Queue<Coord> q = new LinkedList<Coord>();
		q.add(origin);
		visited.add(origin);

		Coord lowest = origin;

		while (!q.isEmpty()) {
			// Checks if a coordinate is the lowest.
			Coord c = q.poll();
			if (c.y < lowest.y) {
				lowest = c;
			} else if (c.y == lowest.y && c.x < lowest.x) {
				lowest = c;
			}
			for (Coord adj : c.getNeighbors()) {
				if (isWalkable(adj) && !visited.contains(adj)) {
					visited.add(adj);
					q.add(adj);
				}
			}
		}

		return lowest;
	}

	/**
	 * 
	 * 
	 * @param c
	 * @return the byte at given coordinate
	 */
	public byte get(Coord c) {
		return this.board[c.y][c.x];
	}

	/**
	 * 
	 * sets the distances to nearest goal in the Board
	 * 
	 */
	public void setDistances() {
		for (Coord currentGoal : this.goals) {
			Queue<Coord> q = new LinkedList<Coord>();
			Queue<Byte> depthQueue = new LinkedList<Byte>();
			q.add(currentGoal);
			depthQueue.add((byte) 0);
			boolean visited[][] = new boolean[this.board.length][this.board[0].length];
			visited[currentGoal.y][currentGoal.x] = true;

			while (!q.isEmpty()) {
				Coord current = q.poll();
				byte currentDepth = depthQueue.poll();
				visited[current.y][current.x] = true;
				if (this.board[current.y][current.x] > currentDepth) {
					this.board[current.y][current.x] = currentDepth;
					for (Coord c : current.getNeighbors()) {
						q.add(c);
						depthQueue.add((byte) (currentDepth + 1));
					}
				} else {
					continue;
				}

			}
		}
		return;
	}

	/**
	 * prints the graph without any content
	 */
	public void printBoard() {
		System.out.println("Printing board without content");
		System.out.print(" ");
		for (int i = 0; i < this.board[0].length; i++) {
			System.out.print(i);
		}
		System.out.println();
		for (int y = 0; y < this.board.length; y++) {
			System.out.print(y);
			for (int x = 0; x < this.board[0].length; x++) {
				if (this.board[y][x] == -1)
					System.out.print("#");
				else if (this.goals.contains(new Coord(y, x)))
					System.out.print(".");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}

	/**
	 * prints the graph with numbers
	 */
	public void printNumbers() {
		System.out.println("Printing board without content");
		System.out.print(" ");
		for (int i = 0; i < this.board[0].length; i++) {
			System.out.print(i + "\t");
		}
		System.out.println();
		for (int y = 0; y < this.board.length; y++) {
			System.out.print(y + "\t");
			for (int x = 0; x < this.board[0].length; x++) {
				System.out.print(this.board[y][x] + "\t");

			}
			System.out.println();
		}
	}

	/**
	 * prints the graph with content
	 * 
	 * @param s
	 *            Given state to print
	 */
	public void printBoard(State s) {
		Set<Coord> boxes = s.boxes;
		Coord player = s.player;
		System.out.println("Printing board with content");
		System.out.print(" ");
		System.out.println(boxes);
		for (int i = 0; i < this.board[0].length; i++) {
			System.out.print(i);
		}
		System.out.println();
		for (int y = 0; y < this.board.length; y++) {
			System.out.print(y);
			for (int x = 0; x < this.board[0].length; x++) {
				Coord tmp = new Coord(y, x);
				if (boxes.contains(tmp) && this.goals.contains(tmp)) {
					System.out.print('*');
				} else if (player.equals(tmp) && this.goals.contains(tmp)) {
					System.out.print('+');
				} else if (player.equals(tmp)) {
					System.out.print('@');
				} else if (this.goals.contains(tmp)) {
					System.out.print('.');
				} else if (boxes.contains(tmp)) {
					System.out.print('$');
				} else if (this.board[y][x] == -1)
					System.out.print("#");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}

}
