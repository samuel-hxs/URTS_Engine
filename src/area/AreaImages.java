package area;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import mdesl.graphics.Texture;
import mdesl.graphics.TextureRegion;

public class AreaImages {

	public final Texture tex;
	
	private Map<Integer, TextureRegion> tiles;
	
	public AreaImages() throws Exception{
		debug.Debug.println("Loading Map-Textures");
		
		tex = new Texture(utility.ResourceLoader.loadResource("res/ima/map/map.png"));
		
		tiles = new TreeMap<>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.intValue()-o2.intValue();
			}
		});
		
		BufferedReader br = new BufferedReader(new FileReader("res/ima/map/map.txt"));
		String s;
		while ((s = br.readLine()) != null) {
			if(s.startsWith("//"))
				continue;
			
			String[] st = s.split("###");
			
			try {
				tiles.put(Integer.parseInt(st[0]), new TextureRegion(tex, Integer.parseInt(st[1])*32, Integer.parseInt(st[2])*32, 32, 32));
			} catch (Exception e) {
				// TODO: What is the error here?
				debug.Debug.println("Can't Parse Map-Texture: "+st[0], debug.Debug.WARN);
			}
		}
		
		br.close();
	}
	
	public TextureRegion get(int id){
		return tiles.get(id);
	}
}
