package fr.gh.spacecontrol.logic;

import java.util.Iterator;

import org.andengine.engine.handler.IUpdateHandler;

import fr.gh.spacecontrol.items.Enemy;
import fr.gh.spacecontrol.items.ParticleEmitterExplosion;
import fr.gh.spacecontrol.items.Tower;
import fr.gh.spacecontrol.pools.EnemyPool;
import fr.gh.spacecontrol.scenes.BaseActivity;
import fr.gh.spacecontrol.scenes.GameScene;

public class GameLoopUpdateHandler implements IUpdateHandler {

	private int scoreTemp = 0;

	@Override
	public void onUpdate(float pSecondsElapsed) {
		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();
		scene.collisionerAndCleaner();

		if (this.scoreTemp + 100 < scene.getScoreValue()) {
			this.scoreTemp += 100;
		} else if (this.scoreTemp != scene.getScoreValue()) {
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
					if (tower.isActive()) {
						tower.shoot((int) tower.getAngle());
					}
				}
			} else {
				for (Tower tower : scene.getTowerList()) {
					tower.getSprite().setScaleY(tower.getScaleYSaved());
				}
			}
		}
		
		// track number of damaged
		WaveMaker.getSharedWaveMaker(scene).trackDamaged();
		// create the waves of enemies
		WaveMaker.getSharedWaveMaker(scene).createNewWave();

		// removes parts and enemies from list when completely destroyed
		if (CycleDelay.getSharedInstance().checkValidity()) {
			Iterator<Enemy> eIt = scene.getEnemyList().iterator();
			while (eIt.hasNext()) {
				Enemy enemy = eIt.next();
//
//				if (enemy.getReactorLeft().getHp() == 0) {
//					ParticleEmitterExplosion.createExplosion(enemy.getReactorLeft().getSprite().getX()
//							+ enemy.getReactorLeft().getSprite().getWidth() / 2, enemy.getReactorLeft().getSprite()
//							.getY()
//							+ enemy.getReactorLeft().getSprite().getHeight() / 2, enemy.getReactorLeft().getSprite()
//							.getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
//					enemy.getReactorLeft().remove();
//				}
//
//				if (enemy.getReactorRight().getHp() == 0) {
//					ParticleEmitterExplosion.createExplosion(enemy.getReactorRight().getSprite().getX()
//							+ enemy.getReactorRight().getSprite().getWidth() / 2, enemy.getReactorRight().getSprite()
//							.getY()
//							+ enemy.getReactorRight().getSprite().getHeight() / 2, enemy.getReactorRight().getSprite()
//							.getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
//					enemy.getReactorRight().remove();
//				}
//
//				if (enemy.getGunship().getHp() == 0) {
//					ParticleEmitterExplosion.createExplosion(enemy.getGunship().getSprite().getX()
//							+ enemy.getGunship().getSprite().getWidth() / 2, enemy.getGunship().getSprite().getY()
//							+ enemy.getGunship().getSprite().getHeight() / 2, enemy.getGunship().getSprite()
//							.getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
//					enemy.getGunship().remove();
//				}
//
//				// Recycling of enemy
//				if (enemy.getCockpit().isDestroyed() && enemy.getReactorLeft().isDestroyed()
//						&& enemy.getReactorRight().isDestroyed() && enemy.getGunship().isDestroyed()) {
//					EnemyPool.sharedEnemyPool().recyclePoolItem(enemy);
//					eIt.remove();
//				}

				// Moving enemies
				enemy.moveNShoot();

			}
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
