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

		if (this.scoreTemp + 100 < scene.getScoreValue()) {
			this.scoreTemp += 100;
		}
		if (this.scoreTemp != scene.getScoreValue()) {
			this.scoreTemp++;
		}
		String textScore = String.valueOf(this.scoreTemp);
		while (textScore.length() < 10) {
			textScore = "0" + textScore;
		}

		scene.getScoreText().setText(textScore);
		

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
			//++DEBUG
			scene.getScoreText().setText(String.valueOf(e.getSprite().getRotation()));
			//--DEBUG			
			e.moveCenter();
			e.getReactorLeft().move();
			e.getReactorRight().move();
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
