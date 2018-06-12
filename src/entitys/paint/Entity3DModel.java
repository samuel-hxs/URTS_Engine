package entitys.paint;

import org.joml.Vector3f;

import area.LandscapeShader;
import main.grphics.Render3D;
import mdesl.graphics.Texture;
import entitys.Entity;

public class Entity3DModel {

	/**
	 * Highest detail Modell, renders when very close
	 */
	private OnMapObject v1;
	
	/**
	 * Lower detail Modell, intermediat rendering distance
	 */
	private OnMapObject v2;
	
	/**
	 * Very low detail Modell, renders always
	 */
	private OnMapObject v3;
	
	/**
	 * Rendering-Hints: Distance to use detail-models.
	 */
	private float distance1, distance2;
	
	/**
	 * Size of the Object to render (For frustum-filtering
	 */
	private float size;
	
	private Vector3f vecT;
	private Vector3f vecR;
	
	
	public Entity3DModel(){
		vecT = new Vector3f();
		vecR = new Vector3f();

		//TEST_VALUES: TODO remove!
		size = 1;
		distance1 = 20;
		distance2 = 80;
		
		v1 = OnMapObject.getTestCircle(20);
		v2 = OnMapObject.getTestCircle(6);
	}
	
	public void render(Entity e, Render3D r3d){
		if(e.outsideFrustum)
			return;
		vecT.x = e.xPos;
		vecT.y = e.yPos;
		vecT.z = e.zPos;
		
		if(e.renderMeshComplex && v1 != null)
			v1.render(r3d, vecT, vecR);
		else if(e.renderMeshSimple && v2 != null)
			v2.render(r3d, vecT, vecR);
		else if(v3 != null)
			v3.render(r3d, vecT, vecR);
	}
	
	public float getSize() {
		return size;
	}
	
	public float getDistance1() {
		return distance1;
	}
	
	public float getDistance2() {
		return distance2;
	}
	
}
