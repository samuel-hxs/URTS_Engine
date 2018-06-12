package entitys;

import java.util.concurrent.Semaphore;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import main.grphics.FrustumCullingFilter;

public class EntityFrustumThread extends Thread{

	private Semaphore sema;
	private CollisionFreeEntityIterator cfi;
	private FrustumCullingFilter fcf;
	private Vector4f cameraPos;
	
	public EntityFrustumThread(){
		super("EntityFrustum");
		EntityThreadTimer.t.start(this);
		sema = new Semaphore(1);
		sema.acquireUninterruptibly();
		fcf = new FrustumCullingFilter();
		cameraPos = new Vector4f();
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
		float d = 0;
		
		en.workStarted();
		for(Entity e : en.list){
			v.x = e.xPos;
			v.y = e.yPos;
			v.z = e.zPos;
			
			d = v.distance(cameraPos);
			
			if(e.fowRadius>0){
				e.renderFow = fcf.insideFrustum(v, e.fowRadius);
			}else{
				e.renderFow = false;
			}
			
			if(fcf.insideFrustum(v, e.model.getSize())){
				e.renderMeshSimple = d < e.model.getDistance2();
				e.renderMeshComplex = d < e.model.getDistance1();
				e.outsideFrustum = false;
			}else{
				e.outsideFrustum = true;
			}
		}
		en.workEnd();
	}
	
	public void start(CollisionFreeEntityIterator c, Matrix4f proj, Matrix4f cam, Vector3f pos){
		if(sema.availablePermits()>0)
			debug.Debug.println("*ERROR EntityThread(s) 03: Trying to restart while still running!", debug.Debug.ERROR);
		
		fcf.updateFrustum(proj, cam);
		cfi = c;
		cameraPos.set(pos, 1);
		sema.release();
	}
	
}
