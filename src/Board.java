import java.util.Set;



/**
 *
 * @author Brute Force.
 *         Created Sep 20, 2012.
 */
public class Board {
	/** byte representation of the board */
	byte[][] board;
	/** set containing the goals */
	Set<Coord> goals;
	
	
	/**
	 * 
	 *
	 * @param b
	 * @param g
	 */
	public Board(byte [] [] b, Set<Coord> g){
		this.board = b;
		this.goals = g;
	}
	/**
	 * 
	 *
	 * @param c
	 * @return the byte at given coordinate
	 */
	public byte get(Coord c){
		return this.board[c.y][c.x];
	}
	
	

	/**
	 * prints the graph without any content
	 */
	public void printBoard(){
		System.out.println("Printing board without content");
		System.out.print(" ");
		for(int i=0;i<this.board[0].length;i++){
			System.out.print(i);
		}
			System.out.println();
		for(int y=0;y<this.board.length;y++){
			System.out.print(y);
			for(int x=0;x<this.board[0].length;x++){
				if(this.board[y][x] == -1)
					System.out.print("#");
				else if(this.goals.contains(new Coord(y,x)))
					System.out.print(".");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}
	/**
	 * prints the graph with content
	 * @param s Given state to print
	 */
	public void printBoard(State s){
		Set<Coord> boxes = s.boxes;
		Coord player = s.player;
		System.out.println("Printing board with content");
		System.out.print(" ");
		System.out.println(boxes);
		for(int i=0;i<this.board[0].length;i++){
			System.out.print(i);
		}
			System.out.println();
		for(int y=0;y<this.board.length;y++){
			System.out.print(y);
			for(int x=0;x<this.board[0].length;x++){
				 Coord tmp = new Coord(x,y);
				if(boxes.contains(tmp) && this.goals.contains(tmp)){
					System.out.print('*');
				}else if(player.equals(tmp) && this.goals.contains(tmp)){
					System.out.print('+');
				}else if(player.equals(tmp)){
					System.out.print('@');
				}else if(this.goals.contains(tmp)){
					System.out.print('.');
				}else if(boxes.contains(tmp)){
					System.out.print('$');
				}else if(this.board[y][x] == -1)
					System.out.print("#");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}

}
