package fr.gh.spacecontrol;

import org.andengine.engine.handler.IUpdateHandler;

public class GameLoopUpdateHandler implements IUpdateHandler {

	@Override
	public void onUpdate(float pSecondsElapsed) {
	    ((GameScene)BaseActivity.getSharedInstance().mCurrentScene).rotateTower();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

}
