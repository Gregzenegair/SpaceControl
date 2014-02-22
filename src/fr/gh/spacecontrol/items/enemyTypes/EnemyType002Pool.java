package fr.gh.spacecontrol.items.enemyTypes;

import org.andengine.util.adt.pool.GenericPool;

import fr.gh.spacecontrol.items.Enemy;

public class EnemyType002Pool extends GenericPool<Enemy> {

	public static EnemyType002Pool instance;

	public static EnemyType002Pool sharedEnemyPool() {
		if (instance == null) {
			instance = new EnemyType002Pool();
		}
		return instance;
	}

	private EnemyType002Pool() {
		super();

	}

	protected Enemy onAllocatePoolItem() {
		return new EnemyType002();

	}

	@Override
	protected void onHandleRecycleItem(final Enemy e) {
		try {
			e.remove();
		} catch (Exception ex) {
			System.err.println(ex + " Objet : " + this.toString());
		}
	}
}
