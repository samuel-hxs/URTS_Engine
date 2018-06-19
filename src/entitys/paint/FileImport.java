package entitys.paint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public abstract class FileImport {
	
	public static final String FILEPATH_MODEL_3D = "res/m3d/";

	protected final OnMapObject[] load(File f) throws Exception{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			return load(br);
		} catch (IOException e) {
			debug.Debug.println("*ERROR loading 3D-File: "+e.toString(), debug.Debug.ERROR);
			throw e;
		}finally {
			try{
				if(br!=null)
					br.close();
			}catch (Exception e2) {}
		}
	}
	
	protected abstract OnMapObject[] load(BufferedReader b) throws Exception;
	
	public static OnMapObject[] load3DObject(String filepath) throws Exception{
		return new FileImport_Obj().load(new File(filepath));
	}
}
