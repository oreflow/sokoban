import java.util.Set;


/**
 * Class that represents a state in the game
 *
 * @author Brute Force.
 *         Created Sep 20, 2012.
 */
final public class State {
	/** position of the player in this state */
	final Coord player;
	/** positions of the boxes in this state */
	final Set<Coord> boxes;
	/** pointer to the board this state is in */
	final Board board;
	/**
	 * @param player the players position
	 * @param boxes the boxes positions
	 */
	public State(Coord player, Set<Coord> boxes){
		this.player = player;
		this.boxes=boxes;
		this.board = null;
	}
	
	public Board getBoard(){
		return board;
	}
	
	public Coord getPlayer(){
		return player;
	}
	
	public Set<Coord> getBoxes(){
		return boxes;
	}
	/**
	 *
	 * @param player player position
	 * @param boxes the boxes positions
	 * @param board used game board
	 */
	public State(Coord player, Set<Coord> boxes, Board board){
		this.player = player;
		this.boxes=boxes;
		this.board = board;
	}


}
