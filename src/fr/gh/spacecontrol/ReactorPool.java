package fr.gh.spacecontrol;

import org.andengine.entity.shape.IShape;
import org.andengine.util.adt.pool.GenericPool;

public class ReactorPool extends GenericPool<Reactor> {

	public static ReactorPool instance;

	public static ReactorPool sharedEnemyPool() {
		if (instance == null)
			instance = new ReactorPool();
		return instance;
	}

	private ReactorPool() {
		super();
	}

	@Override
	protected Reactor onAllocatePoolItem() {
		return new Reactor();
	}

	@Override
	protected void onHandleRecycleItem(final Reactor r) {
		r.remove();
	}
}
