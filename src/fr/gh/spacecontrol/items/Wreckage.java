package fr.gh.spacecontrol.items;

import org.andengine.entity.primitive.Rectangle;

import fr.gh.spacecontrol.activities.BaseActivity;
import fr.gh.spacecontrol.logic.MathTool;

public class Wreckage {

	private Rectangle sprite;

	public Wreckage() {
		sprite = new Rectangle(0, 0, MathTool.randInt(1, 2), MathTool.randInt(3, 5), BaseActivity.getSharedInstance().getVertexBufferObjectManager());

		sprite.setColor(0.04f, 0.02f, 0.0f);
	}

	public Rectangle getSprite() {
		return sprite;
	}

	public void setSprite(Rectangle sprite) {
		this.sprite = sprite;
	}
	
}
