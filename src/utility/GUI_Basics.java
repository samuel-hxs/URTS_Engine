package utility;

public class GUI_Basics {

	public static int[][] getItemPositionsInput(int size, int ammount){
		switch (ammount) {
		case 0:
			return new int[0][0];
		case 1:
			return new int[][]{{0,0}};
		case 2:
			return new int[][]{{0,-size/2-size/6},{0,size/2+size/6}};
		case 3:
			return new int[][]{{0,-size/2-size/6},{0,size/2+size/6},{size+size/3,0}};
		case 4:
			return new int[][]{{0,-size/2-size/6},{0,size/2+size/6},
				{size+size/3,-size/2-size/6},{size+size/3,size/2+size/6}};
		case 5:
			return new int[][]{{0,-size-size/3},{0,0},{0,size/3+size},
				{size+size/3,-size/2-size/6},{size+size/3,size/2+size/6}};

		default:
			debug.Debug.println("*ERROR utility.GUI_Basics 01: Ammount OOR "+ammount, debug.Debug.ERROR);
			return new int[ammount][2];
		}
	}
	
	public static int[][] getItemPositionsOutput(int size, int ammount){
		switch (ammount) {
		case 0:
			return new int[0][0];
		case 1:
			return new int[][]{{0,0}};
		case 2:
			return new int[][]{{0,-size/2-size/6},{0,size/2+size/6}};
		case 3:
			return new int[][]{{0,-size-size/3},{0,0},{0,size/3+size}};

		default:
			debug.Debug.println("*ERROR utility.GUI_Basics 01: Ammount OOR "+ammount, debug.Debug.ERROR);
			return new int[ammount][2];
		}
	}
}
