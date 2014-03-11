package fr.gh.spacecontrol.logic;

import java.util.Iterator;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.text.Text;

import fr.gh.spacecontrol.activities.BaseActivity;
import fr.gh.spacecontrol.items.Enemy;
import fr.gh.spacecontrol.items.ParticleEmitterExplosion;
import fr.gh.spacecontrol.items.Tower;
import fr.gh.spacecontrol.pools.EnemyPool;
import fr.gh.spacecontrol.scenes.GameScene;

public class GameLoopUpdateHandler implements IUpdateHandler {

	private int scoreTemp = 0;

	@Override
	public void onUpdate(float pSecondsElapsed) {
		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();
		scene.collisioner();

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
					if (tower.isActive() && !tower.isDestroyed()) {
						tower.shoot((int) tower.getAngle());
					}
				}
			}
		}

		// removes parts and enemies from list when completely destroyed
		if (CycleDelay.getSharedInstance().checkValidity()) {

			for (Tower tower : scene.getTowerList()) {
				if (tower.isDestroyed()) {
					ParticleEmitterExplosion.createSmoke(tower.getBunker().getSprite().getX() + tower.getBunker().getSprite().getWidth() / 2, tower.getBunker().getSprite().getY(), tower.getBunker()
							.getSprite().getParent(), scene.getActivity(), 4, 2, 2);
				}
			}

			Iterator<Enemy> eIt = scene.getEnemyList().iterator();
			System.out.println(" En  |  Co |  Rl |  Rr |  Gs");
			while (eIt.hasNext()) {
				Enemy enemy = eIt.next();

				this.damagingEnemies(enemy, eIt); // Damaging over time enemies
				// Moving enemies
				enemy.moveNShoot();

				String sEPhysic = (enemy.isDamaged()) ? "T" : "F";
				String sCoPhysic = Integer.toString(enemy.getCockpit().getHp());
				String sRlPhysic = Integer.toString(enemy.getReactorRight().getHp());
				String sRrPhysic = Integer.toString(enemy.getReactorLeft().getHp());
				String sGsPhysic = Integer.toString(enemy.getGunship().getHp());
				System.out.println("  " + sEPhysic + "  |  " + sCoPhysic + "  |  " + sRlPhysic + "  |  " + sRrPhysic + "  |  " + sGsPhysic);

			}
			// track number of damaged
			WaveMaker.getSharedWaveMaker(scene).trackDamaged();
			// create the waves of enemies
			WaveMaker.getSharedWaveMaker(scene).createNewWave();
		}

	}

	private void damagingEnemies(Enemy enemy, Iterator<Enemy> eIt) {
		if (enemy.isDamaged()) {
			if (enemy.getReactorLeft().getHp() == 0) {
				ParticleEmitterExplosion.createExplosion(enemy.getReactorLeft().getSprite().getX() + enemy.getReactorLeft().getSprite().getWidth() / 2, enemy.getReactorLeft().getSprite().getY()
						+ enemy.getReactorLeft().getSprite().getHeight() / 2, enemy.getReactorLeft().getSprite().getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
				enemy.getReactorLeft().remove();
			} else {
				enemy.getReactorLeft().setHp(enemy.getReactorLeft().getHp() - 1);
			}

			if (enemy.getReactorRight().getHp() == 0) {
				ParticleEmitterExplosion.createExplosion(enemy.getReactorRight().getSprite().getX() + enemy.getReactorRight().getSprite().getWidth() / 2, enemy.getReactorRight().getSprite().getY()
						+ enemy.getReactorRight().getSprite().getHeight() / 2, enemy.getReactorRight().getSprite().getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
				enemy.getReactorRight().remove();
			} else {
				enemy.getReactorRight().setHp(enemy.getReactorRight().getHp() - 1);
			}

			if (enemy.getGunship().getHp() == 0) {
				ParticleEmitterExplosion.createExplosion(enemy.getGunship().getSprite().getX() + enemy.getGunship().getSprite().getWidth() / 2, enemy.getGunship().getSprite().getY()
						+ enemy.getGunship().getSprite().getHeight() / 2, enemy.getGunship().getSprite().getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
				enemy.getGunship().remove();
			} else {
				enemy.getGunship().setHp(enemy.getGunship().getHp() - 1);
			}

			if (enemy.getCockpit().getHp() == 0) {
				ParticleEmitterExplosion.createExplosion(enemy.getCockpit().getSprite().getX() + enemy.getCockpit().getSprite().getWidth() / 2, enemy.getCockpit().getSprite().getY()
						+ enemy.getCockpit().getSprite().getHeight() / 2, enemy.getCockpit().getSprite().getParent(), BaseActivity.getSharedInstance(), 2, 3, 3, 0);
				enemy.getCockpit().remove();
			} else {
				enemy.getCockpit().setHp(enemy.getCockpit().getHp() - 1);
			}

			// Recycling of enemy
			if (enemy.getCockpit().isDestroyed() && enemy.getReactorLeft().isDestroyed() && enemy.getReactorRight().isDestroyed() && enemy.getGunship().isDestroyed()) {
				EnemyPool.sharedEnemyPool().recyclePoolItem(enemy);
				eIt.remove();
				System.out.println("Recycling Enemy");
			}
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
