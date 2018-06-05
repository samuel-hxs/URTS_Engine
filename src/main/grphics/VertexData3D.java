package main.grphics;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import main.grphics.VertexDataManager.RenderingHints;
import mdesl.graphics.SpriteBatch;
import mdesl.graphics.Texture;
import mdesl.graphics.glutils.VertexArray;
import mdesl.graphics.glutils.VertexData;

public class VertexData3D {

	private final Semaphore lock;
	
	private VertexData data;
	private Texture tex;
	
	private Brush3D b3d;
	
	public utility.GeometryConstants.FACING renderingHintGeom;
	public VertexDataManager.RenderingHints renderingHintGeneral = RenderingHints.ALWAYS;
	
	public Matrix4f translate = null;
	public Vector3f positionForCulling = null;
	public float radiusForCulling = 1.0f;
	
	public final VertexDataManager.SizeOfVertexArray size;
	
	public VertexData3D(){
		this(1000, VertexDataManager.SizeOfVertexArray.SIZE_1K);
	}
	
	public VertexData3D(int ammount, VertexDataManager.SizeOfVertexArray s){
		lock = new Semaphore(1);
		
		data = new VertexArray(ammount*6, SpriteBatch.ATTRIBUTES);
		
		b3d = new Brush3D(lock, data);
		size = s;
	}
	
	public void render(Render3D sp){
		try {
			if(!lock.tryAcquire(100, TimeUnit.MILLISECONDS)){
				handleMissed();
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			handleMissed();
			return;
		}
		
		sp.setTranslation(translate);
		sp.render(data, tex);
		
		lock.release();
	}
	
	private void handleMissed(){
		//TODO
	}
	
	public Brush3D getBrush(){
		return b3d;
	}
	
	public void setTexure(Texture tex) {
		this.tex = tex;
	}
}
