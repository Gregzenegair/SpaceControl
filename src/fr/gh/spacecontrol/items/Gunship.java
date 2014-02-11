package fr.gh.spacecontrol.items;

import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import android.transition.Scene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import fr.gh.spacecontrol.logic.RandomTool;
import fr.gh.spacecontrol.pools.EnemyBulletPool;
import fr.gh.spacecontrol.scenes.BaseActivity;
import fr.gh.spacecontrol.scenes.GameScene;

public class Gunship {

	private Camera mCamera;

	private Sprite sprite;
	private Enemy enemy;
	private Body body;
	private MoveModifier moveModifier;
	private PhysicsConnector PhysicsConnector;

	private int hp;
	private int speed;
	private int scoreValue;

	private boolean physic;
	private boolean destroyed;

	private int finalPosX;
	private int finalPosY;
	private int shootingPositionX;
	private int shootingPositionY;

	protected final int MAX_HEALTH = 3;
	protected final int PHYSIC_HEALTH = 1;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10, 0.02f, 0.02f);
	private static final Vector2 HIT_VECTOR_LEFT = new Vector2(1, 1);
	private static final Vector2 HIT_VECTOR_RIGHT = new Vector2(-1, 1);
	protected static final int REACTOR_RIGHT = 0;
	protected static final int REACTOR_LEFT = 1;

	public Gunship() {
		this.mCamera = BaseActivity.getSharedInstance().getmCamera();

		sprite = new Sprite(0, 0, BaseActivity.getSharedInstance().enemyGunshipTexture, BaseActivity
				.getSharedInstance().getVertexBufferObjectManager());
	}

	// method for initializing the Gunship object , used by the constructor and
	// the GunshipPool class
	public void init(Enemy enemy) {

		this.enemy = enemy;
		this.scoreValue = 30;
		hp = MAX_HEALTH;
		destroyed = false;
		physic = false;
		speed = enemy.getCockpit().getSpeed();

		sprite.setRotation(180);
		sprite.setScale(0.4f);
		sprite.setFlippedVertical(true);
		sprite.setVisible(true);
		sprite.setPosition(enemy.getCockpit().getSprite().getX(), enemy.getCockpit().getSprite().getY());

		this.finalPosX = enemy.getCockpit().getFinalPosX();
		this.finalPosY = enemy.getCockpit().getFinalPosY();
	}

	public void move() {

		if (!this.physic && !this.destroyed) {
			this.finalPosX = enemy.getCockpit().getFinalPosX();
			this.finalPosY = enemy.getCockpit().getFinalPosY();

			if (this.moveModifier != null)
				sprite.unregisterEntityModifier(this.moveModifier);

			sprite.registerEntityModifier(this.moveModifier = new MoveModifier(speed, enemy.getCockpit().getSprite()
					.getX()
					+ enemy.getCockpit().getSprite().getWidth() / 2 - sprite.getWidth() / 2, this.finalPosX
					+ enemy.getCockpit().getSprite().getWidth() / 2 - sprite.getWidth() / 2, enemy.getCockpit()
					.getSprite().getY()
					+ enemy.getCockpit().getSprite().getHeight() - 10, this.finalPosY
					+ enemy.getCockpit().getSprite().getHeight() - 10));

			this.shootingPositionX = (int) (this.finalPosX + enemy.getCockpit().getSprite().getWidth() / 2 - sprite
					.getWidth() / 2);
			this.shootingPositionY = (int) (this.finalPosY + enemy.getCockpit().getSprite().getHeight());

		}
	}

	public void rotate(float rotation) {
		if (!this.physic && !this.destroyed) {
			this.finalPosX = enemy.getCockpit().getFinalPosX();
			this.finalPosY = enemy.getCockpit().getFinalPosY();

			sprite.registerEntityModifier(new RotationModifier(0.5f, sprite.getRotation(), rotation));

		}
	}

	// angle 0 to up
	public float aim(int tower) {
		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();
		LinkedList<Tower> towerList = scene.getTowerList();
		Tower target = towerList.get(tower);

		float rotation = 0;
		float opposed = (target.getSprite().getX() + target.getSprite().getWidth() / 2 - this.sprite.getX());
		float adjacent = (target.getSprite().getY() + target.getSprite().getHeight() - this.sprite.getY());
		// Here adjust is required !!!!! angles and abs

		rotation = (float) (180 - Math.toDegrees(Math.atan(opposed / adjacent)));

		rotate(rotation);
		return rotation;
	}

	public void shoot(int angle) {

		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();

		EnemyBullet eB = EnemyBulletPool.sharedBulletPool().obtainPoolItem();
		eB.getSprite().setPosition(shootingPositionX, shootingPositionY);

		eB.getSprite().setRotation(angle);
		MoveModifier movMod = new MoveModifier(7, eB.getSprite().getX(), shootingPositionX
				- ((float) Math.cos(Math.toRadians(270 - angle)) * 1000) + sprite.getWidth() / 2 - sprite.getHeight(),
				eB.getSprite().getY(), shootingPositionY + (float) Math.sin(Math.toRadians(270 - angle)) * 1000
						- sprite.getWidth() / 2 + sprite.getHeight());

		eB.setAngle(angle);
		eB.getSprite().setVisible(true);
		scene.attachChild(eB.getSprite());
		scene.getEnemyBulletList().add(eB);
		eB.getSprite().registerEntityModifier(movMod);
		scene.setBulletCount(scene.getBulletCount() + 1);

		int soundRandom = RandomTool.randInt(0, 3);
		if (soundRandom == 0 || soundRandom == 1 || soundRandom == 2)
			scene.getSoundTowerGun().play();
		else
			scene.getSoundTowerGunb().play();
	}

	public int gotHitnDestroyed(int angle) {
		synchronized (this) {
			hp--;
			if (hp <= 0) {
				return 0;
			} else if (hp <= PHYSIC_HEALTH) {
				if (this.physic) {
					if (angle >= 0) {
						body.setLinearVelocity(HIT_VECTOR_LEFT);
						body.setAngularVelocity(1f);
					} else {
						body.setLinearVelocity(HIT_VECTOR_RIGHT);
						body.setAngularVelocity(-1f);
					}
				}
				return 1;
			} else {
				return 2;
			}
		}
	}

	public void addPhysics() {
		if (!this.destroyed && !this.physic) {
			GameScene scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();

			this.sprite.unregisterEntityModifier(this.moveModifier);

			this.body = PhysicsFactory.createBoxBody(scene.mPhysicsWorld, this.sprite, BodyType.DynamicBody,
					FIXTURE_DEF);

			this.PhysicsConnector = new PhysicsConnector(this.sprite, body, true, true);
			scene.mPhysicsWorld.registerPhysicsConnector(PhysicsConnector);
			this.physic = true;

			int random = RandomTool.randInt(-5, 5);
			body.setAngularVelocity(random);
			random = RandomTool.randInt(0, 4);
			if (random != 0) {
				final WeldJointDef joint = new WeldJointDef();
				joint.initialize(enemy.getCockpit().getBody(), this.body, enemy.getCockpit().getBody().getWorldCenter());

				scene.mPhysicsWorld.createJoint(joint);
			}
		}

	}

	public void remove() {
		if (this.isPhysic()) {
			GameScene scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();

			scene.mPhysicsWorld.destroyBody(this.getBody());
			scene.mPhysicsWorld.unregisterPhysicsConnector(this.PhysicsConnector);
		}

		sprite.clearEntityModifiers();
		sprite.clearUpdateHandlers();
		sprite.setVisible(false);
		sprite.detachSelf();
		this.physic = false;
		this.destroyed = true;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public boolean isPhysic() {
		return physic;
	}

	public void setPhysic(boolean physic) {
		this.physic = physic;
	}

	public int getFinalPosX() {
		return finalPosX;
	}

	public void setFinalPosX(int finalPosX) {
		this.finalPosX = finalPosX;
	}

	public int getFinalPosY() {
		return finalPosY;
	}

	public void setFinalPosY(int finalPosY) {
		this.finalPosY = finalPosY;
	}

	public Camera getmCamera() {
		return mCamera;
	}

	public void setmCamera(Camera mCamera) {
		this.mCamera = mCamera;
	}

	public MoveModifier getMoveModifier() {
		return moveModifier;
	}

	public void setMoveModifier(MoveModifier moveModifier) {
		this.moveModifier = moveModifier;
	}

	public int getMAX_HEALTH() {
		return MAX_HEALTH;
	}

	public static FixtureDef getFixtureDef() {
		return FIXTURE_DEF;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

	public int getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(int scoreValue) {
		this.scoreValue = scoreValue;
	}

	public PhysicsConnector getPhysicsConnector() {
		return PhysicsConnector;
	}

	public void setPhysicsConnector(PhysicsConnector physicsConnector) {
		PhysicsConnector = physicsConnector;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

	public Enemy getEnemy() {
		return enemy;
	}

}
