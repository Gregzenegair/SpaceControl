package fr.gh.spacecontrol.items;

import org.andengine.entity.primitive.Rectangle;

import fr.gh.spacecontrol.activities.BaseActivity;

public class Indicator {

	public static Indicator instance;
	private Rectangle rectangleSprite;
	private Rectangle levelSprite;
	private boolean leftHanded;

	public static Indicator getIndicator() {
		if (instance == null)
			instance = new Indicator();
		return instance;
	}

	private Indicator() {
		rectangleSprite = new Rectangle(0, 0, 8, 60, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		rectangleSprite.setAlpha(0.6f);
		rectangleSprite = new Rectangle(0, 0, 8, 60, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		rectangleSprite.setAlpha(0.6f);
		levelSprite = new Rectangle(0, 0, 10, 10, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		levelSprite.setAlpha(0.8f);
		levelSprite.setColor(0.8f, 0.3f, 0.3f);
		this.rectangleSprite.setVisible(false);
		this.levelSprite.setVisible(false);
	}

	public void setStartingPos(float posX, float posY) {
		this.rectangleSprite.setPosition(posX - rectangleSprite.getWidth() / 2, posY - rectangleSprite.getHeight() / 2);
		this.levelSprite.setPosition(posX - (levelSprite.getWidth() / 2), posY);

		this.rectangleSprite.setVisible(true);
		this.levelSprite.setVisible(true);
	}

	public void move(float inTouch) {
		this.levelSprite.setY(inTouch);

	}

	public void hide() {
		this.rectangleSprite.setVisible(false);
		this.levelSprite.setVisible(false);

	}

	public Rectangle getRectangleSprite() {
		return rectangleSprite;
	}

	public void setRectangleSprite(Rectangle rectangleSprite) {
		this.rectangleSprite = rectangleSprite;
	}

	public Rectangle getLevelSprite() {
		return levelSprite;
	}

	public void setLevelSprite(Rectangle levelSprite) {
		this.levelSprite = levelSprite;
	}

}
