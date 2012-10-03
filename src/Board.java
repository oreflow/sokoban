import java.util.LinkedList;
import java.util.Queue;
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
		setDistances();
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
	 * 
	 * sets the distances to nearest goal in the Board
	 *
	 */
	public void setDistances(){
		for(Coord currentGoal : this.goals){
			Queue<Coord> q = new LinkedList<Coord>();
			Queue<Byte> depthQueue = new LinkedList<Byte>();
			q.add(currentGoal);
			depthQueue.add((byte) 0);
			boolean visited [][] = new boolean [this.board.length][this.board[0].length];
			visited[currentGoal.y][currentGoal.x] = true;
			
			while (!q.isEmpty()){
				Coord current = q.poll();
				byte currentDepth = depthQueue.poll();
				visited [current.y][current.x] = true;
				if(this.board [current.y] [current.x] > currentDepth){
					this.board [current.y] [current.x] = currentDepth;
					for(Coord c : current.getNeighbors()){
						q.add(c);
						depthQueue.add((byte) (currentDepth + 1));
					}
				}
				else{
					continue;
				}
				
			}
		}
		return;
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
	 * prints the graph with numbers
	 */
	public void printNumbers(){
		System.out.println("Printing board without content");
		System.out.print(" ");
		for(int i=0;i<this.board[0].length;i++){
			System.out.print(i+"\t");
		}
			System.out.println();
		for(int y=0;y<this.board.length;y++){
			System.out.print(y+"\t");
			for(int x=0;x<this.board[0].length;x++){
					System.out.print(this.board[y][x] + "\t");

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
				 Coord tmp = new Coord(y,x);
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
