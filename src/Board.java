import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;



/**
 * TODO Put here a description of what this class does.
 *
 * @author Tim.
 *         Created Sep 20, 2012.
 */
public class Board {
	byte[][] board;
	Set<Coord> goals;
	
	
	public Board(byte [] [] b, Set<Coord> g){
		this.board = b;
		this.goals = g;
	}
	public byte get(Coord c){
		return board[c.y][c.x];
	}
	
	

	/*
	 * prints the graph without any content
	 */
	public void printBoard(){
		System.out.println("Printing board without content");
		System.out.print(" ");
		for(int i=0;i<board[0].length;i++){
			System.out.print(i);
		}
			System.out.println();
		for(int y=0;y<board.length;y++){
			System.out.print(y);
			for(int x=0;x<board[0].length;x++){
				System.out.print(board[x][y]);
			}
			System.out.println();
		}
	}
	/*
	 * prints the graph with content
	 * @param s Given state to print
	 */
	public void printBoard(State s){
		Set<Coord> boxes = s.boxes;
		Coord player = s.player;
		System.out.println("Printing board with content");
		System.out.print(" ");
		System.out.println(boxes);
		for(int i=0;i<board[0].length;i++){
			System.out.print(i);
		}
			System.out.println();
		for(int y=0;y<board.length;y++){
			System.out.print(y);
			for(int x=0;x<board[0].length;x++){
				 Coord tmp = new Coord(x,y);
				if(boxes.contains(tmp) && goals.contains(tmp)){
					System.out.print('*');
				}else if(player.equals(tmp) && goals.contains(tmp)){
					System.out.print('+');
				}else if(player.equals(tmp)){
					System.out.print('@');
				}else if(goals.contains(tmp)){
					System.out.print('.');
				}else if(boxes.contains(tmp)){
					System.out.print('$');
				}else{
					System.out.print(board[x][y]);
				}
			}
			System.out.println();
		}
	}

}
