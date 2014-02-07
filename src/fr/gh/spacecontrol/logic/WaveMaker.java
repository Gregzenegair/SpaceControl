package fr.gh.spacecontrol.logic;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import fr.gh.spacecontrol.items.Enemy;
import fr.gh.spacecontrol.pools.EnemyPool;
import fr.gh.spacecontrol.scenes.GameScene;

public class WaveMaker {

	private int wave;
	private int enemyCount;
	private int enemyDamagedCount;
	private GameScene scene;

	public static WaveMaker instance;

	public static WaveMaker getSharedWaveMaker(GameScene scene) {
		if (instance == null)
			instance = new WaveMaker(scene);
		return instance;
	}

	private WaveMaker(GameScene scene) {
		super();
		this.scene = scene;
	}

	public void newWave() {
		scene.registerUpdateHandler(new TimerHandler(2, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {

			}
		}));

		this.enemyCount = (int) (this.wave * 1.3f);
		for (int x = 0; x < this.enemyCount; x++) {
			Enemy enemy = EnemyPool.sharedEnemyPool().obtainPoolItem();

			enemy.init();

			enemy.getCockpit().getSprite().detachSelf();
			enemy.getGunship().getSprite().detachSelf();
			enemy.getReactorLeft().getSprite().detachSelf();
			enemy.getReactorRight().getSprite().detachSelf();

			scene.attachChild(enemy.getCockpit().getSprite());
			scene.attachChild(enemy.getGunship().getSprite());
			scene.attachChild(enemy.getReactorLeft().getSprite());
			scene.attachChild(enemy.getReactorRight().getSprite());

			scene.getEnemyList().add(enemy);

			enemy.getCockpit().getSprite().setVisible(true);
			enemy.getGunship().getSprite().setVisible(true);
			enemy.getReactorLeft().getSprite().setVisible(true);
			enemy.getReactorRight().getSprite().setVisible(true);

		}

		this.wave++;
	}

	public void trackDamaged() {
		LinkedList<Enemy> enemyList = scene.getEnemyList();
		Iterator<Enemy> eIt = enemyList.iterator();
		while (eIt.hasNext()) {
			Enemy enemy = (Enemy) eIt.next();
			if (enemy.isDamaged()) {
				this.enemyDamagedCount++;
			}
		}
	}

	public void resetTrackDamaged() {
		this.enemyDamagedCount = 0;
	}

	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

	public int getEnemyCount() {
		return enemyCount;
	}

	public void setEnemyCount(int enemyCount) {
		this.enemyCount = enemyCount;
	}

	public int getEnemyDamagedCount() {
		return enemyDamagedCount;
	}

	public void setEnemyDamagedCount(int enemyDamagedCount) {
		this.enemyDamagedCount = enemyDamagedCount;
	}

}
