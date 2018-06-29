package utility;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import main.DisplayHandler;
import main.GameController;
import main.InputHandler;
import window.interfaces.IWindow;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

public class Window {
	private InputHandler input;
	private DisplayHandler display;
	private long window;
	
	// TODO: Make it into an event for loop in gamecontroller instead of sticky flag
	private boolean wasResiszed;
	private ArrayList<IWindow> listner = new ArrayList<IWindow>();
	
	// TODO: Better throws
	public Window() throws Exception {		
		input = new InputHandler(this);
		display = new DisplayHandler(this);
	}
	
	private void init() {
		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_API);
		glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_NATIVE_CONTEXT_API);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // Minimum Major version
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3); // Minimum Minor version
		glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE); // For debugging remove for Release
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // For explicit Core, Removal of depreaced

		// Create the window
		window = glfwCreateWindow(300, 300, "Engine Start", NULL, NULL);
		if ( window == NULL ) {
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, GLFWKeyCallback.create((window, key, scancode, action, mods) -> {
			this.input.press(window, key, scancode, action, mods);
		}));
		glfwSetMouseButtonCallback(window, GLFWMouseButtonCallback.create((window, button, action, mods) -> {
			this.input.click();
		}));
		// custom
		glfwSetErrorCallback((error, description) -> {
			debug.Debug.println("GLFW error [" + Integer.toHexString(error) + "]: " + GLFWErrorCallback.getDescription(description));
			//System.err.println("");
		});
		glfwSetWindowSizeCallback(window, GLFWWindowSizeCallback.create((window, width, height) -> {
			debug.Debug.println("Window size is now: " + width + "x" + height + "!");
			wasResiszed = true;
			for( int i = 0; i < listner.size(); ++i) {
				listner.get(i).resize(width, height);
			}
		}));

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
		} // the stack frame is popped automatically
		
	}

	public void close() {
		
	}
	
	public void update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	public void setResizable(boolean b) {
		
	}

	public void setVSyncEnabled(boolean b) {
		glfwSwapInterval(b ? 1 : 0);
	}

	public void create() {
		init();
		
		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		
		// Enable v-sync
		glfwSwapInterval(1);
		
		// Make the window visible
		glfwShowWindow(window);
	}

	public void contextCurrent() {
		// Make the OpenGL co// TODO Auto-generated method stubntext current
		glfwMakeContextCurrent(window);
	}
	
	public void setTitle(String title) {
		glfwSetWindowTitle(window, title);
	}

	public boolean wasResized() {
		boolean result = wasResiszed;
		if(wasResiszed) {
			wasResiszed = false;
		}
		return result;
	}

	public boolean isCloseRequested() {
		return glfwWindowShouldClose(window);
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public int getWidth() {
		IntBuffer width = IntBuffer.allocate(1);
		IntBuffer height = IntBuffer.allocate(1);
		glfwGetWindowSize(window, width, height);
		return width.get(0);
	}

	public int getHeight() {
		IntBuffer width = IntBuffer.allocate(1);
		IntBuffer height = IntBuffer.allocate(1);
		glfwGetWindowSize(window, width, height);
		return height.get(0);
	}

	public void setSize(int width, int height) {
		glfwSetWindowSize(window, width, height);
	}

	public void setFullscreen(boolean fullscreen) {
		
	}

	public DisplayHandler getDisplayHandler() {
		return display;
	}

	public InputHandler getInputHandler() {
		return input;
	}
	
	public void setCursorPosition(int x, int i) {
		// TODO Auto-generated method stub	
	}

	public void registerListner(IWindow window) {
		if(listner.contains(window) == false) {
			listner.add(window);
		}
	}
}
