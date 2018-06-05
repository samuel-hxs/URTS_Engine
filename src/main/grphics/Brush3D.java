package main.grphics;

import java.util.concurrent.Semaphore;

import mdesl.graphics.ITexture;
import mdesl.graphics.TextureRegion;
import mdesl.graphics.glutils.VertexData;
import utility.GeometryConstants;
import utility.GeometryConstants.FACING;
import static mdesl.graphics.SpriteBatch.*;

public class Brush3D {

	private final Semaphore lock;
	private final VertexData data;
	
	private boolean statusDraw;
	
	public float r = 1;
	public float g = 1;
	public float b = 1;
	public float a = 1;
	
	public Brush3D(Semaphore l, VertexData d){
		lock = l;
		data = d;
	}
	
	/**
	 * Clears all Vertices and prepares the drawing pipeline
	 */
	public void start(){
		if(!statusDraw)
			lock.acquireUninterruptibly();
		data.clear();
		statusDraw = true;
		
		r = b = g = a = 1;
	}
	
	public boolean check(){
		if(!statusDraw)
			throw new IllegalStateException("Status is not Draw");
		
		return data.getCount()+6 <= data.getMaximumVertices();
	}
	
	/**
	 * ready to be rendered again
	 */
	public void end(){
		if(!statusDraw)
			throw new IllegalStateException("Status is not Draw");
		lock.release();
		data.flip();
		statusDraw = false;
	}
	
	public void drawSimplePlane(float x, float y, float z, float w, float h, GeometryConstants.FACING f, ITexture t)
			throws ArrayIndexOutOfBoundsException, IllegalStateException{
		drawSimplePlane(x, y, z, w, h, f, t.getU(), t.getV(), t.getU2(), t.getV2());
	}
	
	public void drawSimplePlane(float x, float y, float z, float w, float h, GeometryConstants.FACING f,
			float u1, float v1, float u2, float v2) throws ArrayIndexOutOfBoundsException, IllegalStateException{
		
		if(!check())
			throw new ArrayIndexOutOfBoundsException("Vertices expandet: "+data.getCount());
		
		switch (f) {
		case FRONT: case BACK:
			// top left, top right, bottom left
			vertex(x, y, z, r, g, b, a, u1, v1, data);
			vertex(x, y+w, z, r, g, b, a, u2, v1, data);
			vertex(x, y, z+h, r, g, b, a, u1, v2, data);

			// top right, bottom right, bottom left
			vertex(x, y+w, z, r, g, b, a, u2, v1, data);
			vertex(x, y+w, z+h, r, g, b, a, u2, v2, data);
			vertex(x, y, z+h, r, g, b, a, u1, v2, data);
			
			break;
		
		case LEFT: case RIGHT:
			// top left, top right, bottom left
			vertex(x, y, z, r, g, b, a, u1, v1, data);
			vertex(x+w, y, z, r, g, b, a, u2, v1, data);
			vertex(x, y, z+h, r, g, b, a, u1, v2, data);

			// top right, bottom right, bottom left
			vertex(x+w, y, z, r, g, b, a, u2, v1, data);
			vertex(x+w, y, z+h, r, g, b, a, u2, v2, data);
			vertex(x, y, z+h, r, g, b, a, u1, v2, data);
			
			break;
			
		case UP: case DOWN:
			// top left, top right, bottom left
			vertex(x, y, z, r, g, b, a, u1, v1, data);
			vertex(x+w, y, z, r, g, b, a, u2, v1, data);
			vertex(x, y+h, z, r, g, b, a, u1, v2, data);

			// top right, bottom right, bottom left
			vertex(x+w, y, z, r, g, b, a, u2, v1, data);
			vertex(x+w, y+h, z, r, g, b, a, u2, v2, data);
			vertex(x, y+h, z, r, g, b, a, u1, v2, data);
			
			break;

		default:
			break;
		}
	}
	
	public void drawCube(float x, float y, float z, float xs, float ys, float zs, TextureRegion tr){
		drawSimplePlane(x, y, z, xs, ys, FACING.UP, tr);
		drawSimplePlane(x, y, z, xs, zs, FACING.LEFT, tr);
		drawSimplePlane(x, y, z, ys, zs, FACING.FRONT, tr);
		
		drawSimplePlane(x, y, z+zs, xs, ys, FACING.UP, tr);
		drawSimplePlane(x, y+ys, z, xs, zs, FACING.LEFT, tr);
		drawSimplePlane(x+xs, y, z, ys, zs, FACING.FRONT, tr);
	}
}
