package fr.gh.spacecontrol;

public class TowerAxis {

	private float startingY;

	TowerAxis(float x, float y) {
		startingY = y;
	}

	public float getStartingY() {
		return startingY;
	}

	public void setStartingY(float startingY) {
		this.startingY = startingY;
	}
}
