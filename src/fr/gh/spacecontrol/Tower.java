package fr.gh.spacecontrol;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;

public class Tower {
	public Rectangle sprite;
	public Rectangle spriteBase;
	private int posX;
	private int posY;
	private int rotationCenterX;
	private int rotationCenterY;
	private int width;
	private int height;
	private float startingAngle;
	private float angle;
	private Camera mCamera;

	public Tower(int width, int height) {
		sprite = new Rectangle(0, 0, width, height, BaseActivity
				.getSharedInstance().getVertexBufferObjectManager());
		spriteBase = new Rectangle(0, 0, width * 4, height - 8, BaseActivity
				.getSharedInstance().getVertexBufferObjectManager());
		this.width = width;
		this.height = height;

		this.mCamera = BaseActivity.getSharedInstance().mCamera;
	}

	public void setPosition(int posX, int posY) {
		sprite.setPosition(posX, posY);
		sprite.setRotationCenter(width / 2, height);
		spriteBase.setPosition(posX / 2, posY + height - 6);
		this.posX = posX;
		this.posY = posY;
		this.rotationCenterX = posX + height;
		this.rotationCenterY = posY + width / 2;
	}

	public void rotateTower(float inTouch, float startingTouch) {
		angle = startingAngle + (inTouch - startingTouch) / 2;
		if (angle < 0)
			angle = 0;
		if (angle > 90)
			angle = 90;

		sprite.setRotation(angle);
		// System.out.println(sprite.getRotationCenterY());

	}

	public void shoot() {

		GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;

		float randAngle = (float) (angle + Math.round(Math.random()) * 1 - Math
				.round(Math.random()) * 1);

		Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
		b.sprite.setPosition(
				rotationCenterX
						- ((float) Math.cos(Math.toRadians(270 - randAngle)) * height)
						+ width / 2 - height - 1, rotationCenterY
						+ (float) Math.sin(Math.toRadians(270 - randAngle))
						* height - width / 2 + height - 1);

		b.sprite.setRotation(randAngle);
		MoveModifier movMod = new MoveModifier(
				1.5f,
				b.sprite.getX(),
				rotationCenterX
						- ((float) Math.cos(Math.toRadians(270 - randAngle)) * 1000)
						+ width / 2 - height, b.sprite.getY(), rotationCenterY
						+ (float) Math.sin(Math.toRadians(270 - randAngle))
						* 1000 - width / 2 + height);
		b.sprite.setVisible(true);
		// b.sprite.detachSelf();
		scene.attachChild(b.sprite);
		scene.bulletList.add(b);
		b.sprite.registerEntityModifier(movMod);
		scene.bulletCount++;

	}

	public float getAngle() {
		return angle;
	}

	public void setStartingAngle(float startingAngle) {
		this.startingAngle = startingAngle;
	}

	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
