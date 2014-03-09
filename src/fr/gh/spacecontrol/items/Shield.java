package fr.gh.spacecontrol.items;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;

import fr.gh.spacecontrol.activities.BaseActivity;
import fr.gh.spacecontrol.logic.MathTool;

public class Shield {

	private AnimatedSprite aSprite;

	public Shield(Tower tower) {

		aSprite = new AnimatedSprite(0, 0, BaseActivity.getSharedInstance().shieldRegion, BaseActivity.getSharedInstance().getVertexBufferObjectManager());

		aSprite.animate(100);

		this.aSprite.setPosition(tower.getSprite().getX(), tower.getSprite().getY() + tower.getSprite().getHeight() - 10);

		if (tower.isFacingLeft()) {
			this.aSprite.setPosition(tower.getSprite().getX(), tower.getSprite().getY() + tower.getSprite().getHeight() - 10);
		}

		aSprite.setCurrentTileIndex(MathTool.randInt(0, 8));
	}

	public AnimatedSprite getSprite() {
		return aSprite;
	}

	public void setSprite(AnimatedSprite aSprite) {
		this.aSprite = aSprite;
	}

}
