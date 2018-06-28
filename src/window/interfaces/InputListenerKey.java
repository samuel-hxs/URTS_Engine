package window.interfaces;

import utility.InputEvent;

public interface InputListenerKey {
	public void keyPressed(InputEvent e);
	public void keyReleased(InputEvent e);
	public void keyTyped(InputEvent e);
}
