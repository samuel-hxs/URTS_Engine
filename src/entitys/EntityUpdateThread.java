package entitys;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import area.AreaControle;

public class EntityUpdateThread extends Thread{

	private Semaphore sema;
	private CollisionFreeEntityIterator cfi;
	
	public EntityUpdateThread(){
		super("EntityUpdate");
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
			e.xPos += Math.sin(e.debug1)*e.debug2;
			e.yPos += Math.cos(e.debug1)*e.debug2;
			
			if(Math.abs(e.xPos) > main.GameControle.getMapSize()/2){
				e.debug1+=Math.PI;}
			if(Math.abs(e.yPos) > main.GameControle.getMapSize()/2){
				e.debug1+=Math.PI;}
			
			e.zPos = AreaControle.currentArea.getNodeHeight(e.xPos, e.yPos)+1;
		}
		en.workEnd();
	}
	
	public void start(CollisionFreeEntityIterator c){
		if(sema.availablePermits()>0)
			debug.Debug.println("*ERROR EntityThread(s) 01: Trying to restart while still running!", debug.Debug.ERROR);
		
		cfi = c;
		sema.release();
	}
	
}
