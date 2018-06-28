package area;

import org.joml.Vector3f;

import main.grphics.Render3D;
import mdesl.graphics.Texture;
import mdesl.graphics.TextureRegion;
import mdesl.graphics.glutils.VertexArray;

public class AreaRender {
	
	public final int xPos;
	public final int yPos;
	
	public static final int size_raw = 20;
	public final int size;
	private final int superResolution;
	
	private VertexArray data;
	
	private final AreaControle area;
	private Texture tex;
	
	public AreaRender(int x, int y, AreaControle a, int sp){
		size = sp*size_raw;
		data = new VertexArray(size*size*6, LandscapeShader.ATTRIBUTES);
		xPos = x;
		yPos = y;
		area = a;
		superResolution = sp;
	}
	
	public void update(Texture tr){
		tex = tr;
		final float resol = area.getVertexResolution()/(float)superResolution;
		
		float[] x = new float[4];
		float[] y = new float[4];
		float[] z = new float[4];
		Vector3f w = new Vector3f();
		
		data.clear();
		data.setCount(0);
		
		int xP = xPos*superResolution;
		int yP = yPos*superResolution;
		
		float[][] t = new float[4][3];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				x[0] = x[2] = resol*(xP+i-size/2);
				x[1] = x[3] = resol*(xP+i-size/2+1);
				y[0] = y[1] = resol*(yP+j-size/2);
				y[2] = y[3] = resol*(yP+j-size/2+1);
				z[0] = area.getNodeHeight(resol*(xP+i-size/2), resol*(yP+j-size/2));
				z[1] = area.getNodeHeight(resol*(xP+1+i-size/2), resol*(yP+j-size/2));
				z[2] = area.getNodeHeight(resol*(xP+i-size/2), resol*(yP+1+j-size/2));
				z[3] = area.getNodeHeight(resol*(xP+1+i-size/2), resol*(yP+1+j-size/2));
				
				w = getNormal(xP+i-size/2, yP+j-size/2);
				
				//Texture-Positions
				float u = resol*(xP+i-size/2)*area.textureRepeat;
				float u2 = resol*(xP+1+i-size/2)*area.textureRepeat;
				float v = resol*(yP+j-size/2)*area.textureRepeat;
				float v2 = resol*(yP+1+j-size/2)*area.textureRepeat;
				
				for (int k = 0; k < 3; k++) {
					t[0][k] = area.getTextureComponent(resol*(xP+i-size/2), resol*(yP+j-size/2), k);
					t[1][k] = area.getTextureComponent(resol*(xP+1+i-size/2), resol*(yP+j-size/2), k);
					t[2][k] = area.getTextureComponent(resol*(xP+i-size/2), resol*(yP+1+j-size/2), k);
					t[3][k] = area.getTextureComponent(resol*(xP+1+i-size/2), resol*(yP+1+j-size/2), k);
				}
				
				vertexPoint(0, x, y, z, w, u, v, t);
				vertexPoint(1, x, y, z, w, u2, v, t);
				vertexPoint(2, x, y, z, w, u, v2, t);
				
				vertexPoint(1, x, y, z, w, u2, v, t);
				vertexPoint(3, x, y, z, w, u2, v2, t);
				vertexPoint(2, x, y, z, w, u, v2, t);
			}
		}
		
		data.flip();
	}
	
	private void vertexPoint(int p, float[] x, float[] y, float[] z, Vector3f normal, float u, float v, float[][] t){
		data.put(x[p]).put(y[p]).put(z[p]).put(t[p][0]).put(t[p][1]).put(t[p][2]).put(u).put(v).put(normal.x).put(normal.y).put(normal.z);
		data.countIncr();
	}
	
	private Vector3f getNormal(int x, int y){
		final float resol = area.getVertexResolution()/superResolution;
		
		Vector3f[] v = new Vector3f[4];
		v[0] = new Vector3f(x*resol, y*resol, area.getNodeHeight(x*resol, y*resol));
		v[1] = new Vector3f((x+1)*resol, y*resol, area.getNodeHeight((x+1)*resol, y*resol));
		v[2] = new Vector3f(x*resol, (y+1)*resol, area.getNodeHeight(x*resol, (y+1)*resol));
		v[3] = new Vector3f((x+1)*resol, (y+1)*resol, area.getNodeHeight((x+1)*resol, (y+1)*resol));
		
		Vector3f r = getNormal(new Vector3f(), v);
		v[0] = v[3];
		r.add(getNormal(new Vector3f(), v).mul(-1));
		return r.normalize();
	}
	
	public static Vector3f v = new Vector3f();
	public static Vector3f u = new Vector3f();
	public static Vector3f getNormal(Vector3f res, Vector3f[] edges){
		u.set(edges[1]);
		u.sub(edges[0]);
		v.set(edges[2]);
		v.sub(edges[0]);
		
		res.x = (u.y * v.z) - (u.z * v.y);
		res.y = (u.z * v.x) - (u.x * v.z);
		res.z = (u.x * v.y) - (u.y * v.x);
		
		return res;
	}
	
	public void render(Render3D r3d){
		r3d.render(data, tex);
		debug.FrameStatistics.drawMesh+=superResolution*superResolution;
	}
	
	

}
