package fr.gh.spacecontrol;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;

public class MainMenuScene extends MenuScene implements
		IOnMenuItemClickListener {

	BaseActivity activity;
	final int MENU_START = 0;
	final int MENU_BACKTOGAME = 1;

	public MainMenuScene() {
		super(BaseActivity.getSharedInstance().getmCamera());
		activity = BaseActivity.getSharedInstance();
		activity.setCurrentScreen(2);

		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		IMenuItem startButton = new TextMenuItem(MENU_START, activity.getmFont(),
				activity.getString(R.string.start),
				activity.getVertexBufferObjectManager());
		startButton.setPosition(
				(mCamera.getWidth() / 2 - startButton.getWidth() / 2),
				mCamera.getHeight() / 2 - startButton.getHeight() / 2);

		IMenuItem backToGameButton = new TextMenuItem(MENU_BACKTOGAME,
				activity.getmFont(), activity.getString(R.string.backToGame),
				activity.getVertexBufferObjectManager());
		backToGameButton.setPosition(
				(mCamera.getWidth() / 2 - backToGameButton.getWidth() / 2),
				(mCamera.getHeight() / 2 - startButton.getHeight() / 2) + 40);

		addMenuItem(startButton);
		addMenuItem(backToGameButton);

		setOnMenuItemClickListener(this);
	}

	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1,
			float arg2, float arg3) {
		switch (arg1.getID()) {
		case MENU_START:
			activity.setCurrentScene(new GameScene());
			return true;
		case MENU_BACKTOGAME:
			if (activity.isGameStarted()) {
				activity.setCurrentScene(activity.getGameScene());
				activity.setCurrentScreen(activity.GAME_SCREEN);
				return true;
			}
			return false;
		default:
			break;
		}
		return false;
	}

}
