package debug.util;

public class PrecisionTimingUnit {

	private long[] ticks;
	private int position;
	
	public long lastNumber;
	public long number10;
	public long number50;
	public long number100;
	
	private long startTime;
	
	private int c10;
	private int c50;
	private int c100;
	
	public PrecisionTimingUnit(){
		ticks = new long[100];
		ticks[0] = 0;
		position++;
		startTime = System.nanoTime();
	}
	
	public void mark(){
		long t = System.nanoTime();
		ticks[position] = t-startTime;
		startTime = t;
		
		lastNumber = getAvr(1);
		if(c10++>10){
			number10 = getAvr(10);
			c10 = 0;
		}
		if(c50++>50){
			number50 = getAvr(50);
			c50 = 0;
		}
		if(c100++>10){
			number100 = getAvr(99);
			c100 = 0;
		}
		
		position++;
		if(position >= ticks.length)position = 0;
	}
	
	public void mark(long t){
		ticks[position] = t;
		
		lastNumber = getAvr(1);
		if(c10++>10){
			number10 = getAvr(10);
			c10 = 0;
		}
		if(c50++>50){
			number50 = getAvr(50);
			c50 = 0;
		}
		if(c100++>10){
			number100 = getAvr(99);
			c100 = 0;
		}
		
		position++;
		if(position >= ticks.length)position = 0;
	}
	
	public void start(){
		startTime = System.nanoTime();
	}
	
	private long getAvr(int dist){
		int p;
		long q = 0;
		for (int i = 0; i < dist; i++) {
			p = ticks.length+position-dist;
			p = p%ticks.length;
			q += ticks[p];
		}
		return q/dist;
	}
}
