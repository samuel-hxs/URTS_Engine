package main;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import utility.InputEvent;
import utility.InputListenerKey;

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
	
	private boolean isShiftDown;
	private boolean isContoleDown;
	private boolean isAltDown;
	
	public static InputListenerKey listener;
	
	public InputHandler() throws LWJGLException{
		mouseButtons = new boolean[10];
		currentlyPressed = new InputChar[20];
		
		Keyboard.enableRepeatEvents(true);
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
		
		if(k == Keyboard.KEY_LCONTROL || k == Keyboard.KEY_RCONTROL) isContoleDown = true;
		if(k == Keyboard.KEY_LMENU || k == Keyboard.KEY_RMENU) isAltDown = true;
		if(k == Keyboard.KEY_LSHIFT || k == Keyboard.KEY_RSHIFT) isShiftDown = true;
		
		if(k == Keyboard.KEY_F12){
			Settings.debugOnScreen = !Settings.debugOnScreen;
		}
		if(k == Keyboard.KEY_F11){
			Settings.debugComplex = (Settings.debugComplex+1)%4;
		}
		if(k == Keyboard.KEY_F9){
			Settings.debugOnScreenZoom = !Settings.debugOnScreenZoom;
		}
		
		InputEvent e = new InputEvent(k, c, isContoleDown, isShiftDown, isAltDown);
		
		if(listener != null){
			listener.keyTyped(e);
			if(listener != null)
				listener.keyPressed(e);
			
			return;
		}else{
			keyChars+=c;
		}
		
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
		if(k == Keyboard.KEY_LCONTROL || k == Keyboard.KEY_RCONTROL) isContoleDown = false;
		if(k == Keyboard.KEY_LMENU || k == Keyboard.KEY_RMENU) isAltDown = false;
		if(k == Keyboard.KEY_LSHIFT || k == Keyboard.KEY_RSHIFT) isShiftDown = false;
		
		if(listener != null) listener.keyReleased(new InputEvent(k, c, isContoleDown, isShiftDown, isAltDown));
		
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
