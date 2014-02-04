package fr.gh.spacecontrol.pools;

import org.andengine.util.adt.pool.GenericPool;

import fr.gh.spacecontrol.items.Reactor;

public class ReactorPool extends GenericPool<Reactor> {

	public static ReactorPool instance;

	public static ReactorPool sharedReactorPool() {
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
		try {
			r.remove();
		} catch (Exception e) {
			System.err.println(e + " Objet : " + this.toString());
		}

	}
}
