package fr.gh.spacecontrol.pools;

import org.andengine.util.adt.pool.GenericPool;

import fr.gh.spacecontrol.items.Enemy;
import fr.gh.spacecontrol.items.enemyTypes.EnemyType001;
import fr.gh.spacecontrol.items.enemyTypes.EnemyType002;

public class EnemyPool extends GenericPool<Enemy> {

	public static EnemyPool instance;

	public static EnemyPool sharedEnemyPool() {
		if (instance == null) {
			instance = new EnemyPool();
		}
		return instance;
	}

	private EnemyPool() {
		super();

	}

	protected Enemy onAllocatePoolItem() {
		return new EnemyType001();

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
