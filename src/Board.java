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
	int sizeX, sizeY;
	byte[][] board;	// ändra till byte array
	Set<Coord> goals;
	
	
	public Board(int x,int y,byte [] [] b, Set<Coord> g){
		this.sizeX = x;
		this.sizeY = y;
		this.board = b;
		this.goals = g;
	}
	public byte get(Coord c){
		return board[c.x][c.y];
	}
	
	

	/*
	 * prints the graph without any content
	 */
	public void printBoard(){
		System.out.println("Printing board without content");
		System.out.print(" ");
		for(int i=0;i<sizeX;i++){
			System.out.print(i);
		}
			System.out.println();
		for(int y=0;y<sizeY;y++){
			System.out.print(y);
			for(int x=0;x<sizeX;x++){
				System.out.print(board[x][y]);
			}
			System.out.println();
		}
	}
	/*
	 * prints the graph with content
	 */
	public void printBoard(HashSet<Coord> boxes, Coord player){
		System.out.println("Printing board without content");
		System.out.print(" ");
		System.out.println(boxes);
		for(int i=0;i<sizeX;i++){
			System.out.print(i);
		}
			System.out.println();
		for(int y=0;y<sizeY;y++){
			System.out.print(y);
			for(int x=0;x<sizeX;x++){
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
