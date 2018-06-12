package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputHandler {

	private boolean[] mouseButtons;
	public int mouseX;
	public int mouseY;
	private int dispWidth;
	private int dispHeight;
	public int mouseScr;
	
	public String keyChars = "";
	public InputChar[] currentlyPressed;
	
	public boolean escPressed;
	
	public boolean mouseLP;
	public boolean mouseLR;
	public boolean mouseMP;
	public boolean mouseMR;
	
	public InputHandler() throws LWJGLException{
		mouseButtons = new boolean[10];
		currentlyPressed = new InputChar[20];
	}
	
	public void loop(){
		Keyboard.poll();
		while (Keyboard.next()) {
			char c = Keyboard.getEventCharacter();
			int k = Keyboard.getEventKey();
			String key = Keyboard.getKeyName(k);
			boolean down = Keyboard.getEventKeyState();
			if (down)
				keyPressed(k, c, key);
			else
				keyReleased(k, c, key);
		}
		Mouse.poll();
		while (Mouse.next()) {
			int btn = Mouse.getEventButton();
			boolean btnDown = Mouse.getEventButtonState();
			int wheel = Mouse.getEventDWheel();
			int x = Mouse.getEventX();
			int y = Mouse.getEventY();
			
			if(mouseButtons.length<=btn){
				debug.Debug.println("*Mouse-Button out of range: "+btn, debug.Debug.WARN);
				continue;
			}
			
			if(btn>=0){
				if (btnDown) {
					mouseButtons[btn] = true;
					mousePressed(x, y, btn);
				}else if(mouseButtons[btn]){
					mouseButtons[btn] = false;
					mouseReleased(x, y, btn);
				}
			}
			mouseX = x;
			mouseY = dispHeight-y;
			
			if (wheel!=0) {
				scrolled(wheel);
			}
		}
	}
	
	private void keyPressed(int k, char c, String n){
		if(k == Keyboard.KEY_ESCAPE){
			escPressed = true;
			return;
		}
		if(k == Keyboard.KEY_F12){
			Settings.debugOnScreen = !Settings.debugOnScreen;
		}
		if(k == Keyboard.KEY_F11){
			Settings.debugComplex = !Settings.debugComplex;
		}
		if(k == Keyboard.KEY_F9){
			Settings.debugOnScreenZoom = !Settings.debugOnScreenZoom;
		}
		
		keyChars+=c;
		for (int i = 0; i < currentlyPressed.length; i++) {
			if(currentlyPressed[i] == null){
				currentlyPressed[i] = new InputChar(c, k);
				return;
			}
			if(currentlyPressed[i].id == k)
				return;
		}
	}
	
	private void keyReleased(int k, char c, String n){
		for (int i = 0; i < currentlyPressed.length; i++) {
			if(currentlyPressed[i] == null)
				continue;
			if(currentlyPressed[i].id == k)
				currentlyPressed[i] = null;
		}
	}
	
	private void mousePressed(int x, int y, int btn){
		if(btn == 0)
			mouseLP = true;
		if(btn == 2)
			mouseMP = true;
	}
	
	private void mouseReleased(int x, int y, int btn){
		if(btn == 0)
			mouseLR = true;
		if(btn == 2)
			mouseMR = true;
	}
	
	private void scrolled(int s){
		mouseScr -= s;
	}
	
	public void setDispSize(int w, int h){
		dispHeight = h;
		dispWidth = w;
	}
	
	public String getCurrentInput(){
		String s = "";
		for (int i = 0; i < currentlyPressed.length; i++) {
			if(currentlyPressed[i] == null)
				continue;
			s+=currentlyPressed[i].ch;
		}
		return s;
	}
	
	private class InputChar{
		final char ch;
		final int id;
		
		InputChar(char c, int i) {
			ch = c;
			id = i;
		}
	}
	
	public void setMouseTo(int x, int y){
		Mouse.setCursorPosition(x, dispHeight-y);
		mouseX = x;
		mouseY = y;
	}
	
	public float mouseRelX(){
		return (float)mouseX/dispWidth;
	}
	public float mouseRelY(){
		return (float)mouseY/dispHeight;
	}
}
