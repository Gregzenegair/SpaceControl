package fr.gh.spacecontrol;

import org.andengine.entity.primitive.Rectangle;

public class Wreckage {

	private Rectangle sprite;

	public Wreckage() {
		sprite = new Rectangle(0, 0, RandomTool.randInt(1, 2), RandomTool.randInt(3, 5), BaseActivity.getSharedInstance().getVertexBufferObjectManager());

		sprite.setColor(0.04f, 0.02f, 0.0f);
	}

	public Rectangle getSprite() {
		return sprite;
	}

	public void setSprite(Rectangle sprite) {
		this.sprite = sprite;
	}
	
}
