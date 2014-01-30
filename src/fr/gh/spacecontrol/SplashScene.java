package fr.gh.spacecontrol;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;

public class SplashScene extends Scene {

	BaseActivity activity;

	public SplashScene() {
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		activity = BaseActivity.getSharedInstance();
		activity.setCurrentScreen(1);
		Text title1 = new Text(0, 0, activity.getmFont(),
				activity.getString(R.string.title_1),
				activity.getVertexBufferObjectManager());
		Text title2 = new Text(0, 0, activity.getmFont(),
				activity.getString(R.string.title_2),
				activity.getVertexBufferObjectManager());

		title1.setPosition(-title1.getWidth(), activity.getmCamera().getHeight() / 2);
		title2.setPosition(activity.getmCamera().getWidth(),
				activity.getmCamera().getHeight() / 2);

		attachChild(title1);
		attachChild(title2);

		title1.registerEntityModifier(new MoveXModifier(1, title1.getX(),
				activity.getmCamera().getWidth() / 2 - title1.getWidth()));
		title2.registerEntityModifier(new MoveXModifier(1, title2.getX(),
				activity.getmCamera().getWidth() / 2));

		loadResources();

	}

	private void loadResources() {
		DelayModifier dMod = new DelayModifier(2) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				activity.setCurrentScene(new MainMenuScene());
			}
		};
		registerEntityModifier(dMod);
	}
	
}
