package fr.gh.spacecontrol;

import java.util.Iterator;
import java.util.LinkedList;

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

		Iterator<Enemy> itE = scene.enemyList.iterator();

		while (itE.hasNext()) {
			Enemy e = itE.next();
			e.move();
		}

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
