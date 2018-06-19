package entitys;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class EntityList{
	
	public List<Entity> list;
	
	public static final int NUMBER_OF_UNITS = 100;
	
	private Semaphore sema;
	
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
		sema.acquireUninterruptibly();
	}
	
	public void workEnd(){
		sema.release();
	}
}