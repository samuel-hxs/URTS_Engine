package editor.map;

import area.AreaControle;
import main.PicLoader;
import mdesl.graphics.Color;
import mdesl.graphics.SpriteBatch;
import menu.Button;
import menu.DataField;
import menu.FontRenderer;
import menu.MoveMenu;
import menu.ScrollBar;

public class MapEditor extends MoveMenu{

	private float brushSize;
	private DataField brushSizeText;
	private ScrollBar brushSizeScroll;
	private int lastBrushSizeScroll;
	
	private ScrollBar intensityScroll;
	private float intensity;
	private ScrollBar rateScroll;
	private float rate;
	private ScrollBar exponentScroll;
	private float exponent;
	
	private final AreaControle area;
	private Button[] brushes;
	
	private FontRenderer font;
	
	public MapEditor(int x, int y, AreaControle a) {
		super(x, y, 300, 500);
		
		brushes = new Button[4];
		for (int i = 0; i < brushes.length; i++) {
			final int j = i;
			brushes[i] = new Button(20+(i%3)*40, 50+(i/3)*40, "mapEditor") {
				@Override
				protected void isClicked() {
					buttonClicked(j);
				}
			};
			add(brushes[i]);
		}
		brushes[0].additionalImage = PicLoader.pic.getImage("mapEditorB2");
		brushes[1].additionalImage = PicLoader.pic.getImage("mapEditorB3");
		brushes[2].additionalImage = PicLoader.pic.getImage("mapEditorB4");
		brushes[3].additionalImage = PicLoader.pic.getImage("mapEditorB1");
		
		brushSizeText = new DataField(20, 200, 80){
			@Override
			protected void textEntered(String s) {
				try {
					float t = Float.parseFloat(s);
					if(t < 0){
						textColor = Color.RED;
						return;
					}
					brushSize = t;
					brushSizeScroll.setScroll((int)(brushSize*2));
					lastBrushSizeScroll = brushSizeScroll.getScroll();
					textColor = Color.BLACK;
				} catch (Exception e) {
					textColor = Color.RED;
				}
			}
		};
		add(brushSizeText);
		brushSizeText.setCanTextEnter(true);
		brushSizeText.setText("10");
		
		brushSizeScroll = new ScrollBar(30, 230, 250, 50, 6, false);
		add(brushSizeScroll);
		
		exponentScroll = new ScrollBar(30, 270, 250, 52, 6, false);
		add(exponentScroll);
		
		intensityScroll = new ScrollBar(30, 350, 250, 29, 10, false);
		add(intensityScroll);
		
		rateScroll = new ScrollBar(30, 390, 250, 50, 6, false);
		add(rateScroll);
		rateScroll.setScroll(35);
		
		
		font = FontRenderer.getFont("MONO_14");
		area = a;
	}
	
	protected void update(){
		if(lastBrushSizeScroll != brushSizeScroll.getScroll()){
			brushSize = 0.5f*brushSizeScroll.getScroll();
			lastBrushSizeScroll = brushSizeScroll.getScroll();
			brushSizeText.setText(""+brushSize);
		}
		exponent = 1f+0.25f*exponentScroll.getScroll();
		intensity = 0.2f+0.2f*intensityScroll.getScroll();
		rate = 90f-rateScroll.getScroll()*2;
	}

	@Override
	protected void drawIntern(SpriteBatch sp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawTextIntern(SpriteBatch sp) {
		font.render(sp, "Brush-Size", xPos+20, yPos+190);
		font.render(sp, "Eccentricity: "+exponent, xPos+20, yPos+265);
		font.render(sp, "Intensity: "+intensity, xPos+20, yPos+345);
		font.render(sp, "Rate: "+rate, xPos+20, yPos+385);
	}
	
	private void buttonClicked(int i){
		for (int j = 0; j < brushes.length; j++) {
			brushes[j].setDisabled(i==j);
		}
		//TODO
	}
}
