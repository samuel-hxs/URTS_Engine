package entitys;

import java.util.concurrent.Semaphore;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import main.grphics.FrustumCullingFilter;

public class EntityFrustumThread extends Thread{

	private Semaphore sema;
	private CollisionFreeEntityIterator cfi;
	private FrustumCullingFilter fcf;
	
	public EntityFrustumThread(){
		super("EntityFrustum");
		EntityThreadTimer.t.start(this);
		sema = new Semaphore(1);
		sema.acquireUninterruptibly();
		fcf = new FrustumCullingFilter();
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
		Vector4f v = new Vector4f();
		v.w = 1;
		
		en.workStarted();
		for(Entity e : en.list){
			v.x = e.xPos;
			v.y = e.yPos;
			v.z = e.zPos;
			
			if(e.fowRadius>0){
				e.renderFow = fcf.insideFrustum(v, e.fowRadius+1);
			}else{
				e.renderFow = false;
			}
		}
		en.workEnd();
	}
	
	public void start(CollisionFreeEntityIterator c, Matrix4f proj, Matrix4f cam){
		if(sema.availablePermits()>0)
			debug.Debug.println("*ERROR EntityThread(s) 03: Trying to restart while still running!", debug.Debug.ERROR);
		
		fcf.updateFrustum(proj, cam);
		cfi = c;
		sema.release();
	}
	
}
