package fr.gh.spacecontrol.items;

import java.util.LinkedList;

import android.R.bool;
import android.transition.Scene;
import fr.gh.spacecontrol.activities.BaseActivity;
import fr.gh.spacecontrol.logic.MathTool;
import fr.gh.spacecontrol.pools.CockpitPool;
import fr.gh.spacecontrol.pools.GunshipPool;
import fr.gh.spacecontrol.pools.ReactorPool;
import fr.gh.spacecontrol.scenes.GameScene;

public class Enemy {
	private Reactor reactorLeft;
	private Reactor reactorRight;
	private Cockpit cockpit;
	private Gunship gunship;

	private int aimedTower;
	private boolean moving;
	private boolean aimed;
	private boolean hasShot;
	private boolean aiming;
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
		this.aiming = false;
		this.hasShot = false;
		this.moving = false;
		this.aimed = false;
		move();
	}

	public void remove() {
		this.reactorLeft.remove();
		this.reactorRight.remove();
		this.cockpit.remove();
		this.gunship.remove();

	}

	public void moveNShoot() {
		if (!this.isDamaged()) {
			moving = this.cockpit.isMooving();
			if (!hasShot && !moving && !aimed && !isAiming()) {
				aim();
				setAiming(true);
			} else if (hasShot) {
				move();
			} else {
				shoot();
			}
		}
	}

	public void move() {
		if (!this.isDamaged()) {
			this.getCockpit().move();
			this.getGunship().move();
			this.getReactorLeft().move();
			this.getReactorRight().move();
			this.moving = this.cockpit.isMooving();
			setAiming(false);
			this.hasShot = false;
			setAimedTower(MathTool.randInt(0, 3));
		}
	}

	public void aim() {
		if (!this.isDamaged()) {
			this.getGunship().aim(getAimedTower());

			this.aimed = true;
			setAiming(false);
		}
	}

	public void shoot() {
		if (!this.isDamaged()) {
			if (aimed) {
				this.getGunship().shoot((int) this.getGunship().aim(getAimedTower()));
				this.hasShot = true;
				this.moving = this.cockpit.isMooving();
			}
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

	public boolean isAiming() {
		return aimed;
	}

	public void setAiming(boolean aiming) {
		this.aiming = aiming;
	}

	public void setAimed(boolean aimed) {
		this.aimed = aimed;
	}

	public boolean hasShot() {
		return hasShot;
	}

	public void setHasShot(boolean hasShot) {
		this.hasShot = hasShot;
	}

	public int getAimedTower() {
		return aimedTower;
	}

	public void setAimedTower(int aimedTower) {
		this.aimedTower = aimedTower;
	}

}
