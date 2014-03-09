package fr.gh.spacecontrol.items;

import org.andengine.entity.primitive.Rectangle;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.SettingInjectorService;
import android.net.NetworkInfo.DetailedState;
import android.widget.SeekBar;

import fr.gh.spacecontrol.activities.BaseActivity;

public class Indicator {

	private BaseActivity activity;
	SharedPreferences settings;
	public Indicator instance;
	private Rectangle rectangleSprite;
	private Rectangle levelSprite;

	public Indicator() {
		activity = BaseActivity.getSharedInstance();
		activity.setCurrentScreen(activity.OPTION_SCREEN);
		settings = activity.getSettings();

		rectangleSprite = new Rectangle(0, 0, 8, 100, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		rectangleSprite.setAlpha(0.6f);
		levelSprite = new Rectangle(0, 0, 10, 10, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		levelSprite.setAlpha(0.8f);
		levelSprite.setColor(0.8f, 0.3f, 0.3f);
		this.rectangleSprite.setVisible(false);
		this.levelSprite.setVisible(false);
	}

	// implement option for left thumb and hiddden
	public void setStartingPos(float posX, float posY) {
		int leftHanded = 60;
		int rightHanded = -60;
		if (settings.getBoolean("rightHanded", true)) {
			this.rectangleSprite.setPosition(posX + rightHanded - rectangleSprite.getWidth() / 2, posY - rectangleSprite.getHeight() / 2);
			this.levelSprite.setPosition(posX + rightHanded - (levelSprite.getWidth() / 2), posY);
		} else {
			this.rectangleSprite.setPosition(posX + leftHanded - rectangleSprite.getWidth() / 2, posY - rectangleSprite.getHeight() / 2);
			this.levelSprite.setPosition(posX + leftHanded - (levelSprite.getWidth() / 2), posY);
		}
		if (settings.getBoolean("showIndicator", true)) {
			this.rectangleSprite.setVisible(true);
			this.levelSprite.setVisible(true);
		}
	}

	public void move(float towerAxe, boolean facingLeft) {
		float position = towerAxe;
		if (facingLeft)
			position = rectangleSprite.getY() - position;
		else
			position = rectangleSprite.getY() + position;
		this.levelSprite.setY(position);

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
