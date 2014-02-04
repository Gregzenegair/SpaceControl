package fr.gh.spacecontrol.pools;

import org.andengine.util.adt.pool.GenericPool;

import fr.gh.spacecontrol.items.Cockpit;

public class CockpitPool extends GenericPool<Cockpit> {

	public static CockpitPool instance;

	public static CockpitPool sharedCockpitPool() {
		if (instance == null)
			instance = new CockpitPool();
		return instance;
	}

	private CockpitPool() {
		super();
	}

	@Override
	protected Cockpit onAllocatePoolItem() {
		return new Cockpit();
	}

	@Override
	protected void onHandleRecycleItem(final Cockpit c) {
		try {
			c.remove();
		} catch (Exception e) {
			System.err.println(e + " Objet : " + this.toString());
		}
	}
}
