package fr.gh.spacecontrol.items;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.QuadraticBezierCurveMoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.text.Text;
import org.andengine.util.modifier.ease.EaseLinear;

import fr.gh.spacecontrol.R;
import fr.gh.spacecontrol.activities.BaseActivity;
import fr.gh.spacecontrol.entity.modifier.BezierPathModifier;
import fr.gh.spacecontrol.entity.modifier.BezierPathModifier.BezierPath;
import fr.gh.spacecontrol.pools.EnemyPool;
import fr.gh.spacecontrol.primitive.Ellipse;
import fr.gh.spacecontrol.primitive.Mesh.DrawMode;
import fr.gh.spacecontrol.scenes.GameScene;
import fr.gh.spacecontrol.scenes.SplashScene;

public class SplashLogo {

	private Ellipse sprite1;
	private Ellipse sprite2;
	private Ellipse sprite3;
	private Text titleG;
	private Text title;

	protected static final int ELLIPSE_WIDTH_HEIGHT = 96;

	public SplashLogo(SplashScene scene) {
		BaseActivity activity = BaseActivity.getSharedInstance();
		Camera mCamera = BaseActivity.getSharedInstance().getmCamera();
		

		int centerX = (int) mCamera.getWidth() / 2;
		int centerY = (int) mCamera.getHeight() / 2;
		float animatedTime = 2.5f;

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

		QuadraticBezierCurveMoveModifier bezierCurveModifier1 = new QuadraticBezierCurveMoveModifier(animatedTime,
				0 - ELLIPSE_WIDTH_HEIGHT, 0 - ELLIPSE_WIDTH_HEIGHT, mCamera.getWidth() * 2 / 3, 0, centerX, centerY);

		QuadraticBezierCurveMoveModifier bezierCurveModifier2 = new QuadraticBezierCurveMoveModifier(animatedTime,
				mCamera.getWidth() + ELLIPSE_WIDTH_HEIGHT, mCamera.getHeight() * 2 / 3, 0, mCamera.getHeight() / 2,
				centerX, centerY);

		QuadraticBezierCurveMoveModifier bezierCurveModifier3 = new QuadraticBezierCurveMoveModifier(animatedTime,
				mCamera.getWidth() + ELLIPSE_WIDTH_HEIGHT, 0, mCamera.getWidth() * 2 / 3, mCamera.getWidth() / 2,
				centerX, centerY);

		sprite1.registerEntityModifier(bezierCurveModifier1);
		sprite2.registerEntityModifier(bezierCurveModifier2);
		sprite3.registerEntityModifier(bezierCurveModifier3);

		titleG = new Text(0, 0, activity.getmFont(), activity.getString(R.string.title_1),
				activity.getVertexBufferObjectManager());

		titleG.setPosition((activity.getmCamera().getWidth() - titleG.getWidth()) / 2, (activity.getmCamera()
				.getHeight() - titleG.getHeight()) / 2);
		titleG.registerEntityModifier(new ScaleModifier(animatedTime, 0.0f, 1.0f));
		titleG.setColor(0.0f, 0.0f, 0.0f);

		title = new Text(0, 0, activity.getmFont(), activity.getString(R.string.title_2),
				activity.getVertexBufferObjectManager());
		title.setScale(0.5f);
		title.setPosition(activity.getmCamera().getWidth() + title.getWidth() / 1.5f,
				titleG.getX() + titleG.getHeight() * 1.5f + title.getHeight() / 2);

		title.registerEntityModifier(new MoveXModifier(animatedTime, title.getX(), activity.getmCamera().getWidth() / 2));
		
		scene.registerUpdateHandler(new TimerHandler(animatedTime, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				sprite3.setAlpha(1.0f);
				sprite3.setColor(1.0f, 1.0f, 1.0f);
			}
		}));
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

	public Text getTitleG() {
		return titleG;
	}

	public void setTitleG(Text titleG) {
		this.titleG = titleG;
	}

	public Text getTitle() {
		return title;
	}

	public void setTitle(Text title) {
		this.title = title;
	}

}
