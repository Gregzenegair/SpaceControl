package fr.gh.spacecontrol;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;

public class GameScene extends Scene {
	
	Camera mCamera;
	Tower tower;
	int fingerCoordX;
	int fingerCoordY;
	
	public GameScene() {
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
	    mCamera = BaseActivity.getSharedInstance().mCamera;
	    tower = new Tower();
	    attachChild(tower.sprite);
	}

	public void rotateTower() {
		tower.rotateTower(fingerCoordX, fingerCoordY);
		
	}
    
    
}
