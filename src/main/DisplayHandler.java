package main;

import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public abstract class DisplayHandler {

	public DisplayHandler() throws LWJGLException{
		Display.setResizable(true);
		Display.setVSyncEnabled(true);
		
		Display.create();
		Display.setTitle("URTS-Prototype v"+Main.VERSION);
	}
	
	public void loop(){
		if (Display.wasResized())
			wasResized();
		
		if(Display.isCloseRequested()){
			//TODO
			Display.destroy();
			System.exit(0);
		}
	}
	
	public void setSize(int width, int height, boolean fullscreen) {
		setDisplayMode(width, height, fullscreen);
	}
	
	private void wasResized(){
		int w = Display.getWidth();
		int h = Display.getHeight();
		glViewport(0, 0, w, h);
		wasResized(w, h);
	}
	
	protected abstract void wasResized(int w, int h);
	
	private void setDisplayMode(int width, int height, boolean fullscreen) {
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
	    (Display.isFullscreen() == fullscreen)) {
	        return;
	    }
	 
	    try {
	        DisplayMode targetDisplayMode = null;
	         
	    if (fullscreen) {
	        DisplayMode[] modes = Display.getAvailableDisplayModes();
	        int freq = 0;
	                 
	        for (int i=0;i<modes.length;i++) {
	            DisplayMode current = modes[i];
	                     
	        if ((current.getWidth() == width) && (current.getHeight() == height)) {
	            if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
	                if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
	                targetDisplayMode = current;
	                freq = targetDisplayMode.getFrequency();
	                        }
	                    }
	 
	            // if we've found a match for bpp and frequence against the 
	            // original display mode then it's probably best to go for this one
	            // since it's most likely compatible with the monitor
	            if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	                            targetDisplayMode = current;
	                            break;
	                    }
	                }
	            }
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }
	 
	        if (targetDisplayMode == null) {
	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }
	 
	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
	        glViewport(0, 0, Display.getWidth(), Display.getHeight());
	             
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	    }
	}
}
