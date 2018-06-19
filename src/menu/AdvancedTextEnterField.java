package menu;

import org.lwjgl.input.Keyboard;

import utility.InputEvent;
import utility.InputListenerKey;

public abstract class AdvancedTextEnterField implements InputListenerKey {

	public String text = "";
	public int tebpos = 0;
	
	public static final int BUTTON_DOWN = 0x32;
	public static final int BUTTON_UP = 0x35;
	public static final int BUTTON_B_DOWN = 0x42;
	public static final int BUTTON_B_UP = 0x45;
	public static final int BUTTON_CTRL_SPACE = 0xfa;
	public static final int BUTTON_ENTER = 0xff;
	
	public AdvancedTextEnterField() {
		
	}
	
	public void keyPressed(InputEvent e) {
		
	}

	public void keyReleased(InputEvent e) {
		
	}

	public void keyTyped(InputEvent e) {
		if(e.isControlDown)
			return;
		char c = e.keyChar;
		int i = e.keyID;
		if(i == Keyboard.KEY_LEFT){
			tebpos--;
			if(tebpos<0) tebpos = 0;
		}
		else if(i == Keyboard.KEY_RIGHT){
			tebpos++;
			if(tebpos>text.length()) tebpos = text.length();
		}
		else if(i == Keyboard.KEY_PRIOR)
			specialKey(BUTTON_B_UP);
		else if(i == Keyboard.KEY_UP)
			specialKey(BUTTON_UP);
		else if(i == Keyboard.KEY_NEXT)
			specialKey(BUTTON_B_DOWN);
		else if(i == Keyboard.KEY_DOWN)
			specialKey(BUTTON_DOWN);
		else if(i == Keyboard.KEY_SPACE && e.isControlDown)
			specialKey(BUTTON_CTRL_SPACE);
		else if(isSpecialChar(c))
			return;
		else if(i == Keyboard.KEY_RETURN){
			specialKey(BUTTON_ENTER);
		}else if(c == Keyboard.KEY_DELETE){
			if(tebpos < text.length()){
				text = text.substring(0, tebpos)+text.substring(tebpos+1);
			}
		}else if(i == Keyboard.KEY_BACK){
			if(tebpos > 0){
				tebpos--;
				text = text.substring(0, tebpos)+text.substring(tebpos+1);
			}
		}else if(c>0 && c<=255){
			text = text.substring(0, tebpos)+c+text.substring(tebpos);
			tebpos++;
		}
	}

	protected abstract void specialKey(int id);
	
	protected abstract boolean isSpecialChar(char c);
}
