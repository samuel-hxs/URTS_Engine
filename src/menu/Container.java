package menu;

import java.util.ArrayList;
import java.util.List;

import mdesl.graphics.SpriteBatch;

public class Container implements MenuAddable{

	public int xPos;
	public int yPos;
	
	public int xSize = -1;
	public int ySize = -1;
	
	public boolean visible = true;
	
	private List<MenuAddable> buttons;
	private List<MenuAddable> toAdd;
	private List<MenuAddable> toRemove;
	
	public Container(int x, int y) {
		buttons = new ArrayList<>();
		toAdd = new ArrayList<>();
		toRemove = new ArrayList<>();
		xPos = x;
		yPos = y;
	}
	
	@Override
	public boolean leftPressed(int x, int y, boolean onTop) {
		if(!visible)
			return false;
		for (MenuAddable b : buttons) {
			if(b.leftPressed(x-xPos, y-yPos, onTop))
				onTop = false;
		}
		return !onTop;
	}

	@Override
	public boolean leftReleased(int x, int y, boolean onTop) {
		if(!visible)
			return false;
		for (MenuAddable b : buttons) {
			if(b.leftReleased(x-xPos, y-yPos, onTop))
				onTop = false;
		}
		return !onTop;
	}

	@Override
	public boolean mouseMoved(int x, int y, boolean onTop) {
		if(!visible)
			return false;
		for (MenuAddable b : buttons) {
			if(b.mouseMoved(x-xPos, y-yPos, onTop))
				onTop = false;
		}
		
		if(!toAdd.isEmpty()){
			for (MenuAddable m : toAdd) {
				buttons.add(m);
			}
			toAdd.clear();
		}
		if(!toRemove.isEmpty()){
			for (MenuAddable m : toRemove) {
				buttons.remove(m);
			}
			toRemove.clear();
		}
		
		return !onTop;
	}

	@Override
	public void checkScroll(int x, int y, int xScr, int yScr) {
		if(!visible)
			return;
		for (MenuAddable b : buttons) {
			b.checkScroll(x, y, xScr, yScr);
		}
	}

	@Override
	public void draw(SpriteBatch sp, int x, int y) {
		if(!visible)
			return;
		if(xSize>0 || ySize>0)
		{
			SpriteBatch.ClipSet cs = sp.getClip();
			sp.setclip(x+xPos, y+yPos, xSize, ySize);
			for (MenuAddable b : buttons) {
				b.draw(sp, x+xPos, y+yPos);
			}
			cs.resetToThisClip();
		}else{
			for (MenuAddable b : buttons) {
				b.draw(sp, x+xPos, y+yPos);
			}
		}
	}

	@Override
	public void drawMisc(SpriteBatch sp, int x, int y) {
		if(!visible)
			return;
		if(xSize>0 || ySize>0)
		{
			SpriteBatch.ClipSet cs = sp.getClip();
			sp.setclip(x+xPos, y+yPos, xSize, ySize);
			for (MenuAddable b : buttons) {
				b.drawMisc(sp, x+xPos, y+yPos);
			}
			cs.resetToThisClip();
		}else{
			for (MenuAddable b : buttons) {
				b.drawMisc(sp, x+xPos, y+yPos);
			}
		}
	}

	@Override
	public void drawText(SpriteBatch sp, int x, int y) {
		if(!visible)
			return;
		if(xSize>0 || ySize>0)
		{
			SpriteBatch.ClipSet cs = sp.getClip();
			sp.setclip(x+xPos, y+yPos, xSize, ySize);
			for (MenuAddable b : buttons) {
				b.drawText(sp, x+xPos, y+yPos);
			}
			cs.resetToThisClip();
		}else{
			for (MenuAddable b : buttons) {
				b.drawText(sp, x+xPos, y+yPos);
			}
		}
	}

	public void add(MenuAddable b){
		toAdd.add(b);
	}
	
	public void remove(MenuAddable b){
		toRemove.add(b);
	}
}
