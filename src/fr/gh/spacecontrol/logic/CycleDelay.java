package fr.gh.spacecontrol.logic;

import java.util.Timer;
import java.util.TimerTask;

public class CycleDelay {
	private boolean valid;
	private Timer timer;
	private long delay = 1000;
	private static CycleDelay instance = null;

	public static CycleDelay getSharedInstance() {
		if (instance == null) {
			instance = new CycleDelay();
		}
		return instance;
	}

	private CycleDelay() {
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