package debug;

public class FrameStatistics {

	private static int missed;
	private static int clearMissed;
	
	public static int drawMesh;
	public static int drawSum;
	
	public static int entitysPainted;
	public static int entityFOW;
	
	public static void markMissed() {
		missed++;
		clearMissed = 120;
	}
	
	public static void clear() {
		drawMesh = 0;
		drawSum = 0;
		entitysPainted = 0;
		entityFOW = 0;
		
		clearMissed--;
		if(clearMissed == 0) {
			missed = 0;
		}
	}
}
