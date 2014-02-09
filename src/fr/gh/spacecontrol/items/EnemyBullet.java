package fr.gh.spacecontrol.items;

import org.andengine.entity.primitive.Rectangle;

import fr.gh.spacecontrol.scenes.BaseActivity;

public class EnemyBullet {
    private Rectangle sprite;
    private int angle;
    
    public EnemyBullet() {
        sprite = new Rectangle(0, 0, 6, 6, BaseActivity.getSharedInstance()
            .getVertexBufferObjectManager());
 
        sprite.setColor(0.01904f, 0.00113f, 0.00086f);
    }

	public Rectangle getSprite() {
		return sprite;
	}

	public void setSprite(Rectangle sprite) {
		this.sprite = sprite;
	}

	public int getAngle() {
		return angle;
	}

	public void setAngle(int angle) {
		this.angle = angle;
	}
    
}