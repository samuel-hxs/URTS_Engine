package menu;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mdesl.graphics.SpriteBatch;
import mdesl.graphics.Texture;
import mdesl.graphics.TextureRegion;

public class FontRenderer {

	private static List<FontRenderer> fonts;
	
	public Map<Character, Glyph> glyphs;
	
	public final String name;
	private int lineOffset;
	private int height;
	
	public static Texture tex;
	
	class Glyph{
		
		public TextureRegion region;
		public char ch;
		public Map<Character, Integer> distanceTo;
		
		public Glyph(){
			distanceTo = new HashMap<>();
		}
	}
	
	public void render(SpriteBatch sp, String s, int x, int y){
		if(s == null)
			return;
		y-=lineOffset;
		for (int i = 0; i < s.length(); i++) {
			Glyph gl = glyphs.get(s.charAt(i));
			sp.draw(gl.region, x, y);
			if(i < s.length()-1)
				x+=gl.distanceTo.get(s.charAt(i+1));
		}
	}
	
	public int getStringWidth(String s){
		int x = 0;
		if(s == null)
			return 0;
		for (int i = 0; i < s.length(); i++) {
			Glyph gl = glyphs.get(s.charAt(i));
			if(i < s.length()-1)
				x+=gl.distanceTo.get(s.charAt(i+1));
		}
		return x+x/(s.length()-1);
	}
	
	public int getStringHeight(){
		return height;
	}
	
	private FontRenderer(String n, Texture tex) throws Exception{
		name = n;
		
		glyphs = new HashMap<>();
		
		FileReader fr = new FileReader("res/font/map/"+name+".texFnt");
		BufferedReader br = new BufferedReader(fr);
		Glyph currGL = null;
		String s;
		while ((s = br.readLine()) != null) {
			if(s.startsWith("Line:")){
				lineOffset = Integer.parseInt(s.substring(5));
			}
			if(s.startsWith("Hgh:")){
				height = Integer.parseInt(s.substring(4));
			}
			if(s.startsWith("Char:")){
				if(currGL != null)
					glyphs.put(currGL.ch, currGL);
				s = s.substring(5);
				currGL = new Glyph();
				currGL.ch = s.charAt(0);
				s = s.substring(2);
				String[] q = s.split(" ");
				currGL.region = new TextureRegion(tex, Integer.parseInt(q[0]), Integer.parseInt(q[1]),
						Integer.parseInt(q[2]),Integer.parseInt(q[3]));
			}
			if(currGL == null)
				continue;
			if(s.startsWith("Dis:")){
				s = s.substring(4);
				char c = s.charAt(0);
				int dis = Integer.parseInt(s.substring(2));
				currGL.distanceTo.put(c, dis);
			}
		}
		if(currGL != null)
			glyphs.put(currGL.ch, currGL);
		
		br.close();
	}
	
	public static void init() throws Exception{
		fonts = new ArrayList<>();
		tex = new Texture(utility.ResourceLoader.loadResource("res/font/map/tex.png"));
		fonts.add(new FontRenderer("MONO_14", tex));
	}
	
	public static FontRenderer getFont(String name){
		for (FontRenderer f : fonts) {
			if(f.name.matches(name))
				return f;
		}
		debug.Debug.println("*Error: Font not found - "+name, debug.Debug.ERROR);
		return fonts.get(0);
	}
	
}
