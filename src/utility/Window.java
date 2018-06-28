package utility;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import main.DisplayHandler;
import main.InputHandler;

import org.lwjgl.glfw.*;
import org.lwjgl.system.*;

import java.nio.*;

public class Window {
	private InputHandler input;
	private DisplayHandler display;
	private long window;
	
	// TODO: Better throws
	public Window() throws Exception {
// TODO FPS
//		Display.sync(60);
		
		input = new InputHandler(this);
		display = new DisplayHandler(this);
		
// TODO: How to represent?
//		{
//			@Override
//			protected void wasResized(int w, int h) {
//				if(spriteBatch != null)
//					spriteBatch.resize(w, h);
//				input.setDispSize(w, h);
//			}
//		};
	}
	
	private void initGLFW() {
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
		glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_NATIVE_CONTEXT_API);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

		// Create the window
		window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
		if ( window == NULL ) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automaticall
	}

	public void close() {
		
	}
	
	public void update() {
		glfwSwapBuffers(window);
	}

	public void setResizable(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void setVSyncEnabled(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void create() {
		initGLFW();
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);
		
		// Make the window visible
		glfwShowWindow(window);
	}

	public void contextCurrent() {
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
	}
	
	public void setTitle(String string) {
		// TODO Auto-generated method stub
		
	}

	public boolean wasResized() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isCloseRequested() {
		// TODO Auto-generated method stub
		return false;
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void setFullscreen(boolean fullscreen) {
		// TODO Auto-generated method stub
		
	}

	public DisplayHandler getDisplayHandler() {
		return display;
	}

	public InputHandler getInputHandler() {
		return input;
	}
}
