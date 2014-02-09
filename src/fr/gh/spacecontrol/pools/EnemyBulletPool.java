package fr.gh.spacecontrol.pools;

import org.andengine.util.adt.pool.GenericPool;

import fr.gh.spacecontrol.items.EnemyBullet;


public class EnemyBulletPool extends GenericPool<EnemyBullet> {

	public static EnemyBulletPool instance;

	public static EnemyBulletPool sharedBulletPool() {
		if (instance == null)
			instance = new EnemyBulletPool();
		return instance;
	}

	private EnemyBulletPool() {
		super();
	}

	@Override
	protected EnemyBullet onAllocatePoolItem() {
		return new EnemyBullet();
	}

	protected void onHandleRecycleItem(final EnemyBullet b) {
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
