package menu;

import main.PicLoader;
import mdesl.graphics.Color;
import mdesl.graphics.SpriteBatch;
import mdesl.graphics.TextureRegion;

public class DataField extends AbstractButton{

	private TextureRegion[][] tr;
	
	public Color backColor = Color.WHITE;
	public Color textColor = Color.BLACK;
	
	private FontRenderer font;
	
	private TextureRegion line;
	
	private boolean canTextEnter = false;
	private boolean isActive;
	
	private AdvancedTextEnterField adf;
	
	public DataField(int x, int y, int xSize) {
		super(x, y);
		TextureRegion t = PicLoader.pic.getImage("DataField1");
		tr = new TextureRegion[2][3];
		tr[0][0] = new TextureRegion(t, 0, 0, 5, t.getHeight());
		tr[0][1] = new TextureRegion(t, 5, 0, 11, t.getHeight());
		tr[0][2] = new TextureRegion(t, 16, 0, 5, t.getHeight());
		
		t = PicLoader.pic.getImage("DataField2");
		tr[1][0] = new TextureRegion(t, 0, 0, 5, t.getHeight());
		tr[1][1] = new TextureRegion(t, 5, 0, 11, t.getHeight());
		tr[1][2] = new TextureRegion(t, 16, 0, 5, t.getHeight());
		
		this.xSize = xSize;
		ySize = t.getHeight();
		
		font = FontRenderer.getFont("SANS_14");
		
		line = PicLoader.pic.getImage("w");
		
		adf = new AdvancedTextEnterField() {
			@Override
			protected void specialKey(int id) {
				if(id == AdvancedTextEnterField.BUTTON_ENTER){
					unClicked();
					textEnteredDirectly(adf.text);
				}
			}
			
			@Override
			protected boolean isSpecialChar(char c) {
				return false;
			}
		};
	}
	
	/**
	 * Will be called if Text was Entered or focus changes
	 * @param s the text
	 */
	protected void textEntered(String s){}
	
	/**
	 * Will be called if Text was Entered by pressing Enter
	 * @param s the text
	 */
	protected void textEnteredDirectly(String s){}

	@Override
	protected void drawIntern(SpriteBatch sp, int xOff, int yOff) {
		sp.draw(tr[0][0], xPos+xOff, yPos+yOff);
		sp.draw(tr[0][1], xPos+xOff+5, yPos+yOff, xSize-10, ySize);
		sp.draw(tr[0][2], xPos+xOff+xSize-5, yPos+yOff);
		
		sp.setColor(backColor);
		sp.draw(tr[0][0], xPos+xOff, yPos+yOff);
		sp.draw(tr[0][1], xPos+xOff+5, yPos+yOff, xSize-10, ySize);
		sp.draw(tr[0][2], xPos+xOff+xSize-5, yPos+yOff);
		
		if(isActive && (System.currentTimeMillis()/500)%2 == 0){
			sp.setColor(textColor);
			sp.draw(line, xPos+xOff+8+font.getStringWidthSpecial(adf.text.substring(0, adf.tebpos), 'l')+1,
					yPos+yOff+4, 1, 12);
		}
		
		sp.setColor(Color.WHITE);
	}

	@Override
	protected void drawTextIntern(SpriteBatch sp, int xOff, int yOff) {
		sp.setColor(textColor);
		font.render(sp, adf.text, xPos+xOff+8, yPos+yOff+16);
		
		sp.setColor(Color.WHITE);
	}

	@Override
	protected void clicked() {
		if(canTextEnter){
			isActive = true;
			main.InputHandler.listener = adf;
		}
	}
	
	@Override
	protected void unClicked() {
		if(isActive){
			isActive = false;
			main.InputHandler.listener = null;
			textEntered(adf.text);
		}
	}

	public void setText(String text){
		adf.text = text;
		adf.tebpos = text.length();
	}
	
	public String getText(){
		return adf.text;
	}
	
	public void setCanTextEnter(boolean canTextEnter) {
		this.canTextEnter = canTextEnter;
	}
}
