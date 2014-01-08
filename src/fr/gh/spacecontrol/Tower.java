package fr.gh.spacecontrol;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;

	public class Tower {
	    public Rectangle sprite;
	    Camera mCamera;
	 
	 
	    public Tower() {
	        sprite = new Rectangle(0, 0, 70, 30, BaseActivity.getSharedInstance()
	            .getVertexBufferObjectManager());
	 
	        mCamera = BaseActivity.getSharedInstance().mCamera;
	        sprite.setPosition(mCamera.getWidth() / 2 - sprite.getWidth() / 2,
	            mCamera.getHeight() - sprite.getHeight() - 10);
	    }


		public void rotateTower(int fingerCoordX, int fingerCoordY) {
			// TODO Auto-generated method stub
			
		}
	
}
