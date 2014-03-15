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

	private boolean reloaded;
	private int moveState;
	private ArrayList<int[]> movePath;

	static final public int STANDARD_MOVEMENT = 1;

	public Behavior(Enemy enemy, int movementType) {
		this.mCamera = BaseActivity.getSharedInstance().getmCamera();
		this.enemy = enemy;
		this.movementType = movementType;

		movePath = new ArrayList<int[]>();
		int[] coord01 = { 40, 50, 0 };
		int[] coord02 = { (int) (mCamera.getWidth() / 3), 200, 0 };
		int[] coord03 = { (int) (mCamera.getWidth() * 2 / 3), 50, 1 };
		int[] coord04 = { (int) (mCamera.getWidth() - 40), 200, 1 };
		int[] coord05 = { (int) (mCamera.getWidth() + 80), 50, 1 };
		int[] coord06 = { (int) (mCamera.getWidth() - 40), 200, 1 };
		int[] coord07 = { (int) (mCamera.getWidth() * 2 / 3), 50, 1 };
		int[] coord08 = { (int) (mCamera.getWidth() / 3), 200, 1 };
		int[] coord09 = { 40, 50, 0 };
		int[] coord10 = { -80, 200, 0 };

		movePath.add(coord01);
		movePath.add(coord02);
		movePath.add(coord03);
		movePath.add(coord04);
		movePath.add(coord05);
		movePath.add(coord06);
		movePath.add(coord07);
		movePath.add(coord08);
		movePath.add(coord09);
		movePath.add(coord10);

		setReloaded(true);
		setMoveState(0);
	}

	public void move(Cockpit cockpit) {
		int[] coords = movePath.get(getMoveState());

		setMoveState(getMoveState() + 1);

		if (getMoveState() == movePath.size()) {
			setMoveState(0);
			setReloaded(true);
		}

		cockpit.setFinalPosX(coords[0]);
		cockpit.setFinalPosY(coords[1]);

		if (cockpit.getMoveModifier() != null)
			cockpit.getSprite().unregisterEntityModifier(cockpit.getMoveModifier());

		cockpit.getSprite().registerEntityModifier(
				cockpit.moveModifier = new MoveModifier(cockpit.getSpeed(), cockpit.getSprite().getX(), cockpit.getFinalPosX(), cockpit.getSprite().getY(), cockpit.getFinalPosY()));
		// cockpit.getSprite().registerEntityModifier(
		// new QuadraticBezierCurveMoveModifier(1, cockpit.getSprite().getX(),
		// cockpit.getSprite().getY(), cockpit.getSprite().getX(),
		// cockpit.getSprite().getY() + 50, cockpit.getFinalPosX(),
		// cockpit.getFinalPosY()));

	}

	public boolean randomShoot() { // will do 1 shoot per passage, sure thing
		System.out.println(" size : " + movePath.size());
		System.out.println(" move stat + 1 : " + getMoveState() + 1);
		int randomShooter = MathTool.randInt(getMoveState() + 1, movePath.size());
		if (randomShooter == movePath.size()) {
			return true;
		}
		return false;

	}

	public int getMoveState() {
		return moveState;
	}

	public void setMoveState(int moveState) {
		this.moveState = moveState;
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

	public boolean isReloaded() {
		return reloaded;
	}

	public void setReloaded(boolean reloaded) {
		this.reloaded = reloaded;
	}
}
