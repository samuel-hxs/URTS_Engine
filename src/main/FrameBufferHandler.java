package main;

import org.lwjgl.opengl.GL11;

import mdesl.graphics.ITexture;
import mdesl.graphics.SpriteBatch;
import mdesl.graphics.glutils.FrameBuffer;

public class FrameBufferHandler {

	private FrameBuffer f1;
	private FrameBuffer f2;
	
	public FrameBufferHandler() throws Exception {
		resize(10, 10);
	}
	
	public void resize(int w, int h) throws Exception {
		if(f1 != null){
			f1.dispose();
			f2.dispose();
		}
		
		f1 = new FrameBuffer(w, h);
		f2 = new FrameBuffer(w, h);
	}
	
	public void startFrame(){
		f1.begin();
	}
	
	public void captureFrame(SpriteBatch sp){
		GameController.performanceGPU.markCPU_done();
		f1.end();
		sp.setShader(null);
		sp.updateUniforms();
		f2.begin();
		GameController.performanceGPU.markBUS_done();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		sp.draw(f1, 0, 0);
		sp.flush();
		f2.end();
		f1.begin();
		GameController.performanceGPU.markBUS_done();
	}
	
	public ITexture getCapture(){
		return f2;
	}
	
	public void drawMain(SpriteBatch sp){
		f1.end();
		sp.setShader(null);
		sp.updateUniforms();
		sp.draw(f1, 0, 0);
		sp.flush();
	}
}
