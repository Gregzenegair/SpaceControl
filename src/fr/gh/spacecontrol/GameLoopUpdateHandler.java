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
					if (tower.isActive())
						tower.shoot((int) tower.getAngle());
				}
			}

		}

		Iterator<Enemy> itE = scene.enemyList.iterator();

		if (scene.enemyList.isEmpty()) {
			for (int x = 0; x < 10; x++) {
				Enemy enemy = EnemyPool.sharedEnemyPool().obtainPoolItem();
				scene.attachChild(enemy.sprite);
				scene.enemyList.add(enemy);
			}
		} else {
			while (itE.hasNext()) {
				Enemy e = itE.next();
				e.move();
			}
		}

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
