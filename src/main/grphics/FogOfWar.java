package main.grphics;

import java.util.Arrays;
import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import entitys.CollisionFreeEntityIterator;
import entitys.Entity;
import entitys.EntityList;
import mdesl.graphics.Texture;
import mdesl.graphics.glutils.ShaderProgram;
import mdesl.graphics.glutils.VertexArray;
import mdesl.graphics.glutils.VertexAttrib;

public class FogOfWar {

	public static final String U_TEXTURE = "u_texture";
	public static final String U_PROJ_VIEW = "u_projView";
	public static final String U_OFFSET_MAT = "u_offMat";
	public static final String U_FOW_COLOR = "u_fow_color";
	public static final String U_MODE = "u_mode";
	public static final String U_RANDOM = "u_random";

	public static final String ATTR_POSITION = "Position";
	public static final String ATTR_TEXCOORD = "TexCoord";
	
	private static final float ZERO_PLAIN = 0;
	private float addDepth;
	
	public static final List<VertexAttrib> ATTRIBUTES = Arrays.asList(
			new VertexAttrib(0, ATTR_POSITION, 3), new VertexAttrib(1, ATTR_TEXCOORD, 2));
	
	public ShaderProgram fowShader;
	private VertexArray buffer;
	private Texture tex;
	
	public static boolean renderFogOfWar = true;
	
	public FogOfWar() throws Exception{
		fowShader = new ShaderProgram("res/sha/fow", ATTRIBUTES);
		buffer = new VertexArray(6000, ATTRIBUTES);
		tex = new Texture(utility.ResourceLoader.loadResource("res/ima/map/fow.png"));
	}
	
	public void renderFOW(Render3D r3d, CollisionFreeEntityIterator c){
		if(!renderFogOfWar)
			return;
		r3d.setShader(fowShader, false);
		fowShader.setUniformf(U_RANDOM, (float)Math.random());
		r3d.updateProjectionView();
		
		addDepth = 0;
		
		fowShader.setUniformf(U_FOW_COLOR, new Vector4f(0, 0, 0, 0.6f));
		fowShader.setUniformMatrix(U_OFFSET_MAT, false, new Matrix4f());
		
		//Rendering Light-Spots
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		//Clear Depth-Buffer
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		
		buffer.clear();
		buffer.setCount(0);
		fowShader.setUniformf(U_MODE, 1);
		
		for(; c.hasNext();){
			singleBlock(c.next(), r3d);
		}
		
		if(buffer.getCount()>0){
			buffer.flip();
			r3d.render(buffer, tex);
			buffer.clear();
			buffer.setCount(0);
		}
		
		drawArea(0, 0, (0.5f*main.GameControle.getMapSize())*1.1f, ZERO_PLAIN-6);
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		fowShader.setUniformf(U_MODE, 0);
		buffer.flip();
		r3d.render(buffer, tex);
	}
	
	private void singleBlock(EntityList en, Render3D r3d){
		if(addDepth<5)
			addDepth+=0.025f;
		if(buffer.getCount()+EntityList.NUMBER_OF_UNITS*6>=buffer.getMaximumVertices()){
			buffer.flip();
			r3d.render(buffer, tex);
			buffer.clear();
			buffer.setCount(0);
		}
		for (Entity e : en.list) {
			if(!e.renderFow || e.renderFowSkip)continue;
			
			drawArea(e.xPos, e.yPos, e.fowRadius, ZERO_PLAIN-addDepth);
			debug.FrameStatistics.entityFOW++;
		}
	}
	
	private void drawArea(float atX, float atY, float rad, float zPlain){
		paint(atX-rad, atY-rad, zPlain, -1, -1);
		paint(atX+rad, atY-rad, zPlain, 1, -1);
		paint(atX-rad, atY+rad, zPlain, -1, 1);
		paint(atX+rad, atY-rad, zPlain, 1, -1);
		paint(atX+rad, atY+rad, zPlain, 1, 1);
		paint(atX-rad, atY+rad, zPlain, -1, 1);
	}
	
	private void paint(float x, float y, float z, int u, int v){
		buffer.put(x).put(y).put(z).put(u).put(v);
		buffer.countIncr();
	}
}
