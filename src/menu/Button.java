package menu;

import main.PicLoader;
import mdesl.graphics.Color;
import mdesl.graphics.SpriteBatch;
import mdesl.graphics.TextureRegion;

public abstract class Button extends AbstractButton{

	private TextureRegion state1;
	private TextureRegion state2;
	private TextureRegion stateFoc;
	private int focOffsetX;
	private int focOffsetY;
	private TextureRegion stateDis;
	
	public TextureRegion additionalImage;
	
	private String text;
	private int textOff;
	private String secondLine;
	private int secondLineOff;
	private Color textcolor = lightBlue;
	protected FontRenderer font;
	
	private int focusShine = 0;
	
	public static final Color grayDisabled = new Color(100,100,100);
	public static final Color gray = new Color(200,200,200);
	public static final Color blue = new Color(10,10,250);
	public static final Color brown = new Color(185,120,87);
	public static final Color lightBlue = new Color(10,183,255);
	
	public Button(int x, int y, String t) {
		super(x, y);
		
		state1 = PicLoader.pic.getImage(t+"n");
		state2 = PicLoader.pic.getImage(t+"c");
		stateFoc = PicLoader.pic.getImage(t+"f");
		stateDis = PicLoader.pic.getImage(t+"d");
		
		xSize = state1.getWidth();
		ySize = state1.getHeight();
		focOffsetX = (stateFoc.getWidth()-xSize)/2;
		focOffsetY = (stateFoc.getHeight()-ySize)/2;
		
		font = FontRenderer.getFont("MONO_14");
	}

	@Override
	public void drawIntern(SpriteBatch sp, int xOff, int yOff) {
		if(!isVisible)
			return;
		sp.setColor(Color.WHITE);
		if(isDisabled){
			sp.draw(stateDis, xPos+xOff, yPos+yOff);
		}else{
			sp.draw(state1, xPos+xOff, yPos+yOff);
			if(mouseLeft)
				sp.draw(state2, xPos+xOff, yPos+yOff);
			
			int fs = getFocusShine(isFocused);
			if(fs>0){
				sp.setColor(new Color(255,255,255,fs/4));
				sp.draw(stateFoc, xPos+xOff-focOffsetX, yPos+yOff-focOffsetY);
				sp.setColor(Color.WHITE);
			}
		}
		if(additionalImage != null)
			sp.draw(additionalImage, xPos+xOff, yPos+yOff);
	}
	
	private int getFocusShine(boolean b){
		if(b && focusShine<1023){
			focusShine += main.GameController.timePassed;
			if(focusShine>1023)
				focusShine = 1023;
		}else if(!b && focusShine>0){
			focusShine -= main.GameController.timePassed;
			if(focusShine<0)
				focusShine = 0;
		}
		return focusShine;
	}
	
	public void drawTextIntern(SpriteBatch sp, int xOff, int yOff){
		if(!isVisible)
			return;
		if(isDisabled){
			sp.setColor(grayDisabled);
		}else{
			sp.setColor(textcolor);
		}
		
		xOff += xPos;
		yOff += ySize/2+yPos;
		if(text != null && secondLine == null){
			font.render(sp, text, xOff+textOff, yOff+font.getStringHeight()/2-1);
		}else if(text != null && secondLine != null){
			font.render(sp, text, xOff+textOff, yOff-1);
			font.render(sp, secondLine, xOff+secondLineOff, yOff+font.getStringHeight()+1);
		}
	}

	@Override
	protected void clicked() {
		if(main.Settings.debugPrint && text != null){
			debug.Debug.println("*Button Clicked: ", debug.Debug.COM);
			debug.Debug.print(text);
		}
		isClicked();
	}
	
	protected abstract void isClicked();

	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setText(String text) {
		this.text = text;
		relocateText();
	}
	
	public void setSecondLine(String text) {
		secondLine = text;
		relocateText();
	}
	
	public void setFont(FontRenderer fr){
		font  = fr;
		relocateText();
	}

	private void relocateText(){
		if(text != null){
			textOff = (xSize-font.getStringWidth(text))/2;
		}
		if(secondLine != null){
			secondLineOff = (xSize-font.getStringWidth(secondLine))/2;
		}
	}
	
	public void setTextcolor(Color textcolor) {
		this.textcolor = textcolor;
	}
	
}
