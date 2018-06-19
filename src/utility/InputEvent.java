package utility;

public class InputEvent {

	public final char keyChar;
	public final int keyID;
	
	public final boolean isControlDown;
	public final boolean isShiftDown;
	public final boolean isAltDown;
	
	public InputEvent(int i, char c, boolean ctrl, boolean shift, boolean alt){
		keyChar = c;
		keyID = i;
		isControlDown = ctrl;
		isShiftDown = shift;
		isAltDown = alt;
	}
	
	public String keyName;
}
