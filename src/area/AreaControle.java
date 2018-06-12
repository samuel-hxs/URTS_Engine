package area;

import org.joml.Vector3f;
import org.joml.Vector4f;

import main.grphics.FrustumCullingFilter;
import main.grphics.Render3D;
import mdesl.graphics.Texture;
import mdesl.graphics.TextureRegion;

public class AreaControle {

	private final LandscapeShader shader;
	
	/**
	 * Sets the number of Edges in the Map, the number of Vertices is edgesPerSide*edgesPerSide*2
	 * Must be a multiple of AreaRender.size
	 */
	public static final int edgesPerSide = 200;
	public static final int superResolutionSteps = 3;
	
	public int tiles;
	
	private AreaRender[][][] mapRender;
	private RenderingAreaUntility[][] mapRenderU;
	
	private Texture mapTex;
	
	public float textureRepeat = 0.2f;
	
	private AreaValues area;
	
	public AreaControle() throws Exception{
		shader = new LandscapeShader();
		
		mapTex = new Texture(utility.ResourceLoader.loadResource("res/ima/map/defaultMapTexture.png"));
		
		area = new AreaValues();
	}
	
	public void prepareMap(){
		final int n = edgesPerSide / AreaRender.size_raw;
		tiles = n;
		
		mapRender = new AreaRender[n][n][superResolutionSteps];
		mapRenderU = new RenderingAreaUntility[n][n];
		for (int i = 0; i < mapRender.length; i++) {
			for (int j = 0; j < mapRender[i].length; j++) {
				for (int k = 0; k < superResolutionSteps; k++) {
					mapRender[i][j][k] = new AreaRender(i*AreaRender.size_raw - edgesPerSide/2 + AreaRender.size_raw/2,
							j*AreaRender.size_raw - edgesPerSide/2 + AreaRender.size_raw/2, this, k+1);
				}
				mapRenderU[i][j] = new RenderingAreaUntility();
				mapRenderU[i][j].xPos = getVertexResolution()*mapRender[i][j][0].xPos;
				mapRenderU[i][j].yPos = getVertexResolution()*mapRender[i][j][0].yPos;
				mapRenderU[i][j].radius = utility.MathUtility.cubeRadius(getVertexResolution()*AreaRender.size_raw);
			}
		}
	}
	
	public void update(){
		for (int i = 0; i < mapRender.length; i++) {
			for (int j = 0; j < mapRender[i].length; j++) {
				for (int k = 0; k < superResolutionSteps; k++) {
					mapRender[i][j][k].update(mapTex);
				}
			}
		}
	}
	
	public float getNodeHeight(float x, float y){
		return area.getHeight(x, y);
	}
	
	public void render(Render3D r3d, FrustumCullingFilter fcf){
		Vector3f vp = new Vector3f(r3d.getCamera().pos);
		Vector3f vlp = new Vector3f();
		Vector4f vl = new Vector4f();
		shader.prepare(r3d);
		
		shader.getShader().setUniformi(LandscapeShader.U_TEXTURE_MODE, 1);
		
		for (int i = 0; i < mapRender.length; i++) {
			for (int j = 0; j < mapRender[i].length; j++) {
				vl.x = vlp.x = mapRenderU[i][j].xPos;
				vl.y = vlp.y = mapRenderU[i][j].yPos;
				vl.z = vlp.z= 0;
				vl.w = 1;
				if(!fcf.insideFrustum(vl, mapRenderU[i][j].radius))
					continue;
				
				int m = 0;//Distance-Resolution
				float d = vlp.distance(vp);
				if(d<(float)main.GameControle.getMapSize()/2.25f) m = 1;
				if(d<(float)main.GameControle.getMapSize()/4.5f) m = 2;
				
				mapRender[i][j][m].render(r3d);
			}
		}
		
		shader.getShader().setUniformi(LandscapeShader.U_TEXTURE_MODE, 0);
	}
	
	public int getEdgesPerSide(){
		return edgesPerSide;
	}
	
	/**
	 * @return The size of one area-Vertex
	 */
	public float getVertexResolution(){
		return (float)main.GameControle.getMapSize()/(float)(edgesPerSide);
	}
	
	private class RenderingAreaUntility{
		
		public float xPos;
		public float yPos;
		public float radius;
		
		public int renderDepth;
		
		public float distanceToCamera;
		public int distanceOrder;
	}
}
