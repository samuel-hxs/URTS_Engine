package entitys;

/**
 * Will hold Timing-Values for all Entity-Threads and will display them nicely
 * @author Sven T. Schneider
 */
public class EntityThreadTimer {

	private Timer[] timers;
	public static EntityThreadTimer t;
	
	public EntityThreadTimer(){
		timers = new Timer[10];
		t = this;
	}
	
	public synchronized void start(Thread t){
		get(t).start();
	}
	
	public synchronized void done(Thread t){
		get(t).done();
	}
	
	private Timer get(Thread t){
		for (int i = 0; i < timers.length; i++) {
			if(timers[i] == null)
				return timers[i] = new Timer(t);
			if(timers[i].holds == t)
				return timers[i];
		}
		return timers[9];
	}
	
	public synchronized String toString(){
		String s = "";
		for (int i = 0; i < timers.length; i++) {
			if(timers[i] == null)break;
			s+=timers[i];
		}
		return s;
	}
	
	private class Timer{
		
		public final Thread holds;
		private long lastTime;
		private long showTime;
		
		public Timer(Thread t){
			holds = t;
			lastTime = System.nanoTime();
			showTime = 0;
		}
		
		public void done(){
			long t = System.nanoTime();
			if(showTime == 0){
				showTime = t-lastTime;
			}else{
				showTime *= 4;
				showTime += t-lastTime;
				showTime /= 5;
			}
			lastTime = t;
		}
		
		public void start(){
			lastTime = System.nanoTime();
		}
		
		public long get(){
			return showTime;
		}
		
		public String toString(){
			return holds.getName()+":"+(showTime/1000000l)+","+(showTime/100000)%10+(showTime/10000)%10
					+(showTime/1000)%10+"ms ";
		}
	}
	
}
