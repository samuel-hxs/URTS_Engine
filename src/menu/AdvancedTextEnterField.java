package menu;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class AdvancedTextEnterField implements KeyListener{

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
	
	@Override
	public void keyPressed(KeyEvent e) {
		int i = e.getKeyCode();
		if(i == 37){
			tebpos--;
			if(tebpos<0) tebpos = 0;
		}
		if(i == 39){
			tebpos++;
			if(tebpos>text.length()) tebpos = text.length();
		}
		if(i == 33)
			specialKey(BUTTON_B_UP);
		if(i == 38)
			specialKey(BUTTON_UP);
		if(i == 34)
			specialKey(BUTTON_B_DOWN);
		if(i == 40)
			specialKey(BUTTON_DOWN);
		if(i == 32 && e.isControlDown())
			specialKey(BUTTON_CTRL_SPACE);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.isControlDown())
			return;
		char c = e.getKeyChar();
		if(isSpecialChar(c))
			return;
		if(c == '\n'){
			specialKey(BUTTON_ENTER);
		}else if(c == 127){
			if(tebpos < text.length()){
				text = text.substring(0, tebpos)+text.substring(tebpos+1);
			}
		}else if(c == 8){
			if(tebpos > 0){
				tebpos--;
				text = text.substring(0, tebpos)+text.substring(tebpos+1);
			}
		}else{
			text = text.substring(0, tebpos)+c+text.substring(tebpos);
			tebpos++;
		}
	}

	protected abstract void specialKey(int id);
	
	protected abstract boolean isSpecialChar(char c);
}
