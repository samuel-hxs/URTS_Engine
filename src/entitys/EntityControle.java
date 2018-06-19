package entitys;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Will hold the Entity lists, will also control the sync between the Update-Thread,
 * the Projection-Translation-Thread and the Painter-Threads
 * @author Sven T. Schneider
 */
public class EntityControle{

	private int lastBlockUpdated;
	private int lastBlockFrustum;
	private int lastBlockProjected;
	private int lastBlockSecondCheck;
	private int lastBlockPainted;
	
	private int lenght;
	
	private List<EntityList> list;
	
	private EntityUpdateThread eut;
	private EntityProjectionThread ept;
	private EntityFrustumThread eft;
	private EntitySecondCheckThread est;
	
	private EntityTickUpdate eLTU;
	
	private int testAmmount;
	
	public EntityControle(){
		list = new ArrayList<>(); 
		new EntityThreadTimer();
		
		eut = new EntityUpdateThread();
		eft = new EntityFrustumThread();
		est = new EntitySecondCheckThread();
		ept = new EntityProjectionThread();
		
		eLTU = new EntityTickUpdate(this);
	}
	
	public void test(int ammount){
		testAmmount = ammount;
	}
	
	public synchronized int getBlUpdated(){
		return lastBlockUpdated;
	}
	
	public synchronized int getBlProjected(){
		return lastBlockProjected;
	}
	
	public synchronized int getBlPainted(){
		return lastBlockPainted;
	}
	
	public synchronized int getBlFrustum(){
		return lastBlockFrustum;
	}
	
	public synchronized int getBlSecondCheck(){
		return lastBlockSecondCheck;
	}
	
	public synchronized void setBlSecondCheck(int s){
		lastBlockSecondCheck = s;
	}
	
	public synchronized void setBlUpdated(int s){
		lastBlockUpdated = s;
	}
	
	public synchronized void setBlProjected(int s){
		lastBlockProjected = s;
	}
	
	public synchronized void setBlPainted(int s){
		lastBlockPainted = s;
	}
	
	public synchronized void setBlFrustum(int s){
		lastBlockFrustum = s;
	}
	
	public synchronized EntityList get(int p){
		return list.get(p);
	}
	
	public synchronized int getLenght(){
		return lenght;
	}
	
	/**
	 * Must be called at the start of every Frame-Loop!
	 */
	public synchronized void reset(){
		if(testAmmount > 0){
			eLTU.waintUntilDone();
			
			list.clear();
			while (testAmmount>0) {
				EntityList e = new EntityList();
				e.test(testAmmount);
				list.add(e);
				testAmmount -= EntityList.NUMBER_OF_UNITS;
			}
		}
		
		eLTU.startNewRun();
		
		lastBlockPainted = 0;
		lastBlockProjected = 0;
		lastBlockUpdated = 0;
		lastBlockSecondCheck = 0;
		lastBlockFrustum = 0;
		
		lenght = list.size();
	}
	
	/**
	 * Starts the Entity-Updater.<br>
	 * Will load the current Simulation-Parameters into local memory.
	 */
	public void startUpdater(){
		/*
		 * Will always be able to get the next Block, it is the first thread to work.
		 */
		eut.start(new CollisionFreeEntityIterator(this) {
			@Override
			protected void syncPosition(int pos) {
				setBlUpdated(pos);
			}
			
			@Override
			protected boolean canUseNext(int pos) {
				return true;
			}
		});
	}
	
	/**
	 * Starts the Projection-Updater.<br>
	 * Will prepare Screen-Position for Tactical-Icons.<br> 
	 * IMPORTANT: must be given the active Projection-View-Matrix to work correctly
	 * @param view the View-Matrix. Will not be changed!
	 */
	public void startProjection(Matrix4f proj, Matrix4f camera){
		/*
		 * Only can use Blocks cleared by the Updater-Thread
		 */
		ept.start(new CollisionFreeEntityIterator(this) {
			@Override
			protected void syncPosition(int pos) {
				setBlProjected(pos);
			}
			
			@Override
			protected boolean canUseNext(int pos) {
				return pos < getBlFrustum();
			}
		}, proj, camera);
	}
	
	/**
	 * Starts the Projection-Updater.<br>
	 * Will check Frustum-Intersection and active render distance.<br> 
	 * IMPORTANT: must be given the active Projection-View-Matrix to work correctly
	 */
	public void startFrustum(Matrix4f proj, Matrix4f camera, Vector3f pos){
		/*
		 * Only can use Blocks cleared by the Updater-Thread
		 */
		eft.start(new CollisionFreeEntityIterator(this) {
			@Override
			protected void syncPosition(int pos) {
				setBlFrustum(pos);
			}
			
			@Override
			protected boolean canUseNext(int pos) {
				return pos < getBlUpdated();
			}
		}, proj, camera, pos);
	}
	
	/**
	 * Starts the Second-Check.<br>
	 * Will check Fog-Of-War redundancy for lower paint-costs. Is suposed to run paralel to
	 * the Projection thread, however it's complition is necesary for Paint-Calls
	 */
	public void startSecondCheck(){
		/*
		 * Only can use Blocks cleared by the Updater-Thread
		 */
		est.start(new CollisionFreeEntityIterator(this) {
			@Override
			protected void syncPosition(int pos) {
				setBlSecondCheck(pos);
			}
			
			@Override
			protected boolean canUseNext(int pos) {
				return pos < getBlFrustum();
			}
		});
	}
	
	/**
	 * Get a Paint-Iterator over all Entitys; will NOT clear the Paint-Access-Flag.<br>
	 * <b>CAUTION:</b> The itterating methode can not use access to {@link Entity}.xOnScreen.
	 * It also can not modify the position coordinats. Violation may result in Thread-Handler-Exception.
	 * @param secondCheckNeeded true: locks the collision flag, if the second-check-thread hasn't cleared yet.
	 * false: Access is allowed if the SecondCheck hasn't cleared yet. Access to any variables wrote by secondCheck
	 * must be prohibited!
	 * @return a {@link CollisionFreeEntityIterator} to be used for Painting
	 */
	public CollisionFreeEntityIterator getPaintIterator(final boolean secondCheckNeeded){
		/*
		 * Can only use blocks cleared by projection and updater
		 */
		return new CollisionFreeEntityIterator(this) {
			@Override
			protected void syncPosition(int pos) {}
			
			@Override
			protected boolean canUseNext(int pos) {
				if(secondCheckNeeded)
					return pos < getBlFrustum() && pos < getBlSecondCheck();
				return pos < getBlFrustum();
			}
		};
	}
	
	/**
	 * Get a Paint-Iterator over all Entitys; will clear the Paint-Access-Flag for the Cleanup-Thread.<br>
	 * Is allowed to use all entity-References.
	 * @return a {@link CollisionFreeEntityIterator} to be used for Painting ONCE per Frame
	 */
	public CollisionFreeEntityIterator getLastPaintIterator(){
		/*
		 * Can only use blocks cleared by projection and updater
		 */
		return new CollisionFreeEntityIterator(this) {
			@Override
			protected void syncPosition(int pos) {
				setBlPainted(pos);
			}
			
			@Override
			protected boolean canUseNext(int pos) {
				return pos < getBlProjected();
			}
		};
	}
	
}
