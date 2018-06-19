package entitys.paint;

import java.io.IOException;

import org.joml.Vector3f;

import area.LandscapeShader;
import main.grphics.Render3D;
import mdesl.graphics.Texture;
import mdesl.graphics.TextureRegion;
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
	 * Corresbonding Filepaths for v1-3
	 */
	public String fileDir1, fileDir2, fileDir3;
	
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
	
	/**
	 * An identifire for the Tac-Icon-Picture
	 */
	private int iconPosition;
	private TextureRegion icon;
	
	public Entity3DModel(){
		vecT = new Vector3f();
		vecR = new Vector3f();

		setIconPosition(0);
		
		//TEST_VALUES: TODO remove!
		size = 1;
		distance1 = 20;
		distance2 = 80;
		
		v1 = OnMapObject.getTestCircle(20);
		v2 = OnMapObject.getTestCircle(6);
	}
	
	public void setModels(String s1, String s2, String s3) throws Exception{
		if(s1 != null)
			if(s1.compareToIgnoreCase("null") != 0 || s1.length()<2)
				v1 = FileImport.load3DObject(FileImport.FILEPATH_MODEL_3D+s1)[0];
		if(s2 != null)
			if(s2.compareToIgnoreCase("null") != 0 || s2.length()<2)
				v2 = FileImport.load3DObject(FileImport.FILEPATH_MODEL_3D+s2)[0];
		if(s3 != null)
			if(s3.compareToIgnoreCase("null") != 0 || s3.length()<2)
				v3 = FileImport.load3DObject(FileImport.FILEPATH_MODEL_3D+s3)[0];
		fileDir1 = s1;
		fileDir2 = s2;
		fileDir3 = s3;
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
	
	public void setDistance1(float distance1) {
		this.distance1 = distance1;
	}
	
	public void setDistance2(float distance2) {
		this.distance2 = distance2;
	}
	
	public int getIconPosition() {
		return iconPosition;
	}
	
	public void setIconPosition(int iconPosition) {
		this.iconPosition = iconPosition;
		try {
			entitys.TacIconPainter.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		icon = new TextureRegion(entitys.TacIconPainter.tacIconTex,
				(iconPosition%20)*25, (iconPosition/20)*25, 25, 25);
	}
	
	public TextureRegion getIcon() {
		return icon;
	}
	
	public void debugPrint(){
		debug.Debug.println("Status of EntityModel requested:");
		if(v1 != null)
			debug.Debug.println("-CloseUp:"+v1.size/3+" Triangles, "+distance1+" RD");
		else
			debug.Debug.println("-CloseUp: NULL, "+distance1+" RD");
		if(v2 != null)
			debug.Debug.println("-Far    :"+v2.size/3+" Triangles, "+distance2+" RD");
		else
			debug.Debug.println("-Far    : NULL, "+distance2+" RD");
		if(v3 != null)
			debug.Debug.println("-Always :"+v3.size/3+" Triangles");
		else
			debug.Debug.println("-Always : NULL");
	}
	
}
