package fr.gh.spacecontrol;

import org.andengine.entity.shape.IShape;
import org.andengine.util.adt.pool.GenericPool;

public class CockpitPool extends GenericPool<Cockpit> {

	public static CockpitPool instance;

	public static CockpitPool sharedEnemyBodyPool() {
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
	protected void onHandleRecycleItem(final Cockpit e) {
		try {
			e.remove();
		} catch (Exception ex) {
			System.err.println(ex + " Objet : " + this.toString());
		}
	}
}
