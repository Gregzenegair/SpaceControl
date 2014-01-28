package fr.gh.spacecontrol;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;

import fr.gh.spacecontrol.ShootingDelay.Task;

public class GameLoopUpdateHandler implements IUpdateHandler {

	private int scoreTemp = 0;

	@Override
	public void onUpdate(float pSecondsElapsed) {
		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getmCurrentScene();
		scene.cleaner();

		// TODO
		if (this.scoreTemp != scene.getScoreValue()) {
			this.scoreTemp++;
		}
		
		scene.getScoreText().setText("Score : " + String.valueOf(this.scoreTemp));

		if (scene.isShoot()) {
			if (ShootingDelay.getSharedInstance().checkValidity()) {
				for (Tower tower : scene.getTowerList()) {
					if (tower.isActive())
						tower.shoot((int) tower.getAngle());
				}
			}

		}

		// System.out.println(WaveMaker.getSharedWaveMaker(scene).getWave());

		Iterator<Enemy> itE = scene.getEnemyList().iterator();
		while (itE.hasNext()) {
			Enemy e = itE.next();
			e.move();
		}

		if (scene.getEnemyList().isEmpty()) {
			WaveMaker waveMaker = WaveMaker.getSharedWaveMaker(scene);
			waveMaker.newWave();
		}

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}

// //
class TimedTask extends TimerTask {

	GameScene scene;

	public TimedTask(GameScene scene) {
		this.scene = scene;
	}

	@Override
	public void run() {
		WaveMaker waveMaker = WaveMaker.getSharedWaveMaker(scene);
		waveMaker.newWave();
	}
}
