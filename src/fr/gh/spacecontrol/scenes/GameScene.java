package fr.gh.spacecontrol.scenes;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.audio.music.Music;
import org.andengine.audio.sound.Sound;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.color.Color;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import fr.gh.spacecontrol.items.Bullet;
import fr.gh.spacecontrol.items.Bunker;
import fr.gh.spacecontrol.items.Cockpit;
import fr.gh.spacecontrol.items.Enemy;
import fr.gh.spacecontrol.items.Gunship;
import fr.gh.spacecontrol.items.ParticleEmitterExplosion;
import fr.gh.spacecontrol.items.Reactor;
import fr.gh.spacecontrol.items.Tower;
import fr.gh.spacecontrol.items.Wreckage;
import fr.gh.spacecontrol.logic.GameLoopUpdateHandler;
import fr.gh.spacecontrol.logic.WaveMaker;
import fr.gh.spacecontrol.pools.BulletPool;
import fr.gh.spacecontrol.pools.CockpitPool;
import fr.gh.spacecontrol.pools.EnemyPool;
import fr.gh.spacecontrol.pools.GunshipPool;
import fr.gh.spacecontrol.pools.ReactorPool;

public class GameScene extends Scene implements IOnSceneTouchListener {

	private Camera mCamera;
	private Tower tower1;
	private Tower tower2;
	private Tower tower3;
	private Tower tower4;
	private float towerAxe;
	private boolean shoot;

	private Sound soundTowerGun;
	private Sound soundTowerGunb;
	private Sound soundImpact;
	private Music soundExplosion;

	private LinkedList<Tower> towerList;
	private LinkedList<Bullet> bulletList;
	private LinkedList<Wreckage> wreckageList;
	private LinkedList<Enemy> enemyList;

	private BaseActivity activity;
	private Text scoreText;
	private Text debugText;
	private int scoreValue = 0;
	private int bulletCount;
	public PhysicsWorld mPhysicsWorld;

	public GameScene() {
		activity = BaseActivity.getSharedInstance();
		activity.setCurrentScreen(BaseActivity.GAME_SCREEN);
		activity.setGameStarted(true);
		soundTowerGun = BaseActivity.getSharedInstance().getSoundTowerGun();
		soundTowerGunb = BaseActivity.getSharedInstance().getSoundTowerGunb();
		soundImpact = BaseActivity.getSharedInstance().getSoundImpact();
		soundExplosion = BaseActivity.getSharedInstance().getSoundExplosion();

		mCamera = BaseActivity.getSharedInstance().getmCamera();
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		WaveMaker.getSharedWaveMaker(this);
		// reset des vagues
		WaveMaker.instance = null;

		this.towerList = new LinkedList<Tower>();
		this.bulletList = new LinkedList<Bullet>();
		this.wreckageList = new LinkedList<Wreckage>();
		this.enemyList = new LinkedList<Enemy>();

		this.buildTowers();
		this.creatingWalls();

		this.setOnSceneTouchListener(this);

		scoreText = new Text(0, 0, activity.getmFont(), "000000000" + String.valueOf(scoreValue), 12,
				activity.getVertexBufferObjectManager());
		scoreText.setPosition(mCamera.getWidth() / 2 - scoreText.getWidth() / 2, 20);
		scoreText.setScale(0.25f);
		attachChild(scoreText);

		debugText = new Text(0, 0, activity.getmFont(), "DEBUG", 12, activity.getVertexBufferObjectManager());
		debugText.setPosition(mCamera.getWidth() / 2 - debugText.getWidth() / 2, 60);
		debugText.setScale(0.25f);
		attachChild(debugText);

		// Enregistrement d'un update handler
		registerUpdateHandler(new GameLoopUpdateHandler());
		registerUpdateHandler(this.mPhysicsWorld);
	}

	private void buildTowers() {
		int towerSizeX = 10;
		int towerSizeY = 40;

		tower1 = new Tower(towerSizeX, towerSizeY, towerList);
		tower1.setPosition(10, (int) (mCamera.getHeight() - tower1.getSprite().getHeight() - 240));
		Bunker bunker1 = new Bunker(tower1);
		tower1.setBunker(bunker1);

		tower2 = new Tower(towerSizeX, towerSizeY, towerList);
		tower2.setPosition(120, (int) (mCamera.getHeight() - tower2.getSprite().getHeight() * 2));
		Bunker bunker2 = new Bunker(tower2);
		tower2.setBunker(bunker2);

		tower3 = new Tower(towerSizeX, towerSizeY, towerList);
		tower3.setPosition((int) (mCamera.getWidth() - tower3.getSprite().getHeight() - 120),
				(int) (mCamera.getHeight() - tower3.getSprite().getHeight() * 2));
		Bunker bunker3 = new Bunker(tower3);
		tower3.setBunker(bunker3);

		tower4 = new Tower(towerSizeX, towerSizeY, towerList);
		tower4.setPosition((int) (mCamera.getWidth() - tower4.getSprite().getHeight() - 10), (int) (mCamera.getHeight()
				- tower4.getSprite().getHeight() - 240));
		Bunker bunker4 = new Bunker(tower4);
		tower4.setBunker(bunker4);

		for (Tower tower : towerList) {
			attachChild(tower.getSprite());
			attachChild(tower.getBunker().getSprite());
		}

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, final TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {

			int towerTouched;
			towerAxe = pSceneTouchEvent.getY();
			if ((towerTouched = isTouchingABunker((int) pSceneTouchEvent.getX(), (int) pSceneTouchEvent.getY())) >= 0) {
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

	public void collisionerAndCleaner() {
		synchronized (this) {
			Iterator<Enemy> eIt = enemyList.iterator();
			while (eIt.hasNext()) {
				Enemy e = eIt.next();
				Iterator<Bullet> bIt = bulletList.iterator();
				while (bIt.hasNext()) {
					Bullet b = bIt.next();
					if (b.getSprite().getY() <= -b.getSprite().getHeight()
							|| b.getSprite().getX() <= -b.getSprite().getHeight()

							|| b.getSprite().getX() >= -b.getSprite().getHeight() + mCamera.getWidth()) {
						BulletPool.sharedBulletPool().recyclePoolItem(b);
						bIt.remove();
						continue;
					}

					if (b.getSprite().collidesWith(e.getCockpit().getSprite()) && !e.getCockpit().isDestroyed()) {
						damagingElement(b, e.getCockpit(), bIt, eIt);
					} else if (b.getSprite().collidesWith(e.getReactorLeft().getSprite())
							&& !e.getReactorLeft().isDestroyed()) {
						damagingElement(b, e.getReactorLeft(), bIt, eIt);
					} else if (b.getSprite().collidesWith(e.getReactorRight().getSprite())
							&& !e.getReactorRight().isDestroyed()) {
						damagingElement(b, e.getReactorRight(), bIt, eIt);
					} else if (b.getSprite().collidesWith(e.getGunship().getSprite()) && !e.getGunship().isDestroyed()) {
						damagingElement(b, e.getGunship(), bIt, eIt);
					}
				}
			}
		}
	}

	private void damagingElement(Bullet b, Object element, Iterator<Bullet> bIt, Iterator<Enemy> eIt) {
		if (element.getClass().getSimpleName().equals("Cockpit")) {
			Cockpit c = (Cockpit) element;
			Enemy e = c.getEnemy();
			if (c.gotHitnDestroyed(b.getAngle()) == 1 && !c.isPhysic()) {
					ParticleEmitterExplosion.createExplosion(c.getSprite().getX() + c.getSprite().getWidth() / 2, c
							.getSprite().getY() + c.getSprite().getHeight() / 2, c.getSprite().getParent(),
							BaseActivity.getSharedInstance(), 4, 3, 3, b.getSprite().getRotation());
					e.addPhysics();
			} else if (c.gotHitnDestroyed(b.getAngle()) == 0) {
				soundExplosion.play();
				ParticleEmitterExplosion.createExplosion(c.getSprite().getX() + c.getSprite().getWidth() / 2, c
						.getSprite().getY() + c.getSprite().getHeight() / 2, c.getSprite().getParent(),
						BaseActivity.getSharedInstance(), 8, 3, 3, b.getSprite().getRotation());

				CockpitPool.sharedCockpitPool().recyclePoolItem(c);
				this.scoreValue += c.getScoreValue();

			}
			soundImpact.play();
			ParticleEmitterExplosion.createBulletImpact(b.getSprite().getX() + b.getSprite().getWidth() / 2, b
					.getSprite().getY() + b.getSprite().getHeight() / 2, b.getSprite().getParent(),
					BaseActivity.getSharedInstance(), Color.BLACK, 1, 2, 3, b.getSprite().getRotation());
			BulletPool.sharedBulletPool().recyclePoolItem(b);
			bIt.remove();

			// Recyclage de l'enemy
			if (e.getCockpit().isDestroyed() && e.getReactorLeft().isDestroyed() && e.getReactorRight().isDestroyed()
					&& e.getGunship().isDestroyed()) {
				EnemyPool.sharedEnemyPool().recyclePoolItem(e);
				eIt.remove();
			}

		} else if (element.getClass().getSimpleName().equals("Reactor")) {
			Reactor r = (Reactor) element;
			Enemy e = r.getEnemy();
			if (r.gotHitnDestroyed(b.getAngle()) == 1 && !r.isPhysic()) {
					ParticleEmitterExplosion.createExplosion(r.getSprite().getX() + r.getSprite().getWidth() / 2, r
							.getSprite().getY() + r.getSprite().getHeight() / 2, r.getSprite().getParent(),
							BaseActivity.getSharedInstance(), 2, 3, 3, b.getSprite().getRotation());
					e.addPhysics();
			} else if (r.gotHitnDestroyed(b.getAngle()) == 0) {
				soundExplosion.play();
				ParticleEmitterExplosion.createExplosion(r.getSprite().getX() + r.getSprite().getWidth() / 2, r
						.getSprite().getY() + r.getSprite().getHeight() / 2, r.getSprite().getParent(),
						BaseActivity.getSharedInstance(), 2, 3, 3, b.getSprite().getRotation());

				ReactorPool.sharedReactorPool().recyclePoolItem(r);
				this.scoreValue += r.getScoreValue();

			}
			soundImpact.play();
			ParticleEmitterExplosion.createBulletImpact(b.getSprite().getX() + b.getSprite().getWidth() / 2, b
					.getSprite().getY() + b.getSprite().getHeight() / 2, b.getSprite().getParent(),
					BaseActivity.getSharedInstance(), Color.BLACK, 1, 2, 3, b.getSprite().getRotation());
			BulletPool.sharedBulletPool().recyclePoolItem(b);
			bIt.remove();

			// Recyclage de l'enemy
			if (e.getCockpit().isDestroyed() && e.getReactorLeft().isDestroyed() && e.getReactorRight().isDestroyed()
					&& e.getGunship().isDestroyed()) {
				EnemyPool.sharedEnemyPool().recyclePoolItem(e);
				eIt.remove();
			}
		} else if (element.getClass().getSimpleName().equals("Gunship")) {
			Gunship gs = (Gunship) element;
			Enemy e = gs.getEnemy();
			if (gs.gotHitnDestroyed(b.getAngle()) == 1 && !gs.isPhysic()) {
					ParticleEmitterExplosion.createExplosion(gs.getSprite().getX() + gs.getSprite().getWidth() / 2, gs
							.getSprite().getY() + gs.getSprite().getHeight() / 2, gs.getSprite().getParent(),
							BaseActivity.getSharedInstance(), 2, 3, 3, b.getSprite().getRotation());
					e.addPhysics();
			} else if (gs.gotHitnDestroyed(b.getAngle()) == 0) {
				soundExplosion.play();
				ParticleEmitterExplosion.createExplosion(gs.getSprite().getX() + gs.getSprite().getWidth() / 2, gs
						.getSprite().getY() + gs.getSprite().getHeight() / 2, gs.getSprite().getParent(),
						BaseActivity.getSharedInstance(), 2, 3, 3, b.getSprite().getRotation());

				GunshipPool.sharedGunshipPool().recyclePoolItem(gs);
				this.scoreValue += gs.getScoreValue();

			}
			soundImpact.play();
			ParticleEmitterExplosion.createBulletImpact(b.getSprite().getX() + b.getSprite().getWidth() / 2, b
					.getSprite().getY() + b.getSprite().getHeight() / 2, b.getSprite().getParent(),
					BaseActivity.getSharedInstance(), Color.BLACK, 1, 2, 3, b.getSprite().getRotation());
			BulletPool.sharedBulletPool().recyclePoolItem(b);
			bIt.remove();

			// Recyclage de l'enemy
			if (e.getCockpit().isDestroyed() && e.getReactorLeft().isDestroyed() && e.getReactorRight().isDestroyed()
					&& e.getGunship().isDestroyed()) {
				EnemyPool.sharedEnemyPool().recyclePoolItem(e);
				eIt.remove();
			}
		}

	}

	private int isTouchingABunker(int posX, int posY) {
		final int MARGIN_SELECTION = 20;
		for (Tower tower : towerList) {
			if (posX < tower.getBunker().getSprite().getX() + tower.getBunker().getSprite().getWidth()
					+ MARGIN_SELECTION
					&& posX > tower.getBunker().getSprite().getX() - MARGIN_SELECTION) {
				if (posY < tower.getBunker().getSprite().getY() + tower.getBunker().getSprite().getHeight()
						+ MARGIN_SELECTION
						&& posY > tower.getBunker().getSprite().getY() - MARGIN_SELECTION) {
					return towerList.indexOf(tower);
				}
			}
		}
		return -1;
	}

	private void creatingWalls() {
		final Rectangle ground = new Rectangle(0, BaseActivity.CAMERA_HEIGHT - 52, BaseActivity.CAMERA_WIDTH, 2,
				BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		final Rectangle roof = new Rectangle(0, 0, BaseActivity.CAMERA_WIDTH, 2, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		final Rectangle left = new Rectangle(0, 0, 2, BaseActivity.CAMERA_HEIGHT, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		final Rectangle right = new Rectangle(BaseActivity.CAMERA_WIDTH - 2, 0, 2, BaseActivity.CAMERA_HEIGHT,
				BaseActivity.getSharedInstance().getVertexBufferObjectManager());

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.1f, 0.1f);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

	}

	public Camera getmCamera() {
		return mCamera;
	}

	public void setmCamera(Camera mCamera) {
		this.mCamera = mCamera;
	}

	public Tower getTower1() {
		return tower1;
	}

	public void setTower1(Tower tower1) {
		this.tower1 = tower1;
	}

	public Tower getTower2() {
		return tower2;
	}

	public void setTower2(Tower tower2) {
		this.tower2 = tower2;
	}

	public Tower getTower3() {
		return tower3;
	}

	public void setTower3(Tower tower3) {
		this.tower3 = tower3;
	}

	public Tower getTower4() {
		return tower4;
	}

	public void setTower4(Tower tower4) {
		this.tower4 = tower4;
	}

	public float getTowerAxe() {
		return towerAxe;
	}

	public void setTowerAxe(float towerAxe) {
		this.towerAxe = towerAxe;
	}

	public boolean isShoot() {
		return shoot;
	}

	public void setShoot(boolean shoot) {
		this.shoot = shoot;
	}

	public Sound getSoundTowerGun() {
		return soundTowerGun;
	}

	public void setSoundTowerGun(Sound soundTowerGun) {
		this.soundTowerGun = soundTowerGun;
	}

	public Sound getSoundTowerGunb() {
		return soundTowerGunb;
	}

	public void setSoundTowerGunb(Sound soundTowerGunb) {
		this.soundTowerGunb = soundTowerGunb;
	}

	public Sound getSoundImpact() {
		return soundImpact;
	}

	public void setSoundImpact(Sound soundImpact) {
		this.soundImpact = soundImpact;
	}

	public Music getSoundExplosion() {
		return soundExplosion;
	}

	public void setSoundExplosion(Music soundExplosion) {
		this.soundExplosion = soundExplosion;
	}

	public LinkedList<Tower> getTowerList() {
		return towerList;
	}

	public void setTowerList(LinkedList<Tower> towerList) {
		this.towerList = towerList;
	}

	public LinkedList<Bullet> getBulletList() {
		return bulletList;
	}

	public void setBulletList(LinkedList<Bullet> bulletList) {
		this.bulletList = bulletList;
	}

	public LinkedList<Enemy> getEnemyList() {
		return enemyList;
	}

	public void setEnemyList(LinkedList<Enemy> enemyList) {
		this.enemyList = enemyList;
	}

	public LinkedList<Wreckage> getWreckageList() {
		return wreckageList;
	}

	public void setWreckageList(LinkedList<Wreckage> wreckageList) {
		this.wreckageList = wreckageList;
	}

	public BaseActivity getActivity() {
		return activity;
	}

	public void setActivity(BaseActivity activity) {
		this.activity = activity;
	}

	public Text getText1() {
		return scoreText;
	}

	public void setText1(Text text1) {
		this.scoreText = text1;
	}

	public int getBulletCount() {
		return bulletCount;
	}

	public void setBulletCount(int bulletCount) {
		this.bulletCount = bulletCount;
	}

	public PhysicsWorld getmPhysicsWorld() {
		return mPhysicsWorld;
	}

	public void setmPhysicsWorld(PhysicsWorld mPhysicsWorld) {
		this.mPhysicsWorld = mPhysicsWorld;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public Text getScoreText() {
		return scoreText;
	}

	public void setScoreText(Text scoreText) {
		this.scoreText = scoreText;
	}

	public Text getDebugText() {
		return debugText;
	}

	public void setDebugText(Text debugText) {
		this.debugText = debugText;
	}

}
