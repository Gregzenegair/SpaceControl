package fr.gh.spacecontrol;

import java.io.IOException;
import java.util.LinkedList;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;

public class Tower {
	public Rectangle sprite;
	private int posX;
	private int posY;
	private int rotationCenterX;
	private int rotationCenterY;
	private int width;
	private int height;
	private float startingAngle;
	private float angle;
	private boolean facingLeft;
	private boolean active;
	private float towerAxis;
	private Camera mCamera;

	public Tower(int width, int height, LinkedList<Tower> towerList) {

		sprite = new Rectangle(0, 0, width, height, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		this.width = width;
		this.height = height;
		this.mCamera = BaseActivity.getSharedInstance().mCamera;
		this.facingLeft = false;
		if (towerList.size() >= 2) {
			this.facingLeft = true;
		}
		towerList.add(this);
	}

	public void setPosition(int posX, int posY) {

		sprite.setPosition(posX, posY);
		sprite.setRotationCenter(width / 2, height);
		this.posX = posX;
		this.posY = posY;
		this.rotationCenterX = posX + height;
		this.rotationCenterY = posY + width / 2;

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
		// System.out.println(sprite.getRotationCenterY());

	}

	public void shoot() {

		GameScene scene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;

		float randAngle = (float) (angle + RandomTool.randInt(-3, 3));

		Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
		b.sprite.setPosition(rotationCenterX - ((float) Math.cos(Math.toRadians(270 - randAngle)) * height) + width / 2
				- height - 1, rotationCenterY + (float) Math.sin(Math.toRadians(270 - randAngle)) * height - width / 2
				+ height - 1);

		b.sprite.setRotation(randAngle);
		MoveModifier movMod = new MoveModifier(1.5f, b.sprite.getX(), rotationCenterX
				- ((float) Math.cos(Math.toRadians(270 - randAngle)) * 1000) + width / 2 - height, b.sprite.getY(),
				rotationCenterY + (float) Math.sin(Math.toRadians(270 - randAngle)) * 1000 - width / 2 + height);
		
		b.sprite.setVisible(true);
		scene.attachChild(b.sprite);
		scene.bulletList.add(b);
		b.sprite.registerEntityModifier(movMod);
		scene.bulletCount++;

		int soundRandom = RandomTool.randInt(0, 3);
		if (soundRandom == 0 || soundRandom == 1 || soundRandom == 2)
			scene.soundTowerGun.play();
		else
			scene.soundTowerGunb.play();
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
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

}
