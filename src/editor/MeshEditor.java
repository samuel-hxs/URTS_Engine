package editor;

import main.PicLoader;
import mdesl.graphics.SpriteBatch;
import mdesl.graphics.TextureRegion;
import menu.Button;
import menu.MoveMenu;

public class MeshEditor extends MoveMenu{
	
	private Button[] meshPointSelect;
	private int selectedPoint = 4;
	private TextureRegion selTex;
	
	private Button[] move;

	public MeshEditor() {
		super(10, 10, 400, 500);
		
		selTex = PicLoader.pic.getImage("CBqn");
		
		meshPointSelect = new Button[9];
		for (int i = 0; i < meshPointSelect.length; i++) {
			final int j = i;
			
			meshPointSelect[i] = new Button((i/3)*30+155, (i%3)*30+40, "CB") {
				@Override
				protected void isClicked() {
					selectedPoint = j;
				}
			};
			add(meshPointSelect[i]);
		}
		
		move = new Button[6];
		move[0] = new Button(50, 190, "btnL") {
			@Override
			protected void isClicked() {
				
			}
		};
		move[1] = new Button(100, 190, "btnR") {
			@Override
			protected void isClicked() {
				
			}
		};
		move[2] = new Button(75, 165, "btnUp") {
			@Override
			protected void isClicked() {
				
			}
		};
		move[3] = new Button(75, 215, "btnDo") {
			@Override
			protected void isClicked() {
				
			}
		};
		
		move[4] = new Button(155, 165, "btnUp") {
			@Override
			protected void isClicked() {
				
			}
		};
		move[5] = new Button(155, 215, "btnDo") {
			@Override
			protected void isClicked() {
				
			}
		};
		
		for (int i = 0; i < move.length; i++) {
			add(move[i]);
		}
		
		Button b = new Button(230, 140, "bg") {
			@Override
			protected void isClicked() {
				
			}
		};
		b.setText("Step 0.001");
		add(b);
		b = new Button(230, 172, "bg") {
			@Override
			protected void isClicked() {
				
			}
		};
		b.setText("Step 0.01");
		add(b);
		b = new Button(230, 204, "bg") {
			@Override
			protected void isClicked() {
				
			}
		};
		b.setText("Step 0.1");
		add(b);
		b = new Button(230, 236, "bg") {
			@Override
			protected void isClicked() {
				
			}
		};
		b.setText("Step 1.0");
		add(b);
	}

	@Override
	protected void drawIntern(SpriteBatch sp) {
		sp.draw(selTex, (selectedPoint/3)*30+155+xPos, (selectedPoint%3)*30+40+yPos);
	}

	@Override
	protected void drawTextIntern(SpriteBatch sp) {
		
	}

}
