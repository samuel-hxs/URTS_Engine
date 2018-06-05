package entitys;

import java.util.BitSet;

/**
 * A slow updater handling on 500ms tick-rate. Updates renderFOW-Flags etc. 
 * @author Sven Schneider
 */
public class EntityTickUpdate extends Thread{

	public final EntityControle ec;
	private long mark;
	private static long time;
	
	private BitSet bits;
	public static final int RESOLUTION = 500;
	private float ratio;
	
	public EntityTickUpdate(EntityControle e){
		ec = e;
		
		bits = new BitSet(RESOLUTION*RESOLUTION);
		
		start();
	}
	
	@Override
	public void run() {
		while(true){
			long t = System.currentTimeMillis()-mark;
			if(t<500 && t>0){
				try {
					sleep(500-t);
				} catch (Exception e) {
				}
			}
			mark = System.currentTimeMillis();
			
			ratio = (float)main.GameControle.getMapSize() / (float)RESOLUTION;
			bits.clear();
			
			for (int i = ec.getLenght()-1; i >= 0; i--) {
				singleBlock(ec.get(i));
			}
			
			setTime(System.currentTimeMillis()-mark);
		}
	}
	
	private void singleBlock(EntityList en){
		en.workStarted();
		for (Entity e : en.list) {
			if(!e.renderFow)continue;
			e.renderFowSkip = !check(e.xPos, e.yPos, e.fowRadius);
		}
		en.workEnd();
	}
	
	public static synchronized long lastTime(){
		return time;
	}
	
	private static synchronized void setTime(long t){
		time = t;
	}
	
	private boolean check(float x, float y, float rad){
		int xs = (int)((x+main.GameControle.getMapSize()/2)/ratio);
		int ys = (int)((y+main.GameControle.getMapSize()/2)/ratio);
		int u = (int)(rad/ratio);
		int us = u*u;
		boolean needed = false;
		for (int i = -u; i < u; i++) {
			if(i+xs<0 || i+xs>=RESOLUTION)continue;
			for (int j = -u; j < u; j++) {
				if(i*i+j*j > us)continue;
				if(j+ys<0 || j+ys>=RESOLUTION)continue;
				if(bits.get((i+xs)*RESOLUTION+j+ys))continue;
				
				needed = true;
				bits.set((i+xs)*RESOLUTION+j+ys);
			}
		}
		return needed;
	}
}
