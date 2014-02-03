package fr.gh.spacecontrol;

import org.andengine.entity.sprite.Sprite;

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
