package entitys;

import java.util.BitSet;
import java.util.concurrent.Semaphore;

public class EntitySecondCheckThread extends Thread{

	private Semaphore sema;
	private CollisionFreeEntityIterator cfi;
	
	public EntitySecondCheckThread(){
		super("EntitySecondCheck");
		EntityThreadTimer.t.start(this);
		sema = new Semaphore(1);
		sema.acquireUninterruptibly();
		
		start();
	}
	
	@Override
	public void run() {
		while (true) {
			sema.acquireUninterruptibly();
			EntityThreadTimer.t.start(this);
			for (; cfi.hasNext();) {
				singleBlock(cfi.next());
			}
			
			EntityThreadTimer.t.done(this);
		}
	}
	
	private void singleBlock(EntityList en){
		en.workStarted();
		for (Entity e : en.list) {
			
		}
		en.workEnd();
	}
	
	public void start(CollisionFreeEntityIterator c){
		if(sema.availablePermits()>0)
			debug.Debug.println("*ERROR EntityThread(s) 04: Trying to restart while still running!", debug.Debug.ERROR);
		
		cfi = c;
		sema.release();
	}
	
}
