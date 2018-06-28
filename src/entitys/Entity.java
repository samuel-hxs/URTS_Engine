package entitys;

import entitys.paint.Entity3DModel;

public class Entity {

	public float xPos;
	public float yPos;
	public float zPos;
	
	public float xOnScreen;
	public float yOnScreen;
	public boolean renderIcon;
	
	public boolean renderMeshComplex;
	public boolean renderMeshSimple;
	public boolean outsideFrustum;
	
	public int fowRadius = 5;
	public boolean renderFow;
	public boolean renderFowSkip;
	
	public int id;
	
	public float debug1;
	public float debug2;
	
	public Entity3DModel model;
	
	public static float debugStatic = 2;
	
	public Entity(int modelID){
		xPos = (float)(Math.random()-0.5)*main.GameControle.getMapSize();
		yPos = (float)(Math.random()-0.5)*main.GameControle.getMapSize();
		
		debug1 = (float)Math.random()*50;
		debug2 = (float)Math.random()*debugStatic*0.01f;
		
		zPos = 1;
		
		model = entitys.paint.EntityPainter.getModel(modelID);
	}
	
}
