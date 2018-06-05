package debug;

import debug.util.PrecisionTimingUnit;

public class Timing {

	private static PrecisionTimingUnit fps;
	private static PrecisionTimingUnit fpsTh;
	
	public static void init(){
		fps = new PrecisionTimingUnit();
		fpsTh = new PrecisionTimingUnit();
	}
	
	public static float[] getFps(){
		if(fps == null)
			return new float[]{0f,0f,0f,0f};
		return new float[]{
			1000000000f/(float)fps.number10,
			1000000000f/(float)fps.number50,
			1000000000f/(float)fps.number100,
			(float)fps.number50/1000
		};
	}
	
	public static void markFps(long t){
		fps.mark(t);
	}
	
	public static float[] getFpsTh(){
		if(fpsTh == null)
			return new float[]{0f,0f,0f,0f};
		return new float[]{
			1000000000f/(float)fpsTh.number10,
			1000000000f/(float)fpsTh.number50,
			1000000000f/(float)fpsTh.number100,
			(float)fpsTh.number50/1000
		};
	}
	
	public static void markFpsTh(long t){
		fpsTh.mark(t);
	}
}
