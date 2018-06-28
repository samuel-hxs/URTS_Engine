package area;

/**
 * Class to hold the Map itself
 * @author Sven T. Schneider
 */
public class AreaValues {

	public static final int DOTS_PER_SQUARE = 2;
	
	public static final float HEIGHT_DEVIDE = 0.001f;
	
	private final int arrayOffset;
	
	public byte[][][] texture;
	public byte[][][] texIntensity;
	
	public int[][] heightMap;
	
	public final int arraySize;
	
	public AreaValues(){
		arraySize = main.GameControle.getMapSize()*DOTS_PER_SQUARE+2;
		arrayOffset = arraySize/2;
		
		texIntensity = new byte[arraySize+1][arraySize+1][3];
		texture = new byte[arraySize+1][arraySize+1][3];
		heightMap = new int[arraySize+1][arraySize+1];
		
		for (int i = 0; i < heightMap.length; i++) {
			int x = i-arrayOffset;
			for (int j = 0; j < heightMap[i].length; j++) {
				heightMap[i][j] = x*x/10;
				
				heightMap[i][j] += (int)(editor.map.SimplexNoise.noise(0.003*i, 0.003*j)*4000+
						editor.map.SimplexNoise.noise(0.03*i, 0.03*j+63f)*130);
				
				float r = (float)(editor.map.SimplexNoise.noise(0.002*i+1004f, 0.002*j)*3+
						editor.map.SimplexNoise.noise(0.07*i, 0.07*j+10f)*0.3);
				
				r = Math.abs(r)/4;
				r*=255;
				if(r>255) r = 255;
				texIntensity[i][j][0] = (byte)((int)r);
				texIntensity[i][j][2] = (byte)(255-(int)(r));
				texture[i][j][2] = 2;
				texture[i][j][0] = 5;
			}
		}
	}
	
	public int getHeightCell(int x, int y){
		if(x<-arrayOffset || x>arrayOffset || y<-arrayOffset || y>arrayOffset)
			return 0;
		return heightMap[x+arrayOffset][y+arrayOffset];
	}
	
	public float getHeight(float x, float y){
		x *= DOTS_PER_SQUARE;
		y *= DOTS_PER_SQUARE;
		
		float fx = x - (int)x;
		float fy = y - (int)y;
		float dx1 = (1f-fx)*getHeightCell((int)x, (int)y)+fx*getHeightCell((int)x+1, (int)y);
		float dx2 = (1f-fx)*getHeightCell((int)x, (int)y+1)+fx*getHeightCell((int)x+1, (int)y+1);
		return ((1-fy)*dx1+fy*dx2) * HEIGHT_DEVIDE;
	}
	
	public float getTextureComponent(float x, float y, int p){
		x *= DOTS_PER_SQUARE;
		y *= DOTS_PER_SQUARE;
		
		int t1 = texture[(int)x+arrayOffset][(int)y+arrayOffset][p]&0xff;
		float t2 = ((float)(texIntensity[(int)x+arrayOffset][(int)y+arrayOffset][p]&0xff))/512f;
		return t2+t1;
	}
	
}
