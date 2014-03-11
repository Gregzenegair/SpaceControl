package fr.gh.spacecontrol.logic;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.QuadraticBezierCurveMoveModifier;

import fr.gh.spacecontrol.activities.BaseActivity;
import fr.gh.spacecontrol.items.Cockpit;
import fr.gh.spacecontrol.items.Enemy;

public class Behavior {

	private Camera mCamera;
	private Enemy enemy;
	private int movementType;

	private int moveState = 0;
	private ArrayList<int[]> movePath;

	private boolean reversePath;

	static final public int STANDARD_MOVEMENT = 1;

	public Behavior(Enemy enemy, int movementType) {
		this.mCamera = BaseActivity.getSharedInstance().getmCamera();
		this.enemy = enemy;
		this.movementType = movementType;

		movePath = new ArrayList<int[]>();
		int[] coord01 = { 40, 50 };
		int[] coord02 = { (int) (mCamera.getWidth() / 3), 200 };
		int[] coord03 = { (int) (mCamera.getWidth() * 2 / 3), 50 };
		int[] coord04 = { (int) (mCamera.getWidth() - 40), 200 };

		movePath.add(coord01);
		movePath.add(coord02);
		movePath.add(coord03);
		movePath.add(coord04);

		setReversePath(false);
	}

	public boolean move(Cockpit cockpit) {
		int[] coords = movePath.get(getMoveState());
		boolean travelCompleteAndReloaded = false;

		if (!isReversePath()) {
			setMoveState(getMoveState() + 1);
		} else {
			setMoveState(getMoveState() - 1);
		}

		if (getMoveState() == 0 || getMoveState() == movePath.size() - 1) {
			setReversePath(!isReversePath());
			travelCompleteAndReloaded = true;
		}

		cockpit.setFinalPosX(coords[0]);
		cockpit.setFinalPosY(coords[1]);

		if (cockpit.getMoveModifier() != null)
			cockpit.getSprite().unregisterEntityModifier(cockpit.getMoveModifier());

		cockpit.getSprite().registerEntityModifier(new MoveModifier(cockpit.getSpeed(), cockpit.getSprite().getX(), cockpit.getFinalPosX(), cockpit.getSprite().getY(), cockpit.getFinalPosY()));
//		cockpit.getSprite().registerEntityModifier(
//				new QuadraticBezierCurveMoveModifier(1, cockpit.getSprite().getX(), cockpit.getSprite().getY(), cockpit.getSprite().getX(), cockpit.getSprite().getY() + 50, cockpit.getFinalPosX(),
//						cockpit.getFinalPosY()));
		return travelCompleteAndReloaded;
	}

	public int getMoveState() {
		return moveState;
	}

	public void setMoveState(int moveState) {
		this.moveState = moveState;
	}

	public boolean isReversePath() {
		return reversePath;
	}

	public void setReversePath(boolean reversePath) {
		this.reversePath = reversePath;
	}

	public int getMovementType() {
		return movementType;
	}

	public void setMovementType(int movementType) {
		this.movementType = movementType;
	}

	public ArrayList<int[]> getMovePath() {
		return movePath;
	}

	public void setMovePath(ArrayList<int[]> movePath) {
		this.movePath = movePath;
	}
}
