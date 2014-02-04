package fr.gh.spacecontrol.entities;

import org.andengine.util.adt.pool.GenericPool;

public class GunshipPool extends GenericPool<Gunship> {

	public static GunshipPool instance;

	public static GunshipPool sharedGunshipPool() {
		if (instance == null)
			instance = new GunshipPool();
		return instance;
	}

	private GunshipPool() {
		super();
	}

	@Override
	protected Gunship onAllocatePoolItem() {
		return new Gunship();
	}

	@Override
	protected void onHandleRecycleItem(final Gunship g) {
		try {
			g.remove();
		} catch (Exception e) {
			System.err.println(e + " Objet : " + this.toString());
		}

	}
}
