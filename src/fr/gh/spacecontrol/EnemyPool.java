package fr.gh.spacecontrol;

import org.andengine.entity.shape.IShape;
import org.andengine.util.adt.pool.GenericPool;

public class EnemyPool extends GenericPool<Enemy> {
	 
    public static EnemyPool instance;
 
    public static EnemyPool sharedEnemyPool() {
        if (instance == null)
            instance = new EnemyPool();
        return instance;
    }
 
    private EnemyPool() {
        super();
    }
 
    @Override
    protected Enemy onAllocatePoolItem() {
        return new Enemy();
    }
 
    protected void onHandleRecycleItem(final Enemy e) {
    	GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;

        scene.mPhysicsWorld.destroyBody(e.getBody());
        e.getSprite().clearEntityModifiers();
        e.getSprite().clearUpdateHandlers();
        e.getSprite().setVisible(false);
        e.getSprite().detachSelf();
    }
}
