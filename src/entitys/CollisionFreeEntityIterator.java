package entitys;

import java.util.Iterator;

public abstract class CollisionFreeEntityIterator implements Iterator<EntityList>{

	private int position;
	private final EntityControle ec;
	
	public CollisionFreeEntityIterator(EntityControle e) {
		ec = e;
		position = -1;
	}
	
	@Override
	public boolean hasNext() {
		if(position >= ec.getLenght()-1){
			syncPosition(position+1);
		}
		return position < ec.getLenght()-1;
	}

	@Override
	public EntityList next() {
		while (!canUseNext(position)) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
		position++;
		return ec.get(position);
	}
	
	protected abstract boolean canUseNext(int pos);
	
	protected abstract void syncPosition(int pos);
}
