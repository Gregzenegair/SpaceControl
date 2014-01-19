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
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import com.badlogic.gdx.math.Vector2;

import android.hardware.SensorManager;
import android.util.Log;

public class GameScene extends Scene implements IOnSceneTouchListener {

	private Camera mCamera;
	public Tower tower1;
	public Tower tower2;
	public Tower tower3;
	public Tower tower4;
	private float towerAxe;
	public boolean shoot;

	public Sound soundTowerGun;
	public Sound soundTowerGunb;
	public Sound soundImpact;
	public Music soundExplosion;

	public LinkedList<Tower> towerList;
	public LinkedList<Bullet> bulletList;
	public LinkedList<Enemy> enemyList;
	public LinkedList<Wreckage> wreckageList;

	private BaseActivity activity;
	private Text text1;
	public int bulletCount;
	public PhysicsWorld mPhysicsWorld;

	// TODO low : trouver pourquoi ça crash si scene dans le constructeur de
	// tower
	// TODO ajouter le bunker

	public GameScene() {
		activity = BaseActivity.getSharedInstance();

		soundTowerGun = BaseActivity.getSharedInstance().soundTowerGun;
		soundTowerGunb = BaseActivity.getSharedInstance().soundTowerGunb;
		soundImpact = BaseActivity.getSharedInstance().soundImpact;
		soundExplosion = BaseActivity.getSharedInstance().soundExplosion;

		mCamera = BaseActivity.getSharedInstance().mCamera;
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);


		
		this.towerList = new LinkedList<Tower>();
		this.bulletList = new LinkedList<Bullet>();
		this.enemyList = new LinkedList<Enemy>();
		this.wreckageList = new LinkedList<Wreckage>();

		this.buildTowers();

		this.setOnSceneTouchListener(this);

		for (int x = 0; x < 10; x++) {
			Enemy enemy = EnemyPool.sharedEnemyPool().obtainPoolItem();
			attachChild(enemy.sprite);
			this.enemyList.add(enemy);
		}

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
		Bunker bunker1 = new Bunker(tower1);
		tower1.setBunker(bunker1);

		tower2 = new Tower(towerSizeX, towerSizeY, towerList);
		tower2.setPosition(20, (int) mCamera.getHeight() - tower2.getHeight()
				* 2);
		Bunker bunker2 = new Bunker(tower2);
		tower2.setBunker(bunker2);

		tower3 = new Tower(towerSizeX, towerSizeY, towerList);
		tower3.setPosition((int) mCamera.getWidth() - tower3.getWidth() - 20,
				(int) mCamera.getHeight() - tower3.getHeight() * 2);
		Bunker bunker3 = new Bunker(tower3);
		tower3.setBunker(bunker3);

		tower4 = new Tower(towerSizeX, towerSizeY, towerList);
		tower4.setPosition((int) mCamera.getWidth() - tower4.getWidth() - 20,
				(int) mCamera.getHeight() - tower4.getHeight() - 240);
		Bunker bunker4 = new Bunker(tower4);
		tower4.setBunker(bunker4);

		for (Tower tower : towerList) {
			attachChild(tower.sprite);
			attachChild(tower.getBunker().sprite);
		}
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {

			int towerTouched;
			towerAxe = pSceneTouchEvent.getY();
			if ((towerTouched = isTouchingABunker(
					(int) pSceneTouchEvent.getX(),
					(int) pSceneTouchEvent.getY())) >= 0) {
				for (Tower tower : towerList) {
					tower.setActive(false);
				}
				this.towerList.get(towerTouched).setActive(true);
			} else {
				for (Tower tower : towerList) {
					if (tower.isActive())
						tower.setStartingAngle(tower.getAngle());
				}
				this.shoot = true;
			}
		}
		if (pSceneTouchEvent.isActionMove()) {
			for (Tower tower : towerList) {
				if (tower.isActive())
					tower.rotateTower(pSceneTouchEvent.getY(), towerAxe);
			}
		}
		if (pSceneTouchEvent.isActionUp()) {
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
						if (!e.gotHitnDestroyed()) {
							soundExplosion.play();
							particleEmitterExplosion.createExplosion(
									e.sprite.getX() + e.sprite.getWidth() / 2,
									e.sprite.getY() + e.sprite.getHeight() / 2,
									e.sprite.getParent(),
									BaseActivity.getSharedInstance(), 30, 3, 3,
									b.sprite.getRotation());
							
							e.addPhysics();
							
							/*
							EnemyPool.sharedEnemyPool().recyclePoolItem(e);
							eIt.remove();
							*/
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

	private int isTouchingABunker(int posX, int posY) {
		int marginSelection = 20;
		for (Tower tower : towerList) {
			if (posX < tower.getBunker().getPosX()
					+ tower.getBunker().getWidth() + marginSelection
					&& posX > tower.getBunker().getPosX() - marginSelection) {
				if (posY < tower.getBunker().getPosY()
						+ tower.getBunker().getHeight() + marginSelection
						&& posY > tower.getBunker().getPosY() - marginSelection) {
					return towerList.indexOf(tower);
				}
			}
		}
		return -1;
	}
	
	
}
