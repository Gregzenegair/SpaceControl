package fr.gh.spacecontrol.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;

import android.content.SharedPreferences;
import fr.gh.spacecontrol.R;
import fr.gh.spacecontrol.activities.BaseActivity;

public class OptionsScene extends MenuScene implements IOnMenuItemClickListener {

	BaseActivity activity;
	SharedPreferences settings;
	final int RIGHT_HANDED = 0;
	final int SHOW_INDICATOR = 1;
	final int QUIT_OPTIONS = 2;

	IMenuItem rightHandedButton;
	IMenuItem showIndicatorButton;
	boolean rightHanded;
	boolean showIndicator;

	public OptionsScene() {
		super(BaseActivity.getSharedInstance().getmCamera());
		activity = BaseActivity.getSharedInstance();
		activity.setCurrentScreen(activity.OPTION_SCREEN);
		settings = activity.getSettings();

		setBackground(new Background(0.29804f, 0.274f, 0.28784f));

		// -- rightHanded Button
		rightHandedButton = new TextMenuItem(RIGHT_HANDED, activity.getmFont(), activity.getString(R.string.rightHanded), activity.getVertexBufferObjectManager());
		rightHandedButton.setPosition((mCamera.getWidth() / 2 - rightHandedButton.getWidth() / 2), mCamera.getHeight() / 2 - rightHandedButton.getHeight() * 2);
		rightHandedButton.setScale(0.5f);

		if (settings.getBoolean("rightHanded", true)) {
			rightHandedButton.setColor(0, 1, 0.2f);
		} else {
			rightHandedButton.setColor(1, 0, 0.2f);
		}

		// -- showIndicator Button
		showIndicatorButton = new TextMenuItem(SHOW_INDICATOR, activity.getmFont(), activity.getString(R.string.showIndicator), activity.getVertexBufferObjectManager());
		showIndicatorButton.setPosition((mCamera.getWidth() / 2 - showIndicatorButton.getWidth() / 2), mCamera.getHeight() / 2 - rightHandedButton.getHeight());
		showIndicatorButton.setScale(0.5f);

		if (settings.getBoolean("showIndicator", true)) {
			showIndicatorButton.setColor(0, 1, 0.2f);
		} else {
			showIndicatorButton.setColor(1, 0, 0.2f);
		}

		// -- Exit options Button
		IMenuItem exitButton = new TextMenuItem(QUIT_OPTIONS, activity.getmFont(), activity.getString(R.string.quitOptions), activity.getVertexBufferObjectManager());
		exitButton.setPosition((mCamera.getWidth() / 2 - exitButton.getWidth() / 2), mCamera.getHeight() / 2 + rightHandedButton.getHeight());
		exitButton.setScale(0.5f);

		addMenuItem(rightHandedButton);
		addMenuItem(showIndicatorButton);
		addMenuItem(exitButton);

		setOnMenuItemClickListener(this);

	}

	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1, float arg2, float arg3) {
		switch (arg1.getID()) {
		case RIGHT_HANDED:
			activity.getSettings().edit().putBoolean("rightHanded", !settings.getBoolean("rightHanded", true)).apply();
			if (settings.getBoolean("rightHanded", true)) {
				rightHandedButton.setColor(0, 1, 0.2f);
			} else {
				rightHandedButton.setColor(1, 0, 0.2f);
			}
			return true;
		case SHOW_INDICATOR:
			settings.edit().putBoolean("showIndicator", !settings.getBoolean("showIndicator", true)).apply();
			if (settings.getBoolean("showIndicator", true)) {
				showIndicatorButton.setColor(0, 1, 0.2f);
			} else {
				showIndicatorButton.setColor(1, 0, 0.2f);
			}
			return true;
		case QUIT_OPTIONS:
			activity.setCurrentScene(new MainMenuScene());
			return true;
		default:
			break;
		}
		return false;
	}

}
