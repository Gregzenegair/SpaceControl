package fr.gh.spacecontrol.items;

import org.andengine.entity.sprite.AnimatedSprite;

import fr.gh.spacecontrol.activities.BaseActivity;

public class Flames {

	private AnimatedSprite aSprite;

	public Flames(float posX, float posY) {

		aSprite = new AnimatedSprite(posX, posY, BaseActivity.getSharedInstance().flamesRegion, BaseActivity.getSharedInstance().getVertexBufferObjectManager());

		aSprite.animate(100);

	}

	public AnimatedSprite getSprite() {
		return aSprite;
	}

	public void setSprite(AnimatedSprite aSprite) {
		this.aSprite = aSprite;
	}

}
