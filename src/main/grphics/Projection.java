package main.grphics;

import org.joml.Matrix4f;

import main.Settings;

public class Projection {

	private Matrix4f projectionMatrix;
	
	private static final float Z_NEAR = 0.01f;
	private static final float Z_FAR_MUL = 50f;
	
	private float aspect;
	
	public Projection(){
		aspect = 1.0f;
		updateVM();
	}
	
	public void setAspect(int width, int height){
		aspect = (float)width/height;
		updateVM();
	}
	
	public void updateVM(){
		projectionMatrix = new Matrix4f().perspective((float)Math.toRadians(Settings.fov), aspect,
				Z_NEAR, Z_FAR_MUL*Settings.renderDist);
	}
	
	public Matrix4f getProjectionOnly(){
		return projectionMatrix;
	}
	
	public Matrix4f getMatrix(Camera c){
		return new Matrix4f(projectionMatrix).mul(c.getViewMatrix());
	}
}
