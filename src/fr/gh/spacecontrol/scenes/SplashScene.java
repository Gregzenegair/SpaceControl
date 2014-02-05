package fr.gh.spacecontrol.scenes;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;

import fr.gh.spacecontrol.R;
import fr.gh.spacecontrol.items.SplashLogo;

public class SplashScene extends Scene {

	BaseActivity activity;

	public SplashScene() {
		setBackground(new Background(0.0f, 0.0f, 0.0f));
		activity = BaseActivity.getSharedInstance();
		activity.setCurrentScreen(1);

		Text titleG = new Text(0, 0, activity.getmFont(), activity.getString(R.string.title_1),
				activity.getVertexBufferObjectManager());

		titleG.setPosition((activity.getmCamera().getWidth() - titleG.getWidth()) / 2, (activity.getmCamera()
				.getHeight() - titleG.getHeight()) / 2);
		titleG.registerEntityModifier(new ScaleModifier(2.2f, 0.0f, 4.0f));
		titleG.setColor(1.0f, 1.0f, 1.0f);

		Text title = new Text(0, 0, activity.getmFont(), activity.getString(R.string.title_2),
				activity.getVertexBufferObjectManager());

		title.setPosition(activity.getmCamera().getWidth() + title.getWidth() / 1.5f,
				titleG.getX() + titleG.getHeight() * 3.5f + title.getHeight() / 2);

		attachChild(titleG);
		attachChild(title);
		
		SplashLogo logo = new SplashLogo();
		attachChild(logo.getSprite1());
		attachChild(logo.getSprite2());
		attachChild(logo.getSprite3());

		title.registerEntityModifier(new MoveXModifier(1, title.getX(), activity.getmCamera().getWidth() / 2));

		loadResources();

	}

	private void loadResources() {
		DelayModifier dMod = new DelayModifier(4) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				activity.setCurrentScene(new MainMenuScene());
			}
		};
		registerEntityModifier(dMod);
	}

}
