package entitys;

/**
 * Extended Meeting-Room Implementation
 * @author Sven T. Schneider
 */
public class MeetToContinue {

	public final int numberToContinue;
	
	/**
	 * @param n Number of threads expected in this meeting-room
	 */
	public MeetToContinue(int n){
		numberToContinue = n;
	}
	
	/**
	 * Holds until a specified number of Threads are in the Meeting-Room
	 * @param target number of threads needed to continue except the calling thread
	 */
	public void wiatUntil(int target){
		
	}
}
