package fr.gh.spacecontrol.items;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.QuadraticBezierCurveMoveModifier;
import org.andengine.util.modifier.ease.EaseLinear;

import fr.gh.spacecontrol.entity.modifier.BezierPathModifier;
import fr.gh.spacecontrol.entity.modifier.BezierPathModifier.BezierPath;
import fr.gh.spacecontrol.primitive.Ellipse;
import fr.gh.spacecontrol.primitive.Mesh.DrawMode;
import fr.gh.spacecontrol.scenes.BaseActivity;

public class SplashLogo {

	private Camera mCamera;
	private Ellipse sprite1;
	private Ellipse sprite2;
	private Ellipse sprite3;
	private Ellipse sprite4;

	protected static final int ELLIPSE_WIDTH_HEIGHT = 96;

	public SplashLogo() {
		this.mCamera = BaseActivity.getSharedInstance().getmCamera();
		int centerX = (int) mCamera.getWidth() / 2;
		int centerY = (int) mCamera.getHeight() / 2;

		sprite1 = new Ellipse(centerX, centerY, ELLIPSE_WIDTH_HEIGHT, ELLIPSE_WIDTH_HEIGHT, BaseActivity
				.getSharedInstance().getVertexBufferObjectManager());
		sprite1.setColor(0.8f, 0.2f, 0.2f);
		sprite1.setDrawMode(DrawMode.TRIANGLE_FAN);
		sprite1.setAlpha(0.33f);

		sprite2 = new Ellipse(centerX, centerY, ELLIPSE_WIDTH_HEIGHT, ELLIPSE_WIDTH_HEIGHT, BaseActivity
				.getSharedInstance().getVertexBufferObjectManager());
		sprite2.setColor(0.2f, 0.8f, 0.2f);
		sprite2.setDrawMode(DrawMode.TRIANGLE_FAN);
		sprite2.setAlpha(0.33f);

		sprite3 = new Ellipse(centerX, centerY, ELLIPSE_WIDTH_HEIGHT, ELLIPSE_WIDTH_HEIGHT, BaseActivity
				.getSharedInstance().getVertexBufferObjectManager());
		sprite3.setColor(0.2f, 0.2f, 0.8f);
		sprite3.setDrawMode(DrawMode.TRIANGLE_FAN);
		sprite3.setAlpha(0.33f);

		QuadraticBezierCurveMoveModifier bezierCurveModifier1 = new QuadraticBezierCurveMoveModifier(2.5f,
				0 - ELLIPSE_WIDTH_HEIGHT, 0 - ELLIPSE_WIDTH_HEIGHT, mCamera.getWidth() * 2 / 3, 0, centerX, centerY);
		
		QuadraticBezierCurveMoveModifier bezierCurveModifier2 = new QuadraticBezierCurveMoveModifier(2.5f,
				mCamera.getWidth() + ELLIPSE_WIDTH_HEIGHT, mCamera.getHeight() * 2 / 3, 0, mCamera.getHeight()/2,
				centerX, centerY);
		
		QuadraticBezierCurveMoveModifier bezierCurveModifier3 = new QuadraticBezierCurveMoveModifier(2.5f,
				mCamera.getWidth() + ELLIPSE_WIDTH_HEIGHT, 0, mCamera.getWidth() * 2 / 3, mCamera.getWidth() / 2, centerX, centerY);

		sprite1.registerEntityModifier(bezierCurveModifier1);
		sprite2.registerEntityModifier(bezierCurveModifier2);
		sprite3.registerEntityModifier(bezierCurveModifier3);
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
