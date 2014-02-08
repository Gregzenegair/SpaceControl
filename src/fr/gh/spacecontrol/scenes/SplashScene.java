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
		
		SplashLogo logo = new SplashLogo(this);
		attachChild(logo.getSprite1());
		attachChild(logo.getSprite2());
		attachChild(logo.getSprite3());
		attachChild(logo.getTitleG());
		attachChild(logo.getTitle());

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
