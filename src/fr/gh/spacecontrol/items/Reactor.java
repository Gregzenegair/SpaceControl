package fr.gh.spacecontrol.items;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

import fr.gh.spacecontrol.logic.RandomTool;
import fr.gh.spacecontrol.scenes.BaseActivity;
import fr.gh.spacecontrol.scenes.GameScene;

public class Reactor {

	private Sprite sprite;
	private Body body;
	private int hp;
	private int speed;
	private boolean physic;
	private boolean destroyed;
	private int finalPosX;
	private int finalPosY;
	private Camera mCamera;
	private MoveModifier moveModifier;
	private PhysicsConnector PhysicsConnector;
	private int reactorSide;
	private Enemy enemy;
	private int scoreValue;
	protected final int MAX_HEALTH = 3;
	protected final int PHYSIC_HEALTH = 1;

	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10, 0.02f, 0.02f);
	private static final Vector2 HIT_VECTOR_LEFT = new Vector2(1, 1);
	private static final Vector2 HIT_VECTOR_RIGHT = new Vector2(-1, 1);
	protected static final int REACTOR_RIGHT = 0;
	protected static final int REACTOR_LEFT = 1;

	public Reactor() {
		this.mCamera = BaseActivity.getSharedInstance().getmCamera();

		sprite = new Sprite(0, 0, BaseActivity.getSharedInstance().enemyReactorTexture, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
	}

	// method for initializing the Reactor object , used by the constructor and
	// the ReactorPool class
	public void init(Enemy enemy, int reactorSide) {

		this.enemy = enemy;
		this.scoreValue = 20;
		this.reactorSide = reactorSide;
		hp = MAX_HEALTH;
		destroyed = false;
		physic = false;
		speed = enemy.getCockpit().getSpeed();

		sprite.setRotation(0);
		sprite.setVisible(true);
		sprite.setPosition(enemy.getCockpit().getSprite().getX(), enemy.getCockpit().getSprite().getY());
		if(reactorSide == REACTOR_LEFT){
			sprite.setFlippedHorizontal(true);
		} else {
			sprite.setFlippedHorizontal(false);
		}

		this.finalPosX = enemy.getCockpit().getFinalPosX();
		this.finalPosY = enemy.getCockpit().getFinalPosY();
		
	}

	public void move() {

		if (!this.physic && !this.destroyed) {
			this.finalPosX = enemy.getCockpit().getFinalPosX();
			this.finalPosY = enemy.getCockpit().getFinalPosY();

			if (this.moveModifier != null)
				sprite.unregisterEntityModifier(this.moveModifier);

			switch (reactorSide) {
			case REACTOR_LEFT:
				sprite.registerEntityModifier(this.moveModifier = new MoveModifier(speed, enemy.getCockpit()
						.getSprite().getX()
						- sprite.getWidth(), this.finalPosX - sprite.getWidth(), enemy.getCockpit().getSprite().getY(),
						this.finalPosY));
				break;
			case REACTOR_RIGHT:
				sprite.registerEntityModifier(this.moveModifier = new MoveModifier(speed, enemy.getCockpit()
						.getSprite().getX()
						+ enemy.getCockpit().getSprite().getWidth(), this.finalPosX
						+ enemy.getCockpit().getSprite().getWidth(), enemy.getCockpit().getSprite().getY(),
						this.finalPosY));
				break;

			default:
				break;
			}
		}
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

	public int getReactorSide() {
		return reactorSide;
	}

	public void setReactorSide(int reactorSide) {
		this.reactorSide = reactorSide;
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
