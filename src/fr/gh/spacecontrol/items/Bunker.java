package fr.gh.spacecontrol.items;

import org.andengine.entity.sprite.Sprite;

import fr.gh.spacecontrol.scenes.BaseActivity;

public class Bunker {

	private Sprite sprite;

	public Bunker(Tower tower) {
		this.sprite = new Sprite(0, 0, BaseActivity.getSharedInstance().getBunkerTexture(), BaseActivity
				.getSharedInstance().getVertexBufferObjectManager());

		this.sprite.setPosition(tower.getSprite().getX(), tower.getSprite().getY() + tower.getSprite().getHeight() - 10);

		if (tower.isFacingLeft()) {
			this.sprite.setPosition(tower.getSprite().getX(), tower.getSprite().getY() + tower.getSprite().getHeight()
					- 10);
		}

	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

}
