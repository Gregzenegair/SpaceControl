package fr.gh.spacecontrol.entities;

import org.andengine.util.adt.pool.GenericPool;

public class WreckagePool extends GenericPool<Wreckage> {

	public static WreckagePool instance;

	public static WreckagePool sharedWreckagePool() {
		if (instance == null)
			instance = new WreckagePool();
		return instance;
	}

	private WreckagePool() {
		super();
	}

	@Override
	protected Wreckage onAllocatePoolItem() {
		return new Wreckage();
	}

	protected void onHandleRecycleItem(final Wreckage wreckage) {
	try{
		wreckage.getSprite().clearEntityModifiers();
		wreckage.getSprite().clearUpdateHandlers();
		wreckage.getSprite().setVisible(false);
		wreckage.getSprite().detachSelf();
		
	} catch (Exception e) {
		System.err.println(e + " Objet : " + this.toString());
	}
	}

}
