package main.grphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Holds VertexData and according Textures for Render
 * @author Sven T. Schneider
 */
public class VertexDataManager {

	public enum RenderingHints{
		NEVER, ALWAYS, TRANSPARENT, TRANSPARENT_GEOM, GEOMETRY
	}
	
	public enum SizeOfVertexArray{
		SIZE_1K, SIZE_200, SIZE_50, CUSTOM
	}
	
	private Map<Integer, VertexData3D> data;
	private List<VertexData3D> staticData;
	
	private List<VertexData3D> unused1k;
	private List<VertexData3D> unused200;
	private List<VertexData3D> unused50;
	
	public utility.GeometryConstants.FACING[] geometryHints;
	public Matrix4f frustum;
	
	private int continouseCount;
	
	public VertexDataManager(){
		data = new HashMap<>(100);
		staticData = new ArrayList<>();
		
		unused1k = new ArrayList<>(100);
		unused200 = new ArrayList<>(100);
		unused50 = new ArrayList<>(100);
		
		geometryHints = new utility.GeometryConstants.FACING[3];
	}
	
	public void render(Render3D re, boolean transparent, FrustumCullingFilter fcf){
		for (VertexData3D v3d : data.values()) {
			singleRender(re, v3d, transparent, fcf);
		}
		
		for (VertexData3D v3d : staticData) {
			singleRender(re, v3d, transparent, fcf);
		}
	}
	
	private void singleRender(Render3D re, VertexData3D v3d, boolean transparent, FrustumCullingFilter fcf){
		if(!check(v3d, transparent))
			return;
		
		if(v3d.positionForCulling != null){
			if(!fcf.insideFrustum(new Vector4f(v3d.positionForCulling, 0f), v3d.radiusForCulling))
				return;
		}
		
		v3d.render(re);
		
		debug.FrameStatistics.drawMesh++;
	}
	
	public boolean check(VertexData3D v3d, boolean transparent){
		switch (v3d.renderingHintGeneral) {
		case ALWAYS:
			return !transparent;
		case TRANSPARENT:
			return transparent;
			
		case GEOMETRY:
			if(!transparent)return hintMatch(v3d.renderingHintGeom);
			return false;
			
		case TRANSPARENT_GEOM:
			if(transparent)return hintMatch(v3d.renderingHintGeom);
			return false;
		
		case NEVER:
		default:
			return false;
		}
	}
	
	private boolean hintMatch(utility.GeometryConstants.FACING f){
		for (int i = 0; i < geometryHints.length; i++) {
			if(geometryHints[i] == f)
				return true;
		}
		return false;
	}
	
	public VertexData3D getV3D(int id){
		return data.get(id);
	}
	
	public void addStaticV3D(VertexData3D v){
		staticData.add(v);
	}
	
	public void removeStaticV3D(VertexData3D v){
		staticData.remove(v);
	}
	
	/**
	 * Allocates a VertexData3D Object to active space. If none is in backup, a new one is created.
	 * @param s the required size
	 * @return the id of the newly allocated V3D
	 */
	public int allocate(SizeOfVertexArray s){
		continouseCount++;
		VertexData3D v3d;
		
		switch (s) {
		case SIZE_50:
			if(unused50.isEmpty())
				v3d = new VertexData3D(50, SizeOfVertexArray.SIZE_50);
			else
				v3d = unused50.get(0);
			break;
		
		case SIZE_200:
			if(unused200.isEmpty())
				v3d = new VertexData3D(200, SizeOfVertexArray.SIZE_200);
			else
				v3d = unused200.get(0);
			break;
		
		case CUSTOM:
			debug.Debug.println("*ERROR Requested v3d with custom size!", debug.Debug.ERROR);
			return -1;
			
		case SIZE_1K:
		default:
			if(unused1k.isEmpty())
				v3d = new VertexData3D(1000, SizeOfVertexArray.SIZE_1K);
			else
				v3d = unused1k.get(0);
			break;
		}
		
		v3d.renderingHintGeneral = RenderingHints.NEVER;
		data.put(continouseCount, v3d);
		
		return continouseCount;
	}
	
	/**
	 * Removes the given V3D from active space and puts it into unused containers. This can not be undone!
	 * @param id the id of the V3D to remove
	 */
	public void forget(int id){
		VertexData3D v3d = data.get(id);
		
		if(v3d == null){
			debug.Debug.println("*Tryed to unload none-existing V3D: "+id, debug.Debug.WARN);
			return;
		}
		
		data.remove(id);
		
		switch (v3d.size) {
		case SIZE_1K:
			unused1k.add(v3d);
			break;
		case SIZE_200:
			unused200.add(v3d);
			break;
		case SIZE_50:
			unused50.add(v3d);
			break;
		case CUSTOM: break;
		}
	}
}
