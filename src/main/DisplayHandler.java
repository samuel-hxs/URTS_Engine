package main;

import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.glfw.GLFWWindowSizeCallbackI;

import utility.Window;

// TODO: Warum war das hier abstract?
public class DisplayHandler implements GLFWWindowSizeCallbackI {
	private Window window;
	
	// TODO: Specific Exceptions
	public DisplayHandler(Window window) throws Exception {
		this.window = window;
		this.window.create();
		
		this.window.setResizable(true);
		this.window.setVSyncEnabled(true);
		this.window.setTitle("URTS-Prototype v" + Main.VERSION);
	}
	
	public void update() {
		if (window.wasResized()) {
			wasResized();
		}
		
		if(window.isCloseRequested()) {
			//TODO Exactly what is TODO here?
			window.destroy();
			System.exit(0);
		}
	}
	
	public void setSize(int width, int height, boolean fullscreen) {
		window.setSize(width, height);
		window.setFullscreen(fullscreen);
	}
	
	private void wasResized(){
		int width = window.getWidth();
		int height = window.getHeight();
		
		// TODO abstract into own renderer
		glViewport(0, 0, width, height);
		
		//wasResized(width, height);
	}

	@Override
	public void invoke(long window, int width, int height) {
		System.out.println("Window with id '" + window + "' resized to '" + width + "x" + height + "'.");
	}
	
// TODO: Bessere anwendung oder Implementierung
//	protected abstract void wasResized(int w, int h);
	
// TODO: How to represent in GLFW3?
//	private void setDisplayMode(int width, int height, boolean fullscreen) {
//	    if ((window.getDisplayMode().getWidth() == width) && 
//	        (window.getDisplayMode().getHeight() == height) && 
//	    (window.isFullscreen() == fullscreen)) {
//	        return;
//	    }
//	 
//	    try {
//	        DisplayMode targetDisplayMode = null;
//	         
//	    if (fullscreen) {
//	        // DisplayMode[] modes = Display.getAvailableDisplayModes();
//	        int freq = 0;
//	                 
//	        for (int i=0;i<modes.length;i++) {
//	            DisplayMode current = modes[i];
//	           
//	            // TODO: Needed with GLFW3?
////	        if ((current.getWidth() == width) && (current.getHeight() == height)) {
////	            if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
////	                if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
////	                targetDisplayMode = current;
////	                freq = targetDisplayMode.getFrequency();
////	                        }
////	                    }
////	 
////	            // if we've found a match for bpp and frequence against the 
////	            // original display mode then it's probably best to go for this one
////	            // since it's most likely compatible with the monitor
////	            if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
////	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
////	                            targetDisplayMode = current;
////	                            break;
////	                    }
////	                }
////	            }
////	        } else {
////	            targetDisplayMode = new DisplayMode(width,height);
////	        }
//	 
//	        if (targetDisplayMode == null) {
//	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
//	            return;
//	        }
//	 
////	        Display.setDisplayMode(targetDisplayMode);
//	        window.setFullscreen(fullscreen);
//	        
//	        glViewport(0, 0, Display.getWidth(), Display.getHeight());
//	             
//	    } catch (LWJGLException e) {
//	        System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
//	    }
//	}
}
