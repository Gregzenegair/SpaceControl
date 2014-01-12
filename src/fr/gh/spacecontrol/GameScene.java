package fr.gh.spacecontrol;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

public class GameScene extends Scene implements IOnSceneTouchListener {

	private Camera mCamera;
	public Tower tower1;
	private Tower tower2;
	private Tower tower3;
	private Tower tower4;
	private TowerAxis towerAxe;
	public boolean shoot;

	public Sound soundTowerGun;
	public Sound soundTowerGunb;
	public Sound soundImpact;
	public Music soundExplosion;

	public LinkedList<Tower> towerList;
	public LinkedList<Bullet> bulletList;
	public LinkedList<Enemy> enemyList;

	private BaseActivity activity;
	private Text text1;
	public int bulletCount;

	public GameScene() {
		soundTowerGun = BaseActivity.getSharedInstance().soundTowerGun;
		soundTowerGunb = BaseActivity.getSharedInstance().soundTowerGunb;
		soundImpact = BaseActivity.getSharedInstance().soundImpact;
		soundExplosion = BaseActivity.getSharedInstance().soundExplosion;

		mCamera = BaseActivity.getSharedInstance().mCamera;
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		towerList = new LinkedList<Tower>();
		bulletList = new LinkedList<Bullet>();
		enemyList = new LinkedList<Enemy>();

		this.buildTowers();

		this.setOnSceneTouchListener(this);

		for (int x = 0; x < 10; x++) {
			Enemy enemy = EnemyPool.sharedEnemyPool().obtainPoolItem();
			attachChild(enemy.sprite);
			this.enemyList.add(enemy);
		}

		activity = BaseActivity.getSharedInstance();
		text1 = new Text(20, 20, activity.mFont, "Score : ",
				activity.getVertexBufferObjectManager());
		text1.setScale(0.5f);
		attachChild(text1);

		// Enregistrement d'un update handler
		registerUpdateHandler(new GameLoopUpdateHandler());
	}

	private void buildTowers() {
		int towerSizeX = 10;
		int towerSizeY = 40;

		tower1 = new Tower(towerSizeX, towerSizeY, towerList);
		tower1.setPosition(20, (int) mCamera.getHeight() - tower1.getHeight()
				- 240);

		tower2 = new Tower(towerSizeX, towerSizeY, towerList);
		tower2.setPosition(20, (int) mCamera.getHeight() - tower2.getHeight()
				* 2);

		tower3 = new Tower(towerSizeX, towerSizeY, towerList);
		tower3.setPosition((int) mCamera.getWidth() - tower3.getWidth() - 20,
				(int) mCamera.getHeight() - tower3.getHeight() * 2);

		tower4 = new Tower(towerSizeX, towerSizeY, towerList);
		tower4.setPosition((int) mCamera.getWidth() - tower4.getWidth() - 20,
				(int) mCamera.getHeight() - tower4.getHeight() - 240);

		for (Tower tower : towerList) {
			attachChild(tower.sprite);
			attachChild(tower.spriteBase);
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			towerAxe = new TowerAxis(pSceneTouchEvent.getX(),
					pSceneTouchEvent.getY());
			tower1.setStartingAngle(tower1.getAngle());
			this.shoot = true;
		}
		if (pSceneTouchEvent.isActionMove()) {
			tower1.rotateTower(pSceneTouchEvent.getY(), towerAxe.getStartingY());

		}
		if (pSceneTouchEvent.isActionUp()) {
			towerAxe = null;
			this.shoot = false;
		}

		return false;
	}

	public void cleaner() {
		synchronized (this) {

			Iterator<Enemy> eIt = enemyList.iterator();
			while (eIt.hasNext()) {
				Enemy e = eIt.next();
				Iterator<Bullet> it = bulletList.iterator();
				while (it.hasNext()) {
					Bullet b = it.next();
					if (b.sprite.getY() <= -b.sprite.getHeight()
							|| b.sprite.getX() <= -b.sprite.getHeight()

							|| b.sprite.getX() >= -b.sprite.getHeight()
									+ mCamera.getWidth()) {
						BulletPool.sharedBulletPool().recyclePoolItem(b);
						it.remove();
						continue;
					}

					if (b.sprite.collidesWith(e.sprite)) {
						if (!e.gotHit()) {
							soundExplosion.play();
							particleEmitterExplosion.createExplosion(
									e.sprite.getX() + e.sprite.getWidth() / 2,
									e.sprite.getY() + e.sprite.getHeight() / 2,
									e.sprite.getParent(),
									BaseActivity.getSharedInstance(), 30, 3, 3,
									b.sprite.getRotation());
							EnemyPool.sharedEnemyPool().recyclePoolItem(e);
							eIt.remove();
						}
						soundImpact.play();
						particleEmitterExplosion.createBulletImpact(
								b.sprite.getX() + b.sprite.getWidth() / 2,
								b.sprite.getY() + b.sprite.getHeight() / 2,
								b.sprite.getParent(),
								BaseActivity.getSharedInstance(), Color.BLACK,
								1, 2, 3, b.sprite.getRotation());
						BulletPool.sharedBulletPool().recyclePoolItem(b);
						it.remove();
					}
				}
			}
		}
	}

}
