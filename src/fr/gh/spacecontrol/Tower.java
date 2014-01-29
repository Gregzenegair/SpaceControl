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
	private Rectangle sprite;
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
	private Bunker bunker;
	private Camera mCamera;

	public Tower(int width, int height, LinkedList<Tower> towerList) {

		sprite = new Rectangle(0, 0, width, height, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		this.width = width;
		this.height = height;
		this.mCamera = BaseActivity.getSharedInstance().getmCamera();
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

	public void shoot(int angle) {

		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getmCurrentScene();

		float randAngle = (float) (angle + RandomTool.randInt(-3, 3));

		Bullet b = BulletPool.sharedBulletPool().obtainPoolItem();
		b.getSprite()
				.setPosition(
						rotationCenterX - ((float) Math.cos(Math.toRadians(270 - randAngle)) * height) + width / 2
								- height - 1,
						rotationCenterY + (float) Math.sin(Math.toRadians(270 - randAngle)) * height - width / 2
								+ height - 1);

		b.getSprite().setRotation(randAngle);
		MoveModifier movMod = new MoveModifier(1.5f, b.getSprite().getX(), rotationCenterX
				- ((float) Math.cos(Math.toRadians(270 - randAngle)) * 1000) + width / 2 - height,
				b.getSprite().getY(), rotationCenterY + (float) Math.sin(Math.toRadians(270 - randAngle)) * 1000
						- width / 2 + height);

		b.setAngle(angle);
		b.getSprite().setVisible(true);
		scene.attachChild(b.getSprite());
		scene.getBulletList().add(b);
		b.getSprite().registerEntityModifier(movMod);
		scene.setBulletCount(scene.getBulletCount() + 1);

		int soundRandom = RandomTool.randInt(0, 3);
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

	public Bunker getBunker() {
		return bunker;
	}

	public void setBunker(Bunker bunker) {
		this.bunker = bunker;
	}

	public Rectangle getSprite() {
		return sprite;
	}

	public void setSprite(Rectangle sprite) {
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

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

}
