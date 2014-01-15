package fr.gh.spacecontrol;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.handler.IUpdateHandler;

public class GameLoopUpdateHandler implements IUpdateHandler {

	@Override
	public void onUpdate(float pSecondsElapsed) {
		GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;
		scene.cleaner();
		if (scene.shoot) {
			if (ShootingDelay.getSharedInstance().checkValidity()) {
				for (Tower tower : scene.towerList) {
					if(tower.isActive())
						tower.shoot();
				}
			}

		}

		Iterator<Enemy> itE = scene.enemyList.iterator();

		while (itE.hasNext()) {
			Enemy e = itE.next();
			e.moveCenter();
		}

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
