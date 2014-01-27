package fr.gh.spacecontrol;

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
	private LinkedList<Enemy> enemyList;
	private LinkedList<Wreckage> wreckageList;

	private BaseActivity activity;
	private Text text1;
	private int bulletCount;
	public PhysicsWorld mPhysicsWorld;

	// TODO low : trouver pourquoi ça crash si scene dans le constructeur de
	// tower

	public GameScene() {
		activity = BaseActivity.getSharedInstance();
		activity.setCurrentScreen(activity.GAME_SCREEN);
		activity.setGameStarted(true);
		soundTowerGun = BaseActivity.getSharedInstance().getSoundTowerGun();
		soundTowerGunb = BaseActivity.getSharedInstance().getSoundTowerGunb();
		soundImpact = BaseActivity.getSharedInstance().getSoundImpact();
		soundExplosion = BaseActivity.getSharedInstance().getSoundExplosion();

		mCamera = BaseActivity.getSharedInstance().getmCamera();
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		this.towerList = new LinkedList<Tower>();
		this.bulletList = new LinkedList<Bullet>();
		this.enemyList = new LinkedList<Enemy>();
		this.wreckageList = new LinkedList<Wreckage>();

		this.buildTowers();
		this.creatingWalls();

		this.setOnSceneTouchListener(this);

		text1 = new Text(20, 20, activity.getmFont(), "Score : ", activity.getVertexBufferObjectManager());
		text1.setScale(0.5f);
		attachChild(text1);

		// Enregistrement d'un update handler
		registerUpdateHandler(new GameLoopUpdateHandler());
		registerUpdateHandler(this.mPhysicsWorld);
	}

	private void buildTowers() {
		int towerSizeX = 10;
		int towerSizeY = 40;

		tower1 = new Tower(towerSizeX, towerSizeY, towerList);
		tower1.setPosition(20, (int) mCamera.getHeight() - tower1.getHeight() - 240);
		Bunker bunker1 = new Bunker(tower1);
		tower1.setBunker(bunker1);

		tower2 = new Tower(towerSizeX, towerSizeY, towerList);
		tower2.setPosition(20, (int) mCamera.getHeight() - tower2.getHeight() * 2);
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

	public void cleaner() {
		synchronized (this) {

			Iterator<Enemy> eIt = enemyList.iterator();
			while (eIt.hasNext()) {
				Enemy e = eIt.next();
				Iterator<Bullet> it = bulletList.iterator();
				while (it.hasNext()) {
					Bullet b = it.next();
					if (b.getSprite().getY() <= -b.getSprite().getHeight()
							|| b.getSprite().getX() <= -b.getSprite().getHeight()

							|| b.getSprite().getX() >= -b.getSprite().getHeight() + mCamera.getWidth()) {
						BulletPool.sharedBulletPool().recyclePoolItem(b);
						it.remove();
						continue;
					}

					if (b.getSprite().collidesWith(e.getSprite())) {
						if (e.gotHitnDestroyed(b.getAngle()) == 1) {
							if (!e.isPhysic()) {
								particleEmitterExplosion.createExplosion(e.getSprite().getX()
										+ e.getSprite().getWidth() / 2, e.getSprite().getY()
										+ e.getSprite().getHeight() / 2, e.getSprite().getParent(),
										BaseActivity.getSharedInstance(), 30, 3, 3, b.getSprite().getRotation());
								e.addPhysics();

							}
						} else if (e.gotHitnDestroyed(b.getAngle()) == 0) {
							soundExplosion.play();
							particleEmitterExplosion.createExplosion(e.getSprite().getX() + e.getSprite().getWidth()
									/ 2, e.getSprite().getY() + e.getSprite().getHeight() / 2, e.getSprite()
									.getParent(), BaseActivity.getSharedInstance(), 30, 3, 3, b.getSprite()
									.getRotation());

							EnemyPool.sharedEnemyPool().recyclePoolItem(e);
							eIt.remove();

						}
						soundImpact.play();
						particleEmitterExplosion.createBulletImpact(
								b.getSprite().getX() + b.getSprite().getWidth() / 2, b.getSprite().getY()
										+ b.getSprite().getHeight() / 2, b.getSprite().getParent(),
								BaseActivity.getSharedInstance(), Color.BLACK, 1, 2, 3, b.getSprite().getRotation());
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
			if (posX < tower.getBunker().getPosX() + tower.getBunker().getWidth() + marginSelection
					&& posX > tower.getBunker().getPosX() - marginSelection) {
				if (posY < tower.getBunker().getPosY() + tower.getBunker().getHeight() + marginSelection
						&& posY > tower.getBunker().getPosY() - marginSelection) {
					return towerList.indexOf(tower);
				}
			}
		}
		return -1;
	}

	private void creatingWalls() {
		final Rectangle ground = new Rectangle(0, activity.CAMERA_HEIGHT - 52, activity.CAMERA_WIDTH, 2, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		final Rectangle roof = new Rectangle(0, 0, activity.CAMERA_WIDTH, 2, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		final Rectangle left = new Rectangle(0, 0, 2, activity.CAMERA_HEIGHT, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		final Rectangle right = new Rectangle(activity.CAMERA_WIDTH - 2, 0, 2, activity.CAMERA_HEIGHT, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
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
		return text1;
	}

	public void setText1(Text text1) {
		this.text1 = text1;
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

}
