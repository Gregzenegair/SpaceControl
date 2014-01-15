package fr.gh.spacecontrol;

import org.andengine.entity.primitive.Rectangle;

public class Bunker {

	private Tower tower;
	private Rectangle sprite;
	private int posX;
	private int posY;
	private int width;
	private int height;

	public Bunker(Tower tower) {
		this.sprite = new Rectangle(0, 0, tower.getWidth() * 4, tower.getHeight() - 8, BaseActivity.getSharedInstance()
				.getVertexBufferObjectManager());
		this.sprite.setPosition(tower.getPosX() - tower.getWidth() / 2, tower.getPosY() + tower.getHeight() - 6);
		if (tower.isFacingLeft()) {
			this.sprite.setPosition(tower.getPosX() - tower.getWidth() * 2.5f, tower.getPosY() + tower.getHeight() - 6);
		}
		this.tower = tower;
	}

	public Tower getTower() {
		return tower;
	}

	public void setTower(Tower tower) {
		this.tower = tower;
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
