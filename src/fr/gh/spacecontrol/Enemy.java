package fr.gh.spacecontrol;

import org.andengine.entity.primitive.Rectangle;

public class Enemy {
	private Reactor reactorLeft;
	private Reactor reactorRight;
	private Cockpit cockpit;


	
	public Enemy() {
		super();
		this.cockpit = CockpitPool.sharedEnemyBodyPool().obtainPoolItem();
		this.reactorLeft = ReactorPool.sharedReactorPool().obtainPoolItem();
		this.reactorRight = ReactorPool.sharedReactorPool().obtainPoolItem();
		
		this.cockpit.init(this);
		this.reactorLeft.init(this, Reactor.REACTOR_LEFT);
		this.reactorRight.init(this, Reactor.REACTOR_RIGHT);
	}

	public void remove() {
		this.reactorLeft.remove();
		this.reactorRight.remove();
		this.cockpit.remove();
	}

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

	
}
