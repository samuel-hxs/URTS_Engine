package entitys;

public class Entity {

	public float xPos;
	public float yPos;
	public float zPos;
	
	public float xOnScreen;
	public float yOnScreen;
	public boolean renderIcon;
	
	public boolean renderMeshComplex;
	public boolean renderMeshSimple;
	
	public int fowRadius = 5;
	public boolean renderFow;
	public boolean renderFowSkip;
	
	public int id;
	
	public float debug1;
	public float debug2;
	
	public Entity(){
		xPos = (float)Math.random()*50-25;
		yPos = (float)Math.random()*50-25;
		
		debug1 = (float)Math.random()*50;
		debug2 = (float)Math.random()*0.02f;
	}
	
}
