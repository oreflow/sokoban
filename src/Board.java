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
	Set<Coords> goals;
	
	
	public Board(int x,int y,char [] [] b, HashSet<Coords> g){
		this.sizeX = x;
		this.sizeY = y;
		this.board = b;
		this.goals = g;
	}
	public char get(Coords c){
		return board[c.x][c.y];
	}
	
	/*
	 * movement of a player from (x,y) to (x',y')
	 */
	public Path playerBFS(Coords from,Coords to, HashSet<Coords> boxes){
		
		if(from.equals(to)){
			return new Path(from,null);
		}
		Queue<Path> q = new LinkedList<Path>();
		q.add(new Path(from,null));
		boolean visited [][] = new boolean [sizeX][sizeY];
		visited[from.x][from.y] = true;
		
		while (!q.isEmpty()){
			Path current = q.poll();
			for(Coords c : current.coords.neighbours()){
				if(to.equals(c)){
					return new Path(c,current);
				}
				if(get(c) == ' ' && !boxes.contains(c)){
					q.add(new Path(c,current));
					visited[c.x][c.y] = true;
				}
			}
		}
		return null;
	}
	
	/*
	 * movement of a box from (x,y) to (x',y')
	 */
	public Path boxBFS(Coords from,Coords to,HashSet<Coords> boxes){
		
		if(from.equals(to)){
			return new Path(from,null);
		}
		Queue<Path> q = new LinkedList<Path>();
		q.add(new Path(from,null));
		boolean visited [][] = new boolean [sizeX][sizeY];
		visited[from.x][from.y] = true;
		
		while (!q.isEmpty()){
			Path current = q.poll();
			for(Coords c : current.coords.neighbours()){
				if(	(current.coords.to(c) == 'R' && get(current.coords.L()) != ' ') ||
					(current.coords.to(c) == 'L' && get(current.coords.R()) != ' ') ||
					(current.coords.to(c) == 'U' && get(current.coords.D()) != ' ') ||
					(current.coords.to(c) == 'D' && get(current.coords.U()) != ' ') ){
					continue;
				}
				if(to.equals(c)){
					return new Path(c,current);
				}
				if(get(c) == ' '){
					q.add(new Path(c,current));
					visited[c.x][c.y] = true;
				}
			}
		}
		return null;
	}
	
	/*
	 * movement of a box from (x,y) nearest goal
	 */
	public Path boxBFS(Coords from,HashSet<Coords> boxes){
		
		if(goals.contains(from)){
			return new Path(from,null);
		}
		Queue<Path> q = new LinkedList<Path>();
		q.add(new Path(from,null));
		boolean visited [][] = new boolean [sizeX][sizeY];
		visited[from.x][from.y] = true;
		
		while (!q.isEmpty()){
			Path current = q.poll();
			for(Coords c : current.coords.neighbours()){
				if(	(current.coords.to(c) == 'R' && get(current.coords.L()) != ' ') ||
					(current.coords.to(c) == 'L' && get(current.coords.R()) != ' ') ||
					(current.coords.to(c) == 'U' && get(current.coords.D()) != ' ') ||
					(current.coords.to(c) == 'D' && get(current.coords.U()) != ' ') ){
					continue;
				}
				if(goals.contains(c)){
					return new Path(c,current);
				}
				if(get(c) == ' '){
					q.add(new Path(c,current));
					visited[c.x][c.y] = true;
				}
			}
		}
		return null;
	}
	/*
	 * @return a string representation using U,L,D,R for where given box can be moved
	 * @return appends a G to the string if the box is on a goal square
	 */
	public String canBeMoved(Coords box,HashSet<Coords> boxes, Coords player){
		String str="";
		if(get(box.R()) == ' ' &&
				!boxes.contains(box.R()) &&
				!boxes.contains(box.L()) &&
				playerBFS(player,box.L(),boxes) != null){
			str = str + "R";
		}
		if(get(box.L()) == ' ' &&
				!boxes.contains(box.R()) &&
				!boxes.contains(box.L()) &&
				playerBFS(player,box.R(),boxes) != null){
			str = str + "L";
		}
		if(get(box.U()) == ' ' &&
				!boxes.contains(box.U()) &&
				!boxes.contains(box.D()) &&
				playerBFS(player,box.D(),boxes) != null){
			str = str + "U";
		}
		if(get(box.D()) == ' ' &&
				!boxes.contains(box.D()) &&
				!boxes.contains(box.U()) &&
				playerBFS(player,box.U(),boxes) != null){
			str = str + "D";
		}
		if(goals.contains(box)){
			// Box is on Goal
			str = str + "G";
		}
		return str;
	}
	public String canBeMoved(Coords box,HashSet<Coords> boxes){
		String str="";
		if(get(box.R()) == ' ' &&
				!boxes.contains(box.R()) &&
				!boxes.contains(box.L())){
			str = str + "R";
		}
		if(get(box.L()) == ' ' &&
				!boxes.contains(box.R()) &&
				!boxes.contains(box.L())){
			str = str + "L";
		}
		if(get(box.U()) == ' ' &&
				!boxes.contains(box.U()) &&
				!boxes.contains(box.D())){
			str = str + "U";
		}
		if(get(box.D()) == ' ' &&
				!boxes.contains(box.D()) &&
				!boxes.contains(box.U())){
			str = str + "D";
		}
		if(goals.contains(box)){
			// Box is on Goal
			str = str + "G";
		}
		return str;
	}
	/*
	 * Moves the box at given coord, one step in the direction d
	 * @return String representing all player moves needed to be done for this move
	 * @return null if the move is not possible
	 */
public String moveBox(Coords box, char d,Coords player,HashSet<Coords> boxes){
		String s;
		try{
			if( d == 'L'){
				return playerBFS(player,box.R(),boxes).walk() + "L";
			}else if( d == 'R'){
				return playerBFS(player,box.L(),boxes).walk() + "R";
			}else if( d == 'U'){
				return playerBFS(player,box.D(),boxes).walk() + "U";
			}else if( d == 'D'){
				return playerBFS(player,box.U(),boxes).walk() + "D";
			}
		} catch(NullPointerException e){
			
		}
		return null;
		
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
	public void printBoard(HashSet<Coords> boxes, Coords player){
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
				 Coords tmp = new Coords(x,y);
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
