import java.util.ArrayList;
import java.util.Set;


/**
 * TODO Put here a description of what this class does.
 *
 * @author tim.
 *         Created Oct 2, 2012.
 */
public class Solver {

	Board board;
	public Solver(){
		
	}
	public String solve(ArrayList<String> input){
		this.board = buildBoard(input);
		
		return "";
	}
	private Board buildBoard(ArrayList<String> list){
		int xSize = 0;
		for(String str : list){
			if(str.length()>xSize){
				xSize = str.length();
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		byte [] [] indata;
		Set<Coord> goals;
		
		
		
		Board b = new Board(indata,goals);
		return b;
		
	}
}
