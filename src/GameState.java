import java.util.Set;


/**
 * TODO Put here a description of what this class does.
 *
 * @author tim.
 *         Created Sep 20, 2012.
 */
final public class GameState {
	final Coords player;
	final Set<Coords> boxes;
	final Board board;
	/**
	 * TODO Put here a description of what this constructor does.
	 *@param b = set of boxes
	 */
	public GameState(Coords player, Set<Coords> boxes){
		this.player = player;
		this.boxes=boxes;
		board = null;
	}
	public GameState(Coords player, Set<Coords> boxes, Board board){
		this.player = player;
		this.boxes=boxes;
		this.board = board;
	}


}
