package main.grphics;
import org.lwjgl.opengl.GL11;

import area.AreaControle;
import area.AreaImages;
import debug.PerformanceMonitor;
import logic.CameraHandler;
import main.FrameBufferHandler;
import main.PicLoader;
import main.grphics.VertexDataManager.RenderingHints;
import main.grphics.VertexDataManager.SizeOfVertexArray;
import mdesl.graphics.SpriteBatch;
import mdesl.graphics.TextureRegion;
import mdesl.graphics.glutils.ShaderProgram;
import static org.lwjgl.opengl.GL11.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import entitys.EntityControle;
import entitys.TacIconPainter;
import entitys.paint.EntityPainter;

public class Render3D extends SpriteBatch{

	private Projection proj;
	
	private VertexDataManager vdm;
	private final FrameBufferHandler fbh;
	
	private Camera camera;
	private final CameraHandler cameraHandler;
	
	private Matrix4f currWorldTranslate = null;
	private FrustumCullingFilter fcf;
	
	private VertexData3D cursor;
	
	private AreaControle area;
	private FogOfWar fow;
	private EntityControle entitys;
	private TacIconPainter tacPainter;
	
	private EntityPainter entityPainter;
	
	private PerformanceMonitor performance;
	
	public Render3D(CameraHandler c, EntityControle e, AreaControle a, FrameBufferHandler f) throws Exception{
		proj = new Projection();
		camera = new Camera();
		vdm = new VertexDataManager();
		fcf = new FrustumCullingFilter();
		
		fbh = f;
		
		entityPainter = new EntityPainter();
		
		fow = new FogOfWar();
		entitys = e;
		tacPainter = new TacIconPainter();
		cameraHandler = c;
		cameraHandler.link(camera, proj);
		
		area = a;
	}
	
	public void set3Dmode(boolean handle3D){
		flush();
		if(handle3D){
			updateProjectionView();
			fcf.updateFrustum(proj.getProjectionOnly(), camera.getViewMatrix());
			glEnable(GL_DEPTH_TEST);
		}else{
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			updateUniforms();
		}
	}
	
	public void updateProjectionView(){
		getShader().setUniformMatrix(U_PROJ_VIEW, false, proj.getMatrix(camera));
	}
	
	@Override
	public void resize(int width, int height) {
		setAspect(width, height);
		super.resize(width, height);
	}
	
	public void setAspect(int w, int h){
		proj.setAspect(w, h);
	}
	
	public void setTranslation(Matrix4f t){
		if(t == currWorldTranslate)
			return;
		
		if(t == null){
			currWorldTranslate = null;
			program.setUniformMatrix(U_OFFSET_MAT, false, new Matrix4f());
			return;
		}
		
		if(t.equals(currWorldTranslate))
			return;
		
		currWorldTranslate = t;
		program.setUniformMatrix(U_OFFSET_MAT, false, currWorldTranslate);
	}
	
	private boolean init = false;
	
	/**
	 * Will handle all 3D-Rendering
	 */
	public void render3D(){
		if(!init){
			init = true;
			
			mkCursor();
			
			area.prepareMap();
			area.update();
		}
		
		entitys.startFrustum(proj.getProjectionOnly(), camera.getViewMatrix(), camera.pos);
		entitys.startSecondCheck();
		entitys.startProjection(proj.getProjectionOnly(), camera.getViewMatrix());
		
		fbh.startFrame();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		// TODO: For what is this used and how to use it right or replace it?
		//GL11.glEnable(GL11.GL_ALPHA_TEST);
		//GL11.glAlphaFunc(GL_GREATER, 0.1f);
		
		set3Dmode(true);
		area.render(this, fcf);
		performance.mark("R. Area");
		
		entityPainter.render3dUnits(this, entitys.getPaintIterator(false));
		performance.mark("R. Units-3D");
		
		//setShader(null);
		//updateProjectionView();
		//vdm.render(this, false, fcf);
		
		//GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		//vdm.render(this, true, fcf);
		
		fbh.captureFrame(this);
		performance.mark("Swap Capt.");
		draw(fbh.getCapture(), 100, 100, 400, 400);
		GL11.glColorMask(true, true, true, false);
		fow.renderFOW(this, entitys.getPaintIterator(true));
		performance.mark("R. FOW");
		GL11.glColorMask(true, true, true, true);
		
		setShader(null);
		updateProjectionView();
		
		//3D-GUI
		cursor.translate = camera.getPosition();
		cursor.render(this);
		
		performance.mark("R. GUI-3D");
		
		set3Dmode(false);
		
		tacPainter.paintTacIcons(entitys.getLastPaintIterator(), this);
		performance.mark("R. Tac-Icons");
	}
	
	@Override
	public void updateUniforms(ShaderProgram program) {
		super.updateUniforms(program);
		boundTexture = null;
		currWorldTranslate = null;
	}
	
	private void mkCursor(){
		TextureRegion tr = PicLoader.pic.getImage("w");
		cursor = new VertexData3D(100, SizeOfVertexArray.CUSTOM);
		Brush3D b = cursor.getBrush();
		b.start();
		
		cursor.setTexure(tr.getTexture());
		
		b.drawCube(-0.05f, -0.05f, -0.05f, 0.1f, 0.1f, 0.1f, tr);
		b.g = 0;
		b.r = 0;
		b.drawCube(-0.025f, -0.025f, 0.025f, 0.05f, 0.05f, 0.9f, tr);
		b.b = 0.7f;
		b.drawCube(-0.05f, -0.05f, 0.9f, 0.1f, 0.1f, 0.1f, tr);
		b.b = 0;
		b.g = 1;
		b.drawCube(-0.025f, 0.025f, -0.025f, 0.05f, 0.9f, 0.05f, tr);
		b.g = 0.7f;
		b.drawCube(-0.05f, 0.9f, -0.05f, 0.1f, 0.1f, 0.1f, tr);
		b.g = 0;
		b.r = 1;
		b.drawCube(0.025f, -0.025f, -0.025f, 0.9f, 0.05f, 0.05f, tr);
		b.r = 0.7f;
		b.drawCube(0.9f, -0.05f, -0.05f, 0.1f, 0.1f, 0.1f, tr);
		
		b.end();
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public void setPerformance(PerformanceMonitor performance) {
		this.performance = performance;
	}
}
