package fr.gh.spacecontrol.items;

import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;

import fr.gh.spacecontrol.activities.BaseActivity;
import fr.gh.spacecontrol.logic.MathTool;
import fr.gh.spacecontrol.pools.BulletPool;
import fr.gh.spacecontrol.scenes.GameScene;

public class Tower {
	private Sprite sprite;
	private int rotationCenterX;
	private int rotationCenterY;
	private float scaleYSaved;
	private float startingAngle;
	private float angle;
	private boolean facingLeft;
	private boolean active;
	private boolean destroyed;
	private float towerAxis;
	private Bunker bunker;
	private Shield shield;
	private Camera mCamera;
	private ScaleModifier scaleMod;
	private AlphaModifier alphaMod;

	public Tower(int width, int height, LinkedList<Tower> towerList) {
		sprite = new Sprite(0, 0, BaseActivity.getSharedInstance().towerTexture, BaseActivity.getSharedInstance().getVertexBufferObjectManager());

		this.scaleYSaved = sprite.getScaleY();
		this.mCamera = BaseActivity.getSharedInstance().getmCamera();
		this.facingLeft = false;
		if (towerList.size() >= 2) {
			this.facingLeft = true;
		}
		towerList.add(this);
		this.destroyed = false;

	}

	public void setPosition(int posX, int posY) {

		sprite.setPosition(posX, posY);
		sprite.setRotationCenter(sprite.getWidth() / 2, sprite.getHeight());
		this.rotationCenterX = (int) (sprite.getX() + sprite.getHeight());
		this.rotationCenterY = (int) (sprite.getY() + sprite.getWidth() / 2);

		sprite.setScale(1, 0);
		setActive(false);
	}

	public void rotateTower(float inTouch, float startingTouch) {
		if (this.facingLeft) {
			angle = startingAngle - (inTouch - startingTouch) / 2;
			if (angle < -90)
				angle = -90;
			if (angle > 0)
				angle = 0;
		} else {
			angle = startingAngle + (inTouch - startingTouch) / 2;
			if (angle < 0)
				angle = 0;
			if (angle > 90)
				angle = 90;
		}

		sprite.setRotation(angle);

	}

	public void shoot(int angle) {

		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();

		float randAngle = (float) (angle + MathTool.randInt(-3, 3));

		Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
		b.getSprite().setPosition(rotationCenterX - ((float) Math.cos(Math.toRadians(270 - randAngle)) * sprite.getHeight()) + sprite.getWidth() / 2 - sprite.getHeight() - 1,
				rotationCenterY + (float) Math.sin(Math.toRadians(270 - randAngle)) * sprite.getHeight() - sprite.getWidth() / 2 + sprite.getHeight() - 1);

		b.getSprite().setRotation(randAngle);
		MoveModifier movMod = new MoveModifier(1.5f, b.getSprite().getX(), rotationCenterX - ((float) Math.cos(Math.toRadians(270 - randAngle)) * 1000) + sprite.getWidth() / 2 - sprite.getHeight(), b
				.getSprite().getY(), rotationCenterY + (float) Math.sin(Math.toRadians(270 - randAngle)) * 1000 - sprite.getWidth() / 2 + sprite.getHeight());

		b.setAngle(angle);
		b.getSprite().setVisible(true);
		scene.attachChild(b.getSprite());
		scene.getBulletList().add(b);
		b.getSprite().registerEntityModifier(movMod);
		scene.setBulletCount(scene.getBulletCount() + 1);

		int soundRandom = MathTool.randInt(0, 3);
		if (soundRandom == 0 || soundRandom == 1 || soundRandom == 2)
			scene.getSoundTowerGun().play();
		else
			scene.getSoundTowerGunb().play();

	}

	public float getAngle() {
		return angle;
	}

	public void setStartingAngle(float startingAngle) {
		this.startingAngle = startingAngle;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		
		if (active && !this.active) {
			if (scaleMod != null || alphaMod != null) {
				sprite.unregisterEntityModifier(scaleMod);
				getShield().getSprite().unregisterEntityModifier(alphaMod);
			}
			scaleMod = new ScaleModifier(2, 1, 1, 0, 1);
			sprite.registerEntityModifier(scaleMod);

			alphaMod = new AlphaModifier(2, 1, 0);
			getShield().getSprite().registerEntityModifier(alphaMod);
			GameScene scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();
			scene.registerUpdateHandler(new TimerHandler(1.8f, new ITimerCallback() {
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					getShield().getSprite().setVisible(false);
				}
			}));
			

		} else if (!active && this.active) {
			getShield().getSprite().setVisible(true);
			scaleMod = new ScaleModifier(2, 1, 1, 1, 0);
			sprite.registerEntityModifier(scaleMod);

			alphaMod = new AlphaModifier(2, 0, 1);
			getShield().getSprite().registerEntityModifier(alphaMod);

		}

		this.active = active;
	}

	public boolean isFacingLeft() {
		return facingLeft;
	}

	public void setFacingLeft(boolean facingLeft) {
		this.facingLeft = facingLeft;
	}

	public float getTowerAxis() {
		return towerAxis;
	}

	public void setTowerAxis(float towerAxis) {
		this.towerAxis = towerAxis;
	}

	public Bunker getBunker() {
		return bunker;
	}

	public void setBunker(Bunker bunker) {
		this.bunker = bunker;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public int getRotationCenterX() {
		return rotationCenterX;
	}

	public void setRotationCenterX(int rotationCenterX) {
		this.rotationCenterX = rotationCenterX;
	}

	public int getRotationCenterY() {
		return rotationCenterY;
	}

	public void setRotationCenterY(int rotationCenterY) {
		this.rotationCenterY = rotationCenterY;
	}

	public Camera getmCamera() {
		return mCamera;
	}

	public void setmCamera(Camera mCamera) {
		this.mCamera = mCamera;
	}

	public float getStartingAngle() {
		return startingAngle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getScaleYSaved() {
		return scaleYSaved;
	}

	public Shield getShield() {
		return shield;
	}

	public void setShield(Shield shield) {
		this.shield = shield;
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}

}
