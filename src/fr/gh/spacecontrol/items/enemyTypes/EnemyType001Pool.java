package fr.gh.spacecontrol.items.enemyTypes;

import org.andengine.util.adt.pool.GenericPool;

import fr.gh.spacecontrol.items.Enemy;

public class EnemyType001Pool extends GenericPool<Enemy> {

	public static EnemyType001Pool instance;

	public static EnemyType001Pool sharedEnemyPool() {
		if (instance == null) {
			instance = new EnemyType001Pool();
		}
		return instance;
	}

	private EnemyType001Pool() {
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
