package area;

import java.io.IOException;

import mdesl.graphics.SpriteBatch;
import mdesl.graphics.Texture;
import mdesl.graphics.TextureRegion;

public class AreaPainter {

	private Texture[] textures;
	private TextureRegion[][] tiles;
	
	public AreaPainter(){
		textures = new Texture[1];
		for (int i = 0; i < textures.length; i++) {
			try {
				textures[i] = new Texture(utility.ResourceLoader.loadResource("res/ima/map/"+i+".png"));
			} catch (IOException e) {
				debug.Debug.printException(e);
			}
		}
		tiles = new TextureRegion[textures.length][16];
		for (int i = 0; i < textures.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = new TextureRegion(textures[i], (j/4)*32, (j%4)*32, 32, 32);
			}
		}
	}
	
	public void render(SpriteBatch sp){
		for (int i = 0; i < 400; i++) {
			for (int j = 0; j < 100; j++) {
				sp.setMultiColor(new mdesl.graphics.Color((float)i/400f, 0f, 1f), new mdesl.graphics.Color(1f, 0, (float)j/100f),
						new mdesl.graphics.Color(), new mdesl.graphics.Color());
				sp.draw(tiles[0][j%16], i*32, j*32);
			}
		}
	}
}
