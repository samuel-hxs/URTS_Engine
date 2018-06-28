package entitys;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class EntityList{
	
	public List<Entity> list;
	
	public static final int NUMBER_OF_UNITS = 100;
	
	private Semaphore sema;
	
	private Thread occupying;
	
	public EntityList(){
		list = new ArrayList<>(NUMBER_OF_UNITS);
		
		sema = new Semaphore(1);
	}
	
	public void test(int n){
		if(n>NUMBER_OF_UNITS) n = NUMBER_OF_UNITS;
		for (int i = 0; i < n; i++) {
			list.add(new Entity(1));
		}
	}
	
	/**
	 * Holds until available for work
	 */
	public void workStarted(){
		while(true){
			try {
				if(aquireProcedure()){
					setOccupying(Thread.currentThread());
					return;
				}
			} catch (InterruptedException e) {}
		}
	}
	
	private boolean aquireProcedure() throws InterruptedException{
		int i = 0;
		while(!sema.tryAcquire(30, TimeUnit.MILLISECONDS)){
			Thread.sleep(1);
			
			if(i%100 == 0)//Don't want it to repeat every time!
			debug.Debug.println("*Thread ["+Thread.currentThread()+"] was denied entry for to long; ["
			+getOccupying()+"] is currently Occupying!", debug.Debug.WARN);
			
			i++;
		}
		return true;
	}
	
	public void workEnd(){
		setOccupying(null);
		sema.release();
	}
	
	private synchronized void setOccupying(Thread occupying) {
		this.occupying = occupying;
	}
	
	public synchronized Thread getOccupying() {
		return occupying;
	}
}