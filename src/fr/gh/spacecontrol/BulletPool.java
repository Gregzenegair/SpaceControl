package fr.gh.spacecontrol;

import org.andengine.util.adt.pool.GenericPool;

public class BulletPool extends GenericPool<Bullet> {

	public static BulletPool instance;

	public static BulletPool sharedBulletPool() {
		if (instance == null)
			instance = new BulletPool();
		return instance;
	}

	private BulletPool() {
		super();
	}

	@Override
	protected Bullet onAllocatePoolItem() {
		return new Bullet();
	}

	protected void onHandleRecycleItem(final Bullet b) {
		try {

			b.getSprite().clearEntityModifiers();
			b.getSprite().clearUpdateHandlers();
			b.getSprite().setVisible(false);
			b.getSprite().detachSelf();

		} catch (Exception e) {
			System.err.println(b + " Objet : " + this.toString());
		}
	}
}
