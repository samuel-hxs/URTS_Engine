package main;

import logic.CameraHandler;
import mdesl.graphics.SpriteBatch;
import mdesl.graphics.TextureRegion;

public class HigherLevelInput {

	private TextureRegion mouse;
	private final InputHandler iph;
	private final CameraHandler camera;
	
	private boolean paintMouse = true;
	
	private boolean mmb;
	private boolean freeCam;
	private int mx;
	private int my;
	
	public HigherLevelInput(InputHandler i, CameraHandler ch){
		mouse = new TextureRegion(menu.FontRenderer.tex, 0, 993, 21, 31);
		iph = i;
		camera = ch;
	}
	
	public void update(){
		camera.scroll(iph.mouseScr);
		
		if(iph.mouseMP){//TODO && Settings.actionMMB == MOVE_CAMERA
			freeCam = false;
			mmb = true;
			mx = iph.mouseX;
			my = iph.mouseY;
		}
		
		if(iph.getCurrentInput().contains(" ")){
			freeCam = true;
			mmb = false;
			mx = iph.mouseX;
			my = iph.mouseY;
		}
		
		camera.mouseX = iph.mouseRelX();
		camera.mouseY = iph.mouseRelY();
	}
	
	public void updateAlways(){
		paintMouse = true;
		if(mmb){
			if(iph.mouseMR){
				mmb = false;
			}else{
				paintMouse = false;
				camera.move((mx-iph.mouseX)*Settings.moveSpeedMMB, (iph.mouseY-my)*Settings.moveSpeedMMB);
				iph.setMouseTo(mx, my);
			}
		}
		
		String s = iph.getCurrentInput();
		if(s.contains("e"))
			camera.singleScroll(true);
		if(s.contains("q"))
			camera.singleScroll(false);
		
		if(s.contains("w"))
			camera.move(0, Settings.moveSpeedWASD*GameControle.timePassed);
		if(s.contains("s"))
			camera.move(0, -Settings.moveSpeedWASD*GameControle.timePassed);
		if(s.contains("d"))
			camera.move(Settings.moveSpeedWASD*GameControle.timePassed, 0);
		if(s.contains("a"))
			camera.move(-Settings.moveSpeedWASD*GameControle.timePassed, 0);
		if(s.contains("y"))
			camera.moveRot(0, 0, -Settings.moveSpeedWASD*GameControle.timePassed);
		if(s.contains("x"))
			camera.moveRot(0, 0, Settings.moveSpeedWASD*GameControle.timePassed);
		
		if(freeCam){
			if(s.contains(" ")){
				camera.moveRotFC((my-iph.mouseY)*0.05f, (mx-iph.mouseX)*0.05f);
				iph.setMouseTo(mx, my);
				paintMouse = false;
			}else{
				freeCam = false;
				camera.setRotFC(0, 0);
			}
		}
		
		if(camera.mouseX<Settings.moveMouseOnEdgeThreshhold){
			float q = camera.mouseX / Settings.moveMouseOnEdgeThreshhold;
			camera.move(-Settings.moveSpeedWASD*(1f-q)*GameControle.timePassed, 0);
		}
		if(camera.mouseX>1f-Settings.moveMouseOnEdgeThreshhold){
			float q = (1f-camera.mouseX) / (Settings.moveMouseOnEdgeThreshhold);
			camera.move(Settings.moveSpeedWASD*(1f-q)*GameControle.timePassed, 0);
		}
		if(camera.mouseY<Settings.moveMouseOnEdgeThreshhold){
			float q = camera.mouseY / Settings.moveMouseOnEdgeThreshhold;
			camera.move(0, Settings.moveSpeedWASD*(1f-q)*GameControle.timePassed);
		}
		if(camera.mouseY>1f-Settings.moveMouseOnEdgeThreshhold){
			float q = (1f-camera.mouseY) / (Settings.moveMouseOnEdgeThreshhold);
			camera.move(0, -Settings.moveSpeedWASD*(1f-q)*GameControle.timePassed);
		}
	}
	
	public void paint(SpriteBatch sp){
		if(!paintMouse)
			return;
		
		sp.draw(mouse, iph.mouseX, iph.mouseY);
	}
}
