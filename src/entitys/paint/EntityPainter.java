package entitys.paint;

import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.util.vector.Matrix4f;

import area.LandscapeShader;
import entitys.CollisionFreeEntityIterator;
import entitys.Entity;
import entitys.EntityList;
import main.grphics.Render3D;

public class EntityPainter {

	private Map<Integer, Entity3DModel> models;
	
	private static EntityPainter me;
	
	public EntityPainter(){
		models = new TreeMap<>();
		models.put(1, new Entity3DModel());
		
		me = this;
	}
	
	public void render3dUnits(Render3D r3d, CollisionFreeEntityIterator c){
		for (;c.hasNext();) {
			single(c.next(), r3d);
		}
		r3d.getShader().setUniformMatrix(LandscapeShader.U_TRANSLATION, false, new Matrix4f());
	}
	
	private void single(EntityList en, Render3D r3d){
		en.workStarted();
		for (Entity e : en.list) {
			e.model.render(e, r3d);
		}
		en.workEnd();
	}
	
	public static Entity3DModel getModel(int id){
		return me.models.get(id);
	}
}
