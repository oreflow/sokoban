import java.util.LinkedList;


/**
 * TODO Put here a description of what this class does.
 *
 * @author tim.
 *         Created Sep 21, 2012.
 */
final public class Coord {
		final int y;
		final int x;
		
		public Coord(int y, int x){
			this.y = y;
			this.x = x;
		}
		/*
		 * TODO should probably rewrite this, just here to be, well something
		 */
		@Override
		public int hashCode(){
			return (int) (x*Math.pow(2,16)+y);
		}
		@Override
		public boolean equals(Object o){
			Coord c = (Coord) o;
			if(this.x == c.x && this.y == c.y){
				return true;
			}
			return false;
		}
		
		@Override
		public String toString(){
			return "(" + y + "," + x +")";
		}
		
		public Coord [] neighbours(){
			Coord [] l ={relR(),relD(),relL(),relU()};
			return l;
		}
		
		public Coord relR(){
			return new Coord(x+1,y);
		}
		public Coord relD(){
			return new Coord(x,y+1);
		}
		public Coord relL(){
			return new Coord(x-1,y);
		}
		public Coord relU(){
			return new Coord(x,y-1);
		}
		
}
