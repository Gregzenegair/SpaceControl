package fr.gh.spacecontrol.entities;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import fr.gh.spacecontrol.logic.RandomTool;
import fr.gh.spacecontrol.scenes.BaseActivity;
import fr.gh.spacecontrol.scenes.GameScene;

public class Cockpit {

	private Rectangle sprite;
	private Body body;
	private int hp;
	private int speed = 2;
	private boolean physic;
	private boolean destroyed;
	private int finalPosX;
	private int finalPosY;
	private Camera mCamera;
	private MoveModifier moveModifier;
	private PhysicsConnector PhysicsConnector;
	private Enemy enemy;

	private int scoreValue;
	protected final int MAX_HEALTH = 10;
	protected final int PHYSIC_HEALTH = 2;

	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10, 0.02f, 0.02f);
	private static final Vector2 HIT_VECTOR_L = new Vector2(1, 1);
	private static final Vector2 HIT_VECTOR_R = new Vector2(-1, 1);

	public Cockpit() {
		this.mCamera = BaseActivity.getSharedInstance().getmCamera();
		sprite = new Rectangle(0, 0, 30, 30, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		sprite.setColor(0.06f, 0.004f, 0.004f);
	}

	// method for initializing the Enemy object , used by the constructor and
	// the EnemyPool class
	public void init(Enemy enemy) {

		this.enemy = enemy;
		this.scoreValue = 120;
		hp = MAX_HEALTH;
		destroyed = false;
		physic = false;

		sprite.setRotation(0);
		sprite.setVisible(true);
		sprite.setPosition((RandomTool.randInt(100, (int) mCamera.getWidth() - 100)), RandomTool.randInt(-300, 0));

		this.finalPosX = RandomTool.randInt(100, (int) mCamera.getWidth() - 100);
		this.finalPosY = RandomTool.randInt(0, 100);

		if (this.moveModifier != null)
			sprite.unregisterEntityModifier(this.moveModifier);
		sprite.registerEntityModifier(this.moveModifier = new MoveModifier(speed, sprite.getX(), this.finalPosX, sprite
				.getY(), this.finalPosY));

	}

	public void move() {

		if ((int) sprite.getX() == this.finalPosX && !this.isPhysic() && !this.isDestroyed()) {
			this.finalPosX = RandomTool.randInt(50, (int) mCamera.getWidth() - 50);
			this.finalPosY = RandomTool.randInt(0, 500);

			if (this.moveModifier != null)
				sprite.unregisterEntityModifier(this.moveModifier);
			sprite.registerEntityModifier(this.moveModifier = new MoveModifier(speed, sprite.getX(), this.finalPosX,
					sprite.getY(), this.finalPosY));
		}
	}

	public void moveCenter() {
		this.finalPosX = (int) mCamera.getWidth() / 2 - 15;
		this.finalPosY = (int) mCamera.getHeight() / 2;

		if (this.moveModifier != null)
			sprite.unregisterEntityModifier(this.moveModifier);
		sprite.registerEntityModifier(this.moveModifier = new MoveModifier(1, sprite.getX(), this.finalPosX, sprite
				.getY(), this.finalPosY));

	}

	public int gotHitnDestroyed(int angle) {
		synchronized (this) {
			hp--;
			if (hp <= 0) {
				return 0;
			} else if (hp <= PHYSIC_HEALTH) {
				if (this.physic) {
					if (angle >= 0) {
						body.setLinearVelocity(HIT_VECTOR_L);
						body.setAngularVelocity(-0.3f);
					} else {
						body.setLinearVelocity(HIT_VECTOR_R);
						body.setAngularVelocity(0.3f);
					}
				}
				return 1;
			} else {
				return 2;
			}
		}
	}

	public void addPhysics() {
		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getmCurrentScene();

		this.sprite.unregisterEntityModifier(this.moveModifier);

		this.body = PhysicsFactory.createBoxBody(scene.mPhysicsWorld, this.sprite, BodyType.DynamicBody, FIXTURE_DEF);

		this.PhysicsConnector = new PhysicsConnector(this.sprite, body, true, true);
		scene.mPhysicsWorld.registerPhysicsConnector(PhysicsConnector);
		this.physic = true;
	}

	public void remove() {
		if (this.isPhysic()) {
			GameScene scene = (GameScene) BaseActivity.getSharedInstance().getmCurrentScene();

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

	public Rectangle getSprite() {
		return sprite;
	}

	public void setSprite(Rectangle sprite) {
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

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public Enemy getEnemy() {
		return enemy;
	}

	public void setEnemy(Enemy enemy) {
		this.enemy = enemy;
	}

}
