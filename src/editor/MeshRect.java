package editor;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import mdesl.graphics.TextureRegion;

public class MeshRect {

	public Vector3f[] point;
	public Vector2f[] texture;
	public Vector4f[] color;
	private float pixelDividerX = 1.0f;
	private float pixelDividerY = 1.0f;
	
	public final int id;
	
	public MeshRect(int i){
		id = i;
		point = new Vector3f[4];
		texture = new Vector2f[4];
		color = new Vector4f[4];
		for (int j = 0; j < color.length; j++) {
			color[j] = new Vector4f(1,1,1,1);
			texture[j] = new Vector2f();
			point[j] = new Vector3f();
		}
	}
	
	public void setBaseTextureRegion(TextureRegion tr){
		pixelDividerX = 1.0f/tr.getTexture().getWidth();
		pixelDividerY = 1.0f/tr.getTexture().getHeight();
		
		texture[0].x = texture[1].x = tr.getU();
		texture[2].x = texture[3].x = tr.getU2();
		texture[0].y = texture[2].y = tr.getV();
		texture[1].y = texture[3].y = tr.getV2();
	}
	
	public void movePoint(int id, float x, float y, float z){
		point[id].x += x;
		point[id].y += y;
		point[id].z += z;
	}
	
	public void setPoint(int id, float x, float y, float z){
		point[id].x = x;
		point[id].y = y;
		point[id].z = z;
	}
	
	public void moveEdge(int id, float x, float y, float z){
		movePoint(id, x, y, z);
		movePoint((id+1)%4, x, y, z);
	}
	
	public void movePlane(float x, float y, float z){
		for (int i = 0; i < color.length; i++) {
			movePoint(i, x, y, z);
		}
	}
	
	public void moveTexturePoint(int id, float x, float y){
		texture[id].x += x*pixelDividerX;
		texture[id].y += y*pixelDividerY;
	}
	
	public void moveTextureEdge(int id, float x, float y){
		moveTexturePoint(id, x, y);
		moveTexturePoint((id+1)%4, x, y);
	}
	
	public void moveTexturePlane(float x, float y){
		for (int i = 0; i < color.length; i++) {
			moveTexturePoint(i, x, y);
		}
	}
}
