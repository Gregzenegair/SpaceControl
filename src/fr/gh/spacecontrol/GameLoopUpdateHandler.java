package fr.gh.spacecontrol;

import org.andengine.engine.handler.IUpdateHandler;

public class GameLoopUpdateHandler implements IUpdateHandler {

	@Override
	public void onUpdate(float pSecondsElapsed) {
		((GameScene) BaseActivity.getSharedInstance().mCurrentScene).cleaner();
		GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;
		if (scene.shoot) {
			if (ShootingDelay.getSharedInstance().checkValidity())
				scene.tower1.shoot();
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
