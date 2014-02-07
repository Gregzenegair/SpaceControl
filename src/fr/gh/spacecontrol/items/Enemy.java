package fr.gh.spacecontrol.items;

import fr.gh.spacecontrol.pools.CockpitPool;
import fr.gh.spacecontrol.pools.GunshipPool;
import fr.gh.spacecontrol.pools.ReactorPool;

public class Enemy {
	private Reactor reactorLeft;
	private Reactor reactorRight;
	private Cockpit cockpit;
	private Gunship gunship;

	private boolean moving;

	public Enemy() {
		super();
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
	}

	public void remove() {
		this.reactorLeft.remove();
		this.reactorRight.remove();
		this.cockpit.remove();
		this.gunship.remove();

	}

	public void move() {
		this.getCockpit().move();
		this.getGunship().move();
		this.getReactorLeft().move();
		this.getReactorRight().move();
		moving = true;
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

	public boolean shoot() {

		return true;
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

}