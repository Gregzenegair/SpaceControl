package fr.gh.spacecontrol.items;

import java.util.LinkedList;

import android.R.bool;
import android.transition.Scene;
import fr.gh.spacecontrol.logic.RandomTool;
import fr.gh.spacecontrol.pools.CockpitPool;
import fr.gh.spacecontrol.pools.GunshipPool;
import fr.gh.spacecontrol.pools.ReactorPool;
import fr.gh.spacecontrol.scenes.BaseActivity;
import fr.gh.spacecontrol.scenes.GameScene;

public class Enemy {
	private Reactor reactorLeft;
	private Reactor reactorRight;
	private Cockpit cockpit;
	private Gunship gunship;

	private Tower aimedTower;
	private boolean moving;
	private boolean aiming;
	private boolean hasShot;
	private GameScene scene;

	public Enemy() {
		super();
		this.scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();
	}

	public void init() {
		this.cockpit = CockpitPool.sharedCockpitPool().obtainPoolItem();
		this.gunship = GunshipPool.sharedGunshipPool().obtainPoolItem();
		this.reactorLeft = ReactorPool.sharedReactorPool().obtainPoolItem();
		this.reactorRight = ReactorPool.sharedReactorPool().obtainPoolItem();

		this.cockpit.init(this);
		this.gunship.init(this);
		this.reactorLeft.init(this, Reactor.REACTOR_LEFT);
		this.reactorRight.init(this, Reactor.REACTOR_RIGHT);
		hasShot = true;
		moving = false;
		aiming = false;

	}

	public void remove() {
		this.reactorLeft.remove();
		this.reactorRight.remove();
		this.cockpit.remove();
		this.gunship.remove();

	}

	public void moveNShoot() {
		move();
		aim();
		shoot();
	}

	public void move() {
		if (!this.isDamaged() && hasShot) {
			this.getCockpit().move();
			this.getGunship().move();
			this.getReactorLeft().move();
			this.getReactorRight().move();
			moving = true;
			hasShot = false;
		}
	}

	public void aim() {
		LinkedList<Tower> towerList = scene.getTowerList();
		int towerNumber = RandomTool.randInt(0, 3);
		aimedTower = towerList.get(towerNumber);
		this.getGunship().aim(aimedTower);
		if (this.gunship.getSprite().getRotation() == this.getGunship().aim(aimedTower))
			aiming = false;
		else
			aiming = true;
	}

	public void shoot() {
		if (!aiming) {
			this.getGunship().shoot((int) this.getGunship().aim(aimedTower));
			hasShot = true;
			moving = false;
		}
	}

	public void addPhysics() {
		this.getCockpit().addPhysics();
		this.getGunship().addPhysics();
		this.getReactorLeft().addPhysics();
		this.getReactorRight().addPhysics();
	}

	public boolean isDamaged() {
		if (this.reactorLeft.isDestroyed() || this.reactorLeft.isPhysic() || this.reactorRight.isDestroyed()
				|| this.reactorRight.isPhysic() || this.cockpit.isDestroyed() || this.cockpit.isPhysic()
				|| this.cockpit.isDestroyed() || this.cockpit.isPhysic()) {
			return true;
		} else {
			return false;
		}
	}

	// Getters and setters

	public Reactor getReactorLeft() {
		return reactorLeft;
	}

	public void setReactorLeft(Reactor reactorLeft) {
		this.reactorLeft = reactorLeft;
	}

	public Reactor getReactorRight() {
		return reactorRight;
	}

	public void setReactorRight(Reactor reactorRight) {
		this.reactorRight = reactorRight;
	}

	public Cockpit getCockpit() {
		return cockpit;
	}

	public void setCockpit(Cockpit cockpit) {
		this.cockpit = cockpit;
	}

	public Gunship getGunship() {
		return gunship;
	}

	public void setGunship(Gunship gunship) {
		this.gunship = gunship;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public Tower getAimedTower() {
		return aimedTower;
	}

	public void setAimedTower(Tower aimedTower) {
		this.aimedTower = aimedTower;
	}

	public boolean isAiming() {
		return aiming;
	}

	public void setAiming(boolean aiming) {
		this.aiming = aiming;
	}

	public boolean hasShot() {
		return hasShot;
	}

	public void setHasShot(boolean hasShot) {
		this.hasShot = hasShot;
	}

}
