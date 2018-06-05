package utility;

public class MovingHandler{
	
	private Moving x;
	private Moving y;
	private Moving z;
	
	public MovingHandler(){
		x = new Moving(0, 0);
		y = new Moving(0, 0);
		z = new Moving(0, 0);
	}
	
	public void update(long t){
		x = x.update(t);
		y = y.update(t);
		z = z.update(t);
	}
	
	public void set(float xf, float yf, float zf, long tar){
		x.add(new Moving(tar, xf), 0);
		y.add(new Moving(tar, yf), 0);
		z.add(new Moving(tar, zf), 0);
	}
	
	public long maxTime(){
		long t = x.maxTime();
		long u = y.maxTime();
		if(u>t)t = u;
		u = z.maxTime();
		if(u>t)return u;
		return t;
	}
	
	public float x(long time){
		return x.get(time);
	}
	public float y(long time){
		return y.get(time);
	}
	public float z(long time){
		if(z.next!= null)
			System.out.println(z.value+" "+z.get(time)+" "+z.next.value+" : "+
		z.targetTime+" "+z.next.targetTime+" : "+time);
		return z.get(time);
	}
	
	private class Moving{
		
		public long targetTime;
		private Moving next;
		
		public float value;
		
		public Moving(long t, float f){
			targetTime = t;
			value = f;
		}
		
		public Moving update(long t){
			if(next == null)
				return this;
			
			if(next.targetTime<t)
				return next;
			
			if(next.targetTime <= targetTime)
				return next;
			
			return this;
		}
		
		public void add(Moving m, int tendency){
			if(next == null){
				if(Math.abs(value-m.value) > 0.001f)
					next = m;
				return;
			}
			
			int td = 0;
			if(value>next.value)
				td = -1;
			if(value<next.value)
				td = 1;
			
			if(tendency == 0){
				next.add(m, td);
				return;
			}
			
			if(td == tendency){
				next.add(m, tendency);
				return;
			}
			
			next = m;
		}
		
		public float get(long time){
			if(next == null){
				return value;
			}
			
			long div = next.targetTime - targetTime;
			long q = next.targetTime-time;
			float v = (float)q/div;
			return value*v+next.value*(1f-v);
		}
		
		public long maxTime(){
			if(next == null)
				return targetTime;
			return next.maxTime();
		}
	}
}
