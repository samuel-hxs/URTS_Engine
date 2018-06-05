package debug;

import java.awt.event.KeyEvent;

public class KeyListener implements java.awt.event.KeyListener{

	public char[] chars;
	
	private String input;
	
	private String keychain;
	
	private boolean enter;
	
	public boolean requestLock;
	public boolean requestTerminal;
	
	public static java.awt.event.KeyListener forwardKey;
	
	//public BottomMenu fMenus;
	
	public KeyListener() {
		chars = new char[20];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = '\n';
		}
		input="";
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.isShiftDown() && e.isControlDown() && e.getKeyCode() == 76){
			requestLock = true;
			return;
		}
		if(e.isControlDown() && e.getKeyCode() == 84){
			requestTerminal = true;
			return;
		}
		if(e.getKeyCode()>=112){
			//if(fMenus != null){
				if(e.getKeyCode()<121){
				//	fMenus.fKeyPressed(e.getKeyCode()-111);
				}
			//}
		}
		if(e.isControlDown()){
			int f = e.getKeyCode();
			//if(f>=49 && f<=57 && fMenus!=null){
				//fMenus.fKeyPressed(e.getKeyCode()-48);
			//}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			//TODO interupt
		}
		if(forwardKey != null){
			forwardKey.keyPressed(e);
			return;
		}
		
		char c = e.getKeyChar();
		for (int i = 0; i < chars.length; i++) {
			if(chars[i] == c)return;
		}
		for (int i = 0; i < chars.length; i++) {
			if(chars[i] == '\n'){
				chars[i] = c;
				input += c;
				return;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(forwardKey != null){
			forwardKey.keyReleased(e);
			return;
		}
		char c = e.getKeyChar();
		for (int i = 0; i < chars.length; i++) {
			if(chars[i] == c){
				chars[i] = '\n';
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(forwardKey != null){
			forwardKey.keyTyped(e);
			return;
		}
		if(e.getKeyChar() == '\n'){
			enter = true;
			return;
		}
		if(keychain == null){
			return;
		}
		if((int)e.getKeyChar() == 8){
			if(keychain.length()>=1)
				keychain = keychain.substring(0, keychain.length()-1);
		}else{
			keychain += e.getKeyChar();
		}
	}
	
	public String getKeys(){
		String s = "";
		for (int i = 0; i < chars.length; i++) {
			if(chars[i] != '\n')s+=chars[i];
		}
		return s;
	}
	
	public String getInput(){
		String s = new String(input);
		input = "";
		return s;
	}
	
	public String getKeyChain(){
		String s = "";
		return s+keychain;
	}
	
	public boolean isEnter(){
		return enter;
	}
	
	public void deletInput(){
		keychain = "";
		enter = false;
	}
	
	public void setText(String s){
		keychain = s;
	}

}
