package utility;

import java.io.File;
import java.net.URL;

public class ResourceLoader {

	public static URL loadResource(String f){
		URL url = ResourceLoader.class.getResource(f);
		if(url == null){
			try {
				File fi = new File(f);
				if(fi.exists())
					return fi.toURI().toURL();
			} catch (Exception e) {
				return null;
			}
		}
		return url;
	}
}
