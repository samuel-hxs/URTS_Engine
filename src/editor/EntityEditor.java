package editor;

import entitys.EntityControle;
import entitys.paint.Entity3DModel;
import entitys.paint.EntityPainter;
import mdesl.graphics.Color;
import mdesl.graphics.SpriteBatch;
import menu.Button;
import menu.DataField;
import menu.FontRenderer;
import menu.MoveMenu;

public class EntityEditor extends MoveMenu{

	private Entity3DModel e3d;
	
	private FontRenderer font;
	
	private DataField icon;
	
	private DataField fp1;
	private DataField d1;
	
	private DataField fp2;
	private DataField d2;
	
	private DataField fp3;
	private EntityControle entity;
	
	private int toSpawn;
	
	public EntityEditor(int x, int y, EntityControle e) {
		super(x, y, 300, 500);
		
		entity = e;
		
		e3d = EntityPainter.getModel(1);
		
		icon = new DataField(10, 50, 100){
			@Override
			protected void textEntered(String s) {
				try {
					e3d.setIconPosition(Integer.parseInt(s));
					textColor = Color.BLACK;
				} catch (Exception e) {
					textColor = Color.RED;
				}
			}
		};
		add(icon);
		icon.setCanTextEnter(true);
		
		fp1 = new DataField(20, 120, 150){
			@Override
			protected void textEntered(String s) {
				try {
					e3d.setModels(fp1.getText(), fp2.getText(), fp3.getText());
					textColor = Color.BLACK;
				} catch (Exception e) {
					textColor = Color.RED;
				}
			}
		};
		add(fp1);
		fp1.setCanTextEnter(true);
		
		fp2 = new DataField(20, 190, 150){
			@Override
			protected void textEntered(String s) {
				try {
					e3d.setModels(fp1.getText(), fp2.getText(), fp3.getText());
					textColor = Color.BLACK;
				} catch (Exception e) {
					textColor = Color.RED;
				}
			}
		};
		add(fp2);
		fp2.setCanTextEnter(true);
		
		fp3 = new DataField(20, 260, 150){
			@Override
			protected void textEntered(String s) {
				try {
					e3d.setModels(fp1.getText(), fp2.getText(), fp3.getText());
					textColor = Color.BLACK;
				} catch (Exception e) {
					textColor = Color.RED;
				}
			}
		};
		add(fp3);
		fp3.setCanTextEnter(true);
		
		d1 = new DataField(200, 120, 50){
			@Override
			protected void textEntered(String s) {
				try {
					e3d.setDistance1(Float.parseFloat(s));
					textColor = Color.BLACK;
				} catch (Exception e) {
					textColor = Color.RED;
				}
			}
		};
		add(d1);
		d1.setCanTextEnter(true);
		
		d2 = new DataField(200, 190, 50){
			@Override
			protected void textEntered(String s) {
				try {
					e3d.setDistance2(Float.parseFloat(s));
					textColor = Color.BLACK;
				} catch (Exception e) {
					textColor = Color.RED;
				}
			}
		};
		add(d2);
		d2.setCanTextEnter(true);
		
		DataField amm = new DataField(20, 330, 100){
			@Override
			protected void textEntered(String s) {
				try {
					toSpawn = Integer.parseInt(s);
					textColor = Color.BLACK;
				} catch (Exception e) {
					textColor = Color.RED;
					toSpawn = 0;
				}
			}
		};
		add(amm);
		amm.setCanTextEnter(true);
		
		Button b = new Button(140, 320, "bb") {
			@Override
			protected void isClicked() {
				debug.Debug.println("***Test-Spawn "+toSpawn+" Units***", debug.Debug.MESSAGE);
				e3d.debugPrint();
				entity.test(toSpawn);
			}
		};
		add(b);
		b.setText("Spawn");
		b.setTextcolor(Button.gray);
		
		setLoad();
		
		font = FontRenderer.getFont("MONO_14");
	}
	
	private void setLoad(){
		icon.setText(""+e3d.getIconPosition());
		fp1.setText(""+e3d.fileDir1);
		fp2.setText(""+e3d.fileDir2);
		fp3.setText(""+e3d.fileDir3);
		d1.setText(""+e3d.getDistance1());
		d2.setText(""+e3d.getDistance2());
	}

	@Override
	protected void drawIntern(SpriteBatch sp) {
		
	}
	
	@Override
	protected void drawMisc(SpriteBatch sp) {
		super.drawMisc(sp);
		sp.setColor(Color.WHITE);
		sp.draw(e3d.getIcon(), 230+xPos, 35+yPos, 50, 50);
	}

	@Override
	protected void drawTextIntern(SpriteBatch sp) {
		sp.setColor(Color.WHITE);
		font.render(sp, "Icon-ID:", 7+xPos, 40+yPos);
		
		font.render(sp, "High-Resolution Model:", 7+xPos, 100+yPos);
		font.render(sp, "Filename:", 20+xPos, 113+yPos);
		font.render(sp, "Render-D.:", 200+xPos, 113+yPos);
		
		font.render(sp, "Low-Resolution Model:", 7+xPos, 170+yPos);
		font.render(sp, "Filename:", 20+xPos, 183+yPos);
		font.render(sp, "Render-D.:", 200+xPos, 183+yPos);
		
		font.render(sp, "Rander-Always-Model (Use sparing!):", 7+xPos, 240+yPos);
		font.render(sp, "Filename:", 20+xPos, 253+yPos);
		
		font.render(sp, "Spawn units:", 7+xPos, 323+yPos);
	}

}
