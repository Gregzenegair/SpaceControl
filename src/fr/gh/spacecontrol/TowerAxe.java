package fr.gh.spacecontrol;

public class TowerAxe {

	private float startingY;

	TowerAxe(float x, float y) {
		startingY = y;
	}

	public float getStartingY() {
		return startingY;
	}

	public void setStartingY(float startingY) {
		this.startingY = startingY;
	}
}
