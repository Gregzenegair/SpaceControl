package fr.gh.spacecontrol;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

public class WaveMaker {

	private int wave;
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
		for (int x = 0; x < this.wave * 1.3f; x++) {
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

	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

}
