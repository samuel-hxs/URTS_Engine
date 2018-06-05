package area;

/**
 * Class to hold the Map itself
 * @author Sven T. Schneider
 */
public class AreaValues {

	public static final int DOTS_PER_SQUARE = 2;
	public static final int SQUARES_PER_PACKAGE = 10;
	
	public AreaValues(){
		
	}
	
	public class AreaData{
		
		public byte[][][] texture;
		public byte[][][] texIntensity;
		
		public int[][] heightMap;
		
		public static final int DOTS_PER_PACKAGE = SQUARES_PER_PACKAGE*DOTS_PER_SQUARE;
		
		private AreaData(){
			texIntensity = new byte[DOTS_PER_PACKAGE][DOTS_PER_PACKAGE][3];
			texture = new byte[DOTS_PER_PACKAGE][DOTS_PER_PACKAGE][3];
			heightMap = new int[DOTS_PER_PACKAGE][DOTS_PER_PACKAGE];
		}
	}
}
