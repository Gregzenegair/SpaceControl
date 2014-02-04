package fr.gh.spacecontrol.logic;

import java.util.Iterator;

import org.andengine.engine.handler.IUpdateHandler;

import fr.gh.spacecontrol.entities.Enemy;
import fr.gh.spacecontrol.entities.EnemyPool;
import fr.gh.spacecontrol.entities.ParticleEmitterExplosion;
import fr.gh.spacecontrol.entities.Tower;
import fr.gh.spacecontrol.scenes.BaseActivity;
import fr.gh.spacecontrol.scenes.GameScene;

public class GameLoopUpdateHandler implements IUpdateHandler {

	private int scoreTemp = 0;

	@Override
	public void onUpdate(float pSecondsElapsed) {
		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getmCurrentScene();
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

		if (CycleDelay.getSharedInstance().checkValidity()) {
			Iterator<Enemy> eIt = scene.getEnemyList().iterator();
			while (eIt.hasNext()) {
				Enemy enemy = eIt.next();
				if (enemy.getReactorLeft().isPhysic() && !enemy.getReactorLeft().isDestroyed()) {
					enemy.getReactorLeft().setHp(enemy.getReactorLeft().getHp() - 1);
					if (enemy.getReactorLeft().getHp() == 0) {
						ParticleEmitterExplosion.createExplosion(enemy.getReactorLeft().getSprite().getX()
								+ enemy.getReactorLeft().getSprite().getWidth() / 2, enemy.getReactorLeft().getSprite()
								.getY()
								+ enemy.getReactorLeft().getSprite().getHeight() / 2, enemy.getReactorLeft()
								.getSprite().getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
						enemy.getReactorLeft().remove();
					}
				}
				if (enemy.getReactorRight().isPhysic() && !enemy.getReactorRight().isDestroyed()) {
					enemy.getReactorRight().setHp(enemy.getReactorRight().getHp() - 1);
					if (enemy.getReactorRight().getHp() == 0) {
						ParticleEmitterExplosion.createExplosion(enemy.getReactorRight().getSprite().getX()
								+ enemy.getReactorRight().getSprite().getWidth() / 2, enemy.getReactorRight()
								.getSprite().getY()
								+ enemy.getReactorRight().getSprite().getHeight() / 2, enemy.getReactorRight()
								.getSprite().getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
						enemy.getReactorRight().remove();
					}
				}
				if (enemy.getGunship().isPhysic() && !enemy.getGunship().isDestroyed()) {
					enemy.getGunship().setHp(enemy.getGunship().getHp() - 1);
					if (enemy.getGunship().getHp() == 0) {
						ParticleEmitterExplosion.createExplosion(enemy.getGunship().getSprite().getX()
								+ enemy.getGunship().getSprite().getWidth() / 2, enemy.getGunship().getSprite().getY()
								+ enemy.getGunship().getSprite().getHeight() / 2, enemy.getGunship().getSprite()
								.getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
						enemy.getGunship().remove();
					}
				}

				// Recyclage de l'enemy
				if (enemy.getCockpit().isDestroyed() && enemy.getReactorLeft().isDestroyed()
						&& enemy.getReactorRight().isDestroyed() && enemy.getGunship().isDestroyed()) {
					EnemyPool.sharedEnemyPool().recyclePoolItem(enemy);
					eIt.remove();
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
