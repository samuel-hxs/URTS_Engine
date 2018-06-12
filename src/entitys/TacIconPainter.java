package entitys;

import main.PicLoader;
import mdesl.graphics.Color;
import mdesl.graphics.SpriteBatch;
import mdesl.graphics.Texture;
import mdesl.graphics.TextureRegion;
import mdesl.graphics.glutils.VertexArray;

public class TacIconPainter {
	
	private VertexArray buffer;
	
	private static final int size = 12;
	
	private double shinyPoint;
	
	public TacIconPainter(){
		buffer = new VertexArray(6000, SpriteBatch.ATTRIBUTES);
	}
	
	public void paintTacIcons(CollisionFreeEntityIterator c, SpriteBatch sp){
		TextureRegion tr = PicLoader.pic.getImage("TacIconTest");
		
		shinyPoint = (double)(System.currentTimeMillis()/60)%main.GameControle.getMapSize()*8-
				main.GameControle.getMapSize()*4;
		shinyPoint /= 2;
		
		sp.getShader().setUniformi("u_mode", 1);
		for(; c.hasNext();){
			singleBlock(c.next(), sp, tr);
		}
		if(buffer.getCount()>0)
			draw(sp, tr.getTexture());
		sp.getShader().setUniformi("u_mode", 0);
	}
	
	private void singleBlock(EntityList en, SpriteBatch sp, TextureRegion tr){
		if(buffer.getCount()+6*EntityList.NUMBER_OF_UNITS >= 6000)
			draw(sp, tr.getTexture());
		
		Color c = new Color(0.2f, 0.4f, 1f);
		for (Entity e : en.list) {
			if(!e.renderIcon || e.outsideFrustum)continue;
			int x = (int)((e.xOnScreen+1f)*sp.getWidth()/2);
			int y = (int)((-e.yOnScreen+1f)*sp.getHeight()/2);
			float s = 1-(float)Math.abs(shinyPoint - e.xPos + e.yPos)*0.01f;
			if(s<0)s = 0;
			paint(sp, tr, x, y, c, s);
			debug.FrameStatistics.entitysPainted++;
		}
	}
	
	public void draw(SpriteBatch sp, Texture t){
		buffer.flip();
		sp.render(buffer, t);
		buffer.clear();
		buffer.setCount(0);
	}
	
	private void paint(SpriteBatch sp, TextureRegion tr, int x, int y, Color c, float a){
		SpriteBatch.vertex(x-size, y-size, c.r, c.g, c.b, a, tr.getU(), tr.getV(), buffer);
		SpriteBatch.vertex(x+size+1, y-size, c.r, c.g, c.b, a, tr.getU2(), tr.getV(), buffer);
		SpriteBatch.vertex(x-size, y+size+1, c.r, c.g, c.b, a, tr.getU(), tr.getV2(), buffer);
		
		SpriteBatch.vertex(x+size+1, y-size, c.r, c.g, c.b, a, tr.getU2(), tr.getV(), buffer);
		SpriteBatch.vertex(x+size+1, y+size+1, c.r, c.g, c.b, a, tr.getU2(), tr.getV2(), buffer);
		SpriteBatch.vertex(x-size, y+size+1, c.r, c.g, c.b, a, tr.getU(), tr.getV2(), buffer);
	}
	
}
