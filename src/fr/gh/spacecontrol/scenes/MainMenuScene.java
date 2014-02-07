package fr.gh.spacecontrol.scenes;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;

import fr.gh.spacecontrol.R;

public class MainMenuScene extends MenuScene implements IOnMenuItemClickListener {

	BaseActivity activity;
	final int MENU_START = 0;
	final int MENU_BACKTOGAME = 1;
	final int MENU_EXIT = 2;

	public MainMenuScene() {
		super(BaseActivity.getSharedInstance().getmCamera());
		activity = BaseActivity.getSharedInstance();
		activity.setCurrentScreen(2);

		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		IMenuItem startButton = new TextMenuItem(MENU_START, activity.getmFont(),
				activity.getString(R.string.startGame), activity.getVertexBufferObjectManager());
		startButton.setPosition((mCamera.getWidth() / 2 - startButton.getWidth() / 2), mCamera.getHeight() / 2
				- startButton.getHeight() / 2);
		startButton.setScale(0.5f);

		IMenuItem backToGameButton = new TextMenuItem(MENU_BACKTOGAME, activity.getmFont(),
				activity.getString(R.string.backToGame), activity.getVertexBufferObjectManager());
		backToGameButton.setPosition((mCamera.getWidth() / 2 - backToGameButton.getWidth() / 2),
				(mCamera.getHeight() / 2 - backToGameButton.getHeight() / 2) + (backToGameButton.getHeight() * 2));
		backToGameButton.setScale(0.5f);

		IMenuItem exitButton = new TextMenuItem(MENU_EXIT, activity.getmFont(), activity.getString(R.string.exitGame),
				activity.getVertexBufferObjectManager());
		exitButton.setPosition((mCamera.getWidth() / 2 - exitButton.getWidth() / 2),
				(mCamera.getHeight() / 2 - exitButton.getHeight() / 2) + (exitButton.getHeight() * 4));
		exitButton.setScale(0.5f);

		addMenuItem(startButton);
		if (activity.isGameStarted())
			addMenuItem(backToGameButton);
		addMenuItem(exitButton);

		setOnMenuItemClickListener(this);
		/*
		 * //++DEBUG DelayModifier dMod = new DelayModifier(2) {
		 * 
		 * @Override protected void onModifierFinished(IEntity pItem) {
		 * activity.setCurrentScene(new GameScene()); } };
		 * registerEntityModifier(dMod); //--DEBUG
		 */
	}

	public boolean onMenuItemClicked(MenuScene arg0, IMenuItem arg1, float arg2, float arg3) {
		switch (arg1.getID()) {
		case MENU_START:
			activity.setCurrentScene(new GameScene());
			return true;
		case MENU_BACKTOGAME:
			if (activity.isGameStarted()) {
				activity.setCurrentScene(activity.getGameScene());
				activity.setCurrentScreen(BaseActivity.GAME_SCREEN);
				return true;
			}
			return false;
		case MENU_EXIT:
			activity.finish();
			return true;
		default:
			break;
		}
		return false;
	}

}
