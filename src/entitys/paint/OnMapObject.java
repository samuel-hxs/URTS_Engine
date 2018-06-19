package entitys.paint;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import area.LandscapeShader;
import main.grphics.Render3D;
import mdesl.graphics.Texture;
import mdesl.graphics.glutils.VertexArray;

public class OnMapObject {

	private VertexArray data;
	
	private Matrix4f m;
	
	private Texture texture;
	
	public String name;
	
	public final int size;
	
	public OnMapObject(int size, Texture t){
		data = new VertexArray(size, LandscapeShader.ATTRIBUTES);
		m = new Matrix4f();
		texture = t;
		
		this.size = size;
	}
	
	public void flip(){
		data.flip();
	}
	
	public void render(Render3D r3d, Vector3f tran, Vector3f rot){
		m.identity();
		m.translate(tran);
		r3d.getShader().setUniformMatrix(LandscapeShader.U_TRANSLATION, false, m);
		r3d.render(data, texture);
	}
	
	public VertexArray getData(){
		return data;
	}
	
	public static OnMapObject getTestCircle(int size){
		mdesl.graphics.TextureRegion t = main.PicLoader.pic.getImage("menuBack");
		OnMapObject o = new OnMapObject(size*size*12, t.getTexture());
		
		Vector3f[] vec = new Vector3f[4];
		float[] u = new float[]{t.getU(),t.getU2(),t.getU(),t.getU2()};
		float[] v = new float[]{t.getV(),t.getV(),t.getV2(),t.getV2()};
		
		final float f = (float)(Math.PI/size);
		for (int i = 0; i < size*2; i++) {
			for (int j = 0; j < size; j++) {
				vec[0] = edge(i, j, f);
				vec[1] = edge(i+1, j, f);
				vec[2] = edge(i, j+1, f);
				vec[3] = edge(i+1, j+1, f);
				
				LandscapeShader.drawPlane(vec, u, v, o.getData());
			}
		}
		o.flip();
		return o;
	}
	
	private static Vector3f edge(int u, int v, float f){
		final float radius = 1;
		Vector3f e = new Vector3f();
		
		e.z = (float)Math.cos(f*v)*radius;
		float r = (float)Math.sin(f*v)*radius;
		e.y = (float)Math.sin(f*u)*r;
		e.x = (float)Math.cos(f*u)*r;
		
		return e;
	}
	
}
