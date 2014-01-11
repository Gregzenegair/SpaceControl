package fr.gh.spacecontrol;

import org.andengine.util.adt.pool.GenericPool;

public class EnemyPool extends GenericPool<Enemy1> {

	public static EnemyPool instance;
	
    public static EnemyPool sharedEnemyPool() {
        if (instance == null)
            instance = new EnemyPool();
        return instance;
    }
	
	@Override
	protected Enemy1 onAllocatePoolItem() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onHandleObtainItem(Enemy1 pItem) {
	    pItem.init();
	}
	/** Called when a projectile is sent to the pool */
	protected void onHandleRecycleItem(final Enemy1 e) {
	    e.sprite.setVisible(false);
	    e.sprite.detachSelf();
	    e.clean();
	}
	
}
