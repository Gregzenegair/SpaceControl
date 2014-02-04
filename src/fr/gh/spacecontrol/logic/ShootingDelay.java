package fr.gh.spacecontrol.logic;

import java.util.Timer;
import java.util.TimerTask;

public class ShootingDelay {
	private boolean valid;
	private Timer timer;
	private long delay = 50;
	private static ShootingDelay instance = null;

	public static ShootingDelay getSharedInstance() {
		if (instance == null) {
			instance = new ShootingDelay();
		}
		return instance;
	}

	private ShootingDelay() {
		timer = new Timer();
		valid = true;
	}

	public boolean checkValidity() {
		if (valid) {
			valid = false;
			timer.schedule(new Task(), delay);
			return true;
		}
		return false;
	}

	class Task extends TimerTask {

		public void run() {
			valid = true;
		}

	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

}