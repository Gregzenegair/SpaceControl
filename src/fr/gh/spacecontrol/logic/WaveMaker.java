package fr.gh.spacecontrol.logic;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.MoveYModifier;

import fr.gh.spacecontrol.items.Enemy;
import fr.gh.spacecontrol.pools.EnemyPool;
import fr.gh.spacecontrol.scenes.BaseActivity;
import fr.gh.spacecontrol.scenes.GameScene;

public class WaveMaker {

	private int wave;
	private boolean creatingWave;
	private int enemyDamagedCount;
	private int enemyCount;
	private GameScene scene;
	BaseActivity activity;

	public static WaveMaker instance;

	public static WaveMaker getSharedWaveMaker(GameScene scene) {
		if (instance == null)
			instance = new WaveMaker(scene);
		return instance;
	}

	private WaveMaker(GameScene scene) {
		super();
		this.scene = scene;
		this.creatingWave = false;
		this.wave = 1;
	}

	public void createNewWave() {
		if (getEnemyDamagedCount() == scene.getEnemyList().size() && !creatingWave) {
			creatingWave = true;
			newWave();
		}
	}

	// Here we have wave word to put into strings
	public void newWave() {
		activity = BaseActivity.getSharedInstance();
		enemyCount = (int) (this.wave * 1.3f);
		scene.registerUpdateHandler(new TimerHandler(1, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				scene.getWaveText().setText("Wave " + Integer.toString(getWave() - 1));
				scene.getWaveText().setPosition(scene.getmCamera().getWidth() / 2 - scene.getWaveText().getWidth() / 2,
						scene.getmCamera().getHeight() / 2);
				scene.getWaveText().registerEntityModifier(
						new MoveYModifier(1, scene.getWaveText().getY(), scene.getmCamera().getHeight() / 2));
			}
		}));

		scene.registerUpdateHandler(new TimerHandler(3, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				scene.getWaveText().registerEntityModifier(new MoveYModifier(1, scene.getWaveText().getY(), -100));
			}
		}));

		scene.registerUpdateHandler(new TimerHandler(6, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				for (int x = 0; x < enemyCount; x++) {
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
				creatingWave = false;
			}
		}));
		this.wave++;
	}

	public void trackDamaged() {
		this.enemyDamagedCount = 0;
		LinkedList<Enemy> enemyList = scene.getEnemyList();
		Iterator<Enemy> eIt = enemyList.iterator();
		while (eIt.hasNext()) {
			Enemy enemy = (Enemy) eIt.next();
			if (enemy.isDamaged()) {
				this.enemyDamagedCount++;
			}
		}
	}

	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

	public int getEnemyDamagedCount() {
		return enemyDamagedCount;
	}

	public void setEnemyDamagedCount(int enemyDamagedCount) {
		this.enemyDamagedCount = enemyDamagedCount;
	}

	public int getEnemyCount() {
		return enemyCount;
	}

	public void setEnemyCount(int enemyCount) {
		this.enemyCount = enemyCount;
	}

}
