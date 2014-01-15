package fr.gh.spacecontrol;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;

public class Enemy {
	public Rectangle sprite;
	public int hp;
	protected final int MAX_HEALTH = 50;

	private Camera mCamera;
	public int finalPosX;
	public int finalPosY;
	private MoveModifier moveModifier;

	public Enemy() {
		this.mCamera = BaseActivity.getSharedInstance().mCamera;
		sprite = new Rectangle(0, 0, 30, 30, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		sprite.setColor(0.06f, 0.004f, 0.004f);
		init();
	}

	// method for initializing the Enemy object , used by the constructor and
	// the EnemyPool class
	public void init() {
		hp = MAX_HEALTH;
		sprite.setPosition((RandomTool.randInt(100, (int) mCamera.getWidth() - 100)), RandomTool.randInt(-300, 0));

		this.finalPosX = RandomTool.randInt(100, (int) mCamera.getWidth() - 100);
		this.finalPosY = RandomTool.randInt(0, 100);

		sprite.registerEntityModifier(this.moveModifier = new MoveModifier(10, sprite.getX(), this.finalPosX, sprite
				.getY(), this.finalPosY));
	}

	public void move() {
		if ((int) sprite.getX() == this.finalPosX) {

			int speed = RandomTool.randInt(2, 4);
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
		this.finalPosY = (int) mCamera.getHeight()/2;

		if (this.moveModifier != null)
			sprite.unregisterEntityModifier(this.moveModifier);
		sprite.registerEntityModifier(this.moveModifier = new MoveModifier(1, sprite.getX(), this.finalPosX, sprite
				.getY(), this.finalPosY));
	}

	public void clean() {
		sprite.clearEntityModifiers();
		sprite.clearUpdateHandlers();
	}

	// method for applying hit and checking if enemy died or not
	// returns false if enemy died
	public boolean gotHitnDestroyed() {
		synchronized (this) {
			hp--;
			if (hp <= 0)
				return false;
			else
				return true;
		}
	}
}
