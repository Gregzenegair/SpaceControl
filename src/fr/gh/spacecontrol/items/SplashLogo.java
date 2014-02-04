package fr.gh.spacecontrol.items;

import org.andengine.engine.camera.Camera;

import fr.gh.spacecontrol.primitive.Ellipse;
import fr.gh.spacecontrol.scenes.BaseActivity;

public class SplashLogo {

	private Camera mCamera;
	private Ellipse sprite1;
	private Ellipse sprite2;
	private Ellipse sprite3;

	public SplashLogo() {
		this.mCamera = BaseActivity.getSharedInstance().getmCamera();

		sprite1 = new Ellipse(0, 0, 20, 20, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		sprite1.setColor(1.0f, 0.0f, 0.0f);
		sprite1.setAlpha(1 / 3);

		sprite2 = new Ellipse(0, 0, 20, 20, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		sprite2.setColor(0.0f, 1.0f, 0.0f);
		sprite2.setAlpha(1 / 3);

		sprite3 = new Ellipse(0, 0, 20, 20, BaseActivity.getSharedInstance().getVertexBufferObjectManager());
		sprite3.setColor(0.0f, 0.0f, 1.0f);
		sprite3.setAlpha(1 / 3);

		int centerX = (int) mCamera.getWidth() / 2;
		int centerY = (int) mCamera.getHeight() / 2;

	}

	public Ellipse getSprite1() {
		return sprite1;
	}

	public void setSprite1(Ellipse sprite1) {
		this.sprite1 = sprite1;
	}

	public Ellipse getSprite2() {
		return sprite2;
	}

	public void setSprite2(Ellipse sprite2) {
		this.sprite2 = sprite2;
	}

	public Ellipse getSprite3() {
		return sprite3;
	}

	public void setSprite3(Ellipse sprite3) {
		this.sprite3 = sprite3;
	}

}
