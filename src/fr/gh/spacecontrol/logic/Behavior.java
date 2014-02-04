package fr.gh.spacecontrol.logic;

import org.andengine.entity.modifier.MoveModifier;

import fr.gh.spacecontrol.items.Enemy;

public class Behavior {

	private boolean moving;
	private boolean shooting;
	private boolean aiming;
	private Enemy enemy;

	static final protected int STANDARD_MOVEMENT = 1;

	public Behavior(Enemy enemy) {
		this.enemy = enemy;
	}

	public void move(int movementType) {
		switch (movementType) {
		case STANDARD_MOVEMENT:

			
			break;

		default:
			break;
		}
		
	}
}
