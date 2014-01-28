package fr.gh.spacecontrol;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;

public class GameLoopUpdateHandler implements IUpdateHandler {

	@Override
	public void onUpdate(float pSecondsElapsed) {
		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getmCurrentScene();
		scene.cleaner();
		if (scene.isShoot()) {
			if (ShootingDelay.getSharedInstance().checkValidity()) {
				for (Tower tower : scene.getTowerList()) {
					if (tower.isActive())
						tower.shoot((int) tower.getAngle());
				}
			}

		}

		Iterator<Enemy> itE = scene.getEnemyList().iterator();
		Camera mCamera = BaseActivity.getSharedInstance().getmCamera();
		if (scene.getEnemyList().isEmpty()) {
			for (int x = 0; x < 10; x++) {
				System.out.println(" empty");
				Enemy enemy = EnemyPool.sharedEnemyPool().obtainPoolItem();
//enemy.init();
scene.attachChild(enemy.getSprite());
/*
				scene.attachChild(enemy.getSprite());
				enemy.getSprite().setVisible(true);
				enemy.setPhysic(false);
				enemy.setHp(enemy.MAX_HEALTH);				
				
				enemy.getSprite().setPosition(20, 20);
				enemy.setFinalPosX(20);
				enemy.setFinalPosY(20);
				*/
				scene.getEnemyList().add(enemy);
				
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
