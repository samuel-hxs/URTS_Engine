package main;

import java.util.concurrent.TimeUnit;

import org.lwjgl.system.Configuration;

public class Main {

	public static final String TITLE = "Seypris";
	public static final String VERSION = "0.1";
	
	public static void main(String[] args) {
		new debug.util.DebugFrame();
		debug.Debug.println("* Starting "+TITLE+" v"+VERSION+" *");
		
		Fonts.createAllFonts();
		GameController gc;
		Thread gct;
		boolean err = false;
		
		try {
			// Configuration.DEBUG.set(true);
			gc = new GameController();
			// Using a real thread
			// TODO: Exception handling
			// TODO: Debugging
			
			gct = new Thread(gc);
			gct.start();
			
			while(true)
			{
				TimeUnit.MINUTES.sleep(1);
			}
		} catch (Exception e) {
			debug.Debug.println("FATAL: "+e.toString(), debug.Debug.FATAL);
			debug.Debug.printException(e);
			err = true;
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(!err)
		{
			// Indicating that no Exception occurred is 0.
			// Positive values indicating an Exception.
			// Set for Linux as it makes more sense for me, but it is not a standard.
			System.exit(0);
		}
	}
	
	private static void test() {
		org.joml.Matrix4f m1 = new org.joml.Matrix4f().perspective(0.4f, 10, 0.001f, 100);
		org.joml.Matrix4f m2 = new org.joml.Matrix4f().translate(10, 10, 333.3f);
	
		int testTime = 1000;
		long t = System.nanoTime();
		for (int i = 0; i < testTime; i++) {
			org.joml.Vector3f v = new org.joml.Vector3f((float)Math.random()*33, 
					(float)Math.random()*22, (float)Math.random());
			if(new org.joml.Vector4f(v, 0).mul(new org.joml.Matrix4f(m1).mul(m2)).x == 0)
				System.out.println("waa");
		}
		System.out.println("T: "+(System.nanoTime()-t)/testTime+"------------------------------");
	}
}
