import java.util.Set;


/**
 * TODO Put here a description of what this class does.
 *
 * @author tim.
 *         Created Sep 20, 2012.
 */
final public class State {
	final Coord player;
	final Set<Coord> boxes;
	final Board board;
	/**
	 * TODO Put here a description of what this constructor does.
	 *@param b = set of boxes
	 */
	public State(Coord player, Set<Coord> boxes){
		this.player = player;
		this.boxes=boxes;
		board = null;
	}
	public State(Coord player, Set<Coord> boxes, Board board){
		this.player = player;
		this.boxes=boxes;
		this.board = board;
	}


}
