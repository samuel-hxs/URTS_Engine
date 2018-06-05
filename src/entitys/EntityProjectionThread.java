package entitys;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.Semaphore;

import org.joml.AxisAngle4d;
import org.joml.AxisAngle4f;
import org.joml.Matrix3d;
import org.joml.Matrix3f;
import org.joml.Matrix3x2fc;
import org.joml.Matrix4d;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Matrix4x3f;
import org.joml.Matrix4x3fc;
import org.joml.Planef;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector2fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

public class EntityProjectionThread extends Thread{

	private Semaphore sema;
	private CollisionFreeEntityIterator cfi;
	private Matrix4f viewMatrix;
	
	public EntityProjectionThread(){
		super("EntityProjection");
		EntityThreadTimer.t.start(this);
		sema = new Semaphore(1);
		sema.acquireUninterruptibly();
		viewMatrix = new Matrix4f();
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
		
		en.workStarted();
		for (Entity e : en.list) {
			v.x = e.xPos;
			v.y = e.yPos;
			v.z = e.zPos;
			v.w = 1;
			
			viewMatrix.transform(v);
			e.renderIcon = v.z>0;
			e.xOnScreen = v.x/v.w;
			e.yOnScreen = v.y/v.w;
		}
		en.workEnd();
	}
	
	public void start(CollisionFreeEntityIterator c, Matrix4f proj, Matrix4f cam){
		if(sema.availablePermits()>0)
			debug.Debug.println("*ERROR EntityThread(s) 02: Trying to restart while still running!", debug.Debug.ERROR);
		
		cfi = c;
		viewMatrix.set(proj);
		viewMatrix = viewMatrix.mul(cam);
		
		sema.release();
	}
	
}
