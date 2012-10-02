
/**
 * TODO Put here a description of what this class does.
 *
 * @author Brute Force.
 *         Created Sep 21, 2012.
 */
final public class Coord {
		/**
		 * TODO Put here a description of this field.
		 */
		final int y;
		/**
		 * TODO Put here a description of this field.
		 */
		final int x;
		
		/**
		 * TODO Put here a description of what this constructor does.
		 *
		 * @param y
		 * @param x
		 */
		public Coord(int y, int x){
			this.y = y;
			this.x = x;
		}
		/*
		 * TODO should probably rewrite this, just here to be, well something
		 */
		@Override
		public int hashCode(){
			return (int) (this.x*Math.pow(2,16)+this.y);
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
			return "(" + this.y + "," + this.x +")";
		}
		
		
		/**
		 * @return coordinates to the position to the right
		 */
		public Coord relR(){
			return new Coord(this.x+1,this.y);
		}
		/**
		 * @return coordinates to the position below
		 */
		public Coord relD(){
			return new Coord(this.x,this.y+1);
		}
		/**
		 * @return coordinates to the position to the left
		 */
		public Coord relL(){
			return new Coord(this.x-1,this.y);
		}
		/**
		 * @return coordinates to the position above
		 */
		public Coord relU(){
			return new Coord(this.x,this.y-1);
		}
		
}
