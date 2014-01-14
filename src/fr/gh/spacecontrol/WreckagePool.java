package fr.gh.spacecontrol;

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
		wreckage.sprite.clearEntityModifiers();
		wreckage.sprite.clearUpdateHandlers();
		wreckage.sprite.setVisible(false);
		wreckage.sprite.detachSelf();
	}

}
