import java.util.LinkedList;


/**
 * TODO Put here a description of what this class does.
 *
 * @author tim.
 *         Created Sep 21, 2012.
 */
final public class Coords {
		final int y;
		final int x;
		
		public Coords(int y, int x){
			this.y = y;
			this.x = x;
		}
		@Override
		public int hashCode(){
			return (int) (x*Math.pow(2,16)+y);
		}
		@Override
		public boolean equals(Object o){
			Coords c = (Coords) o;
			if(this.x == c.x && this.y == c.y){
				return true;
			}
			return false;
		}
		
		@Override
		public String toString(){
			return "(" + y + "," + x +")";
		}
		
		public Coords [] neighbours(){
			Coords [] l ={relR(),relD(),relL(),relU()};
			return l;
		}
		
		public Coords relR(){
			return new Coords(x+1,y);
		}
		public Coords relD(){
			return new Coords(x,y+1);
		}
		public Coords relL(){
			return new Coords(x-1,y);
		}
		public Coords relU(){
			return new Coords(x,y-1);
		}
		
}
