package fr.gh.spacecontrol.pools;

import org.andengine.util.adt.pool.MultiPool;

import fr.gh.spacecontrol.items.enemyTypes.EnemyType001;
import fr.gh.spacecontrol.items.enemyTypes.EnemyType002;
import fr.gh.spacecontrol.scenes.BaseActivity;

public class EnemyMultiPool extends MultiPool {
    private BaseActivity   mContext;

    public static enum TYPE {
        ENEMY001, ENEMY002
    }

    public EnemyMultiPool(BaseActivity mContext) {
        super();
        this.mContext = mContext;
        this.registerPool(TYPE.ENEMY001.ordinal(), new EnemyType001());
        this.registerPool(TYPE.ENEMY002.ordinal(), new EnemyType002());
    }

    private void registerPool(int ordinal, EnemyType002 enemyType002) {
		// TODO Auto-generated method stub
		
	}

	private void registerPool(int ordinal, EnemyType001 enemyType001) {
		// TODO Auto-generated method stub
		
	}


}