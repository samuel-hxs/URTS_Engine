package main;

import gui.GuiControle;
import logic.CameraHandler;
import main.grphics.Render3D;
import mdesl.graphics.Color;
import mdesl.graphics.SpriteBatch;
import menu.FontRenderer;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import debug.Timing;
import entitys.EntityControle;
import entitys.EntityThreadTimer;
import entitys.EntityTickUpdate;
import area.AreaControle;
import area.AreaPainter;

public class GameControle implements Runnable{

	private DisplayHandler display;
	private InputHandler input;
	
	private SpriteBatch spriteBatch;
	private Render3D render3d;
	private CameraHandler cameraHandler;
	
	private GuiControle gui;
	private HigherLevelInput hli;
	
	private FontRenderer font14;
	
	public static int timePassed;
	private long lastTime;
	private Runtime runtime;
	
	private EntityControle entitys;
	
	private AreaControle area;
	
	private static int mapSize = 200;
	
	public GameControle() throws Exception{
		input = new InputHandler();
		display = new DisplayHandler(){
			@Override
			protected void wasResized(int w, int h) {
				if(spriteBatch != null)
					spriteBatch.resize(w, h);
				input.setDispSize(w, h);
			}
		};
		runtime = Runtime.getRuntime();
		
		FontRenderer.init();
		font14 = FontRenderer.getFont("MONO_14");
		
		GL11.glClearColor(0.0f,  0.0f, 0.2f, 1);
		GL11.glClearDepth(1.0f);
		
		entitys = new EntityControle();
		
		area = new AreaControle();
		
		cameraHandler = new CameraHandler(area);
		render3d = new Render3D(cameraHandler, entitys, area);
		spriteBatch = render3d;
		
		display.setSize(Settings.displWith, Settings.displHeight, !true);
		Mouse.create();
		Mouse.setGrabbed(true);
		
		PicLoader.pic = new PicLoader("res/ima/gui/gui");
		
		gui = new GuiControle();
		hli = new HigherLevelInput(input, cameraHandler);
		
		////////////TEST
		gui.addMenu(new editor.MeshEditor());
		
	}
	
	public void startLoop(){
		lastTime = System.currentTimeMillis();
		run();
	}

	@Override
	public void run() {
		while(!input.escPressed){
			timePassed = (int)(System.currentTimeMillis()-lastTime);
			lastTime = System.currentTimeMillis();
			
			long t = System.nanoTime();
			
			entitys.reset();
			entitys.startUpdater();
			
			display.loop();
			
			input.loop();
			gui.update(input, hli);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			spriteBatch.begin();
			
			/*String inp = input.getCurrentInput();
			if(inp.contains("w"))
				render3d.test3(0, 0, -1);
			if(inp.contains("s"))
				render3d.test3(0, 0, 1);
			if(inp.contains("a"))
				render3d.test3(-1, 0, 0);
			if(inp.contains("d"))
				render3d.test3(1, 0, 0);
			if(inp.contains("e"))
				render3d.test3(0, -1, 0);
			if(inp.contains("q"))
				render3d.test3(0, 1, 0);*/
			
			cameraHandler.sync();
			render3d.render3D();
			
			spriteBatch.setShader(null);
			gui.draw(spriteBatch);
			
			spriteBatch.setColor(Color.WHITE);
			hli.paint(spriteBatch);
			String fps = "FPS "+Timing.getFps()[1];
			if(fps.length()>8)fps = fps.substring(0,8);
			font14.render(spriteBatch, fps+" V:"+debug.FrameStatistics.drawMesh+
					" E:"+debug.FrameStatistics.entitysPainted+" F:"+debug.FrameStatistics.entityFOW, 3, 14);
			if(Settings.debugOnScreen){
				font14.render(spriteBatch, "RAM: "+generateRAM(), 3, 24);
				font14.render(spriteBatch, "Entity-Threads: "+EntityThreadTimer.t, 3, 34);
				font14.render(spriteBatch, "LTU: "+EntityTickUpdate.lastTime()+"ms", 3, 44);
			}
			spriteBatch.end();
			debug.Timing.markFpsTh(System.nanoTime()-t);
			
			Display.update();
			
			Display.sync(60);//TODO FPS
			debug.Timing.markFps(System.nanoTime()-t);
			
			input.keyChars = "";
			debug.FrameStatistics.clear();
		}
	}
	
	public static int getMapSize(){
		return mapSize;
	}
	
	private static String generateRAM()
	{
	    long RAM_TOTAL = Runtime.getRuntime().totalMemory();
	    long RAM_FREE = Runtime.getRuntime().freeMemory();
	    long RAM_MAX = Runtime.getRuntime().maxMemory();
	    
	    RAM_TOTAL = RAM_TOTAL / 1024 /1024;
	    RAM_FREE = RAM_FREE / 1024 /1024;
	    RAM_MAX = RAM_MAX / 1024 /1024;
	    
	    double percent = (1.0 * RAM_TOTAL-RAM_FREE) / (1.0 * RAM_MAX) * 100;
	    return "Alloc:"+RAM_TOTAL+"MB / Max:"+RAM_MAX+"MB / "+(int)percent+"."+(int)(percent+10)%10+"%";
	}
}
