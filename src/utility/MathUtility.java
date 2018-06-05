package utility;

public class MathUtility {

	public static float cubeRadius(float edge){
		edge /= 2;
		float a = (float)Math.sqrt(edge*edge*2);
		return (float)Math.sqrt(a*a+edge*edge);
	}
}
