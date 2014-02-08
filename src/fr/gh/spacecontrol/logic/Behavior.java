package fr.gh.spacecontrol.logic;

import fr.gh.spacecontrol.items.Enemy;

public class Behavior {

	private Enemy enemy;

	static final protected int STANDARD_MOVEMENT = 1;

	public Behavior(Enemy enemy) {
		this.enemy = enemy;
	}

	public void move(int movementType) {
		switch (movementType) {
		case STANDARD_MOVEMENT:
			enemy.move();

			break;

		default:
			break;
		}

	}
}
