package fr.gh.spacecontrol;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

public class GameScene extends Scene implements IOnSceneTouchListener {

	private Camera mCamera;
	public Tower tower1;
	private Tower tower2;
	private Tower tower3;
	private Tower tower4;
	private TowerAxe towerAxe;
	public boolean shoot;
	public LinkedList<Bullet> bulletList;

	private BaseActivity activity;
	private Text text1;
	public int bulletCount;

	public GameScene() {
		mCamera = BaseActivity.getSharedInstance().mCamera;
		setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		bulletList = new LinkedList<Bullet>();

		int towerSizeX = 10;
		int towerSizeY = 40;

		tower1 = new Tower(towerSizeX, towerSizeY);
		tower1.setPosition(20, (int) mCamera.getHeight() - tower1.getHeight()
				- 240);
		attachChild(tower1.sprite);
		attachChild(tower1.spriteBase);
		tower2 = new Tower(towerSizeX, towerSizeY);
		tower2.setPosition(20, (int) mCamera.getHeight() - tower2.getHeight());
		attachChild(tower2.sprite);
		tower3 = new Tower(towerSizeX, towerSizeY);
		tower3.setPosition((int) mCamera.getWidth() - tower3.getWidth() - 20,
				(int) mCamera.getHeight() - tower3.getHeight());
		attachChild(tower3.sprite);
		tower4 = new Tower(towerSizeX, towerSizeY);
		tower4.setPosition((int) mCamera.getWidth() - tower4.getWidth() - 20,
				(int) mCamera.getHeight() - tower4.getHeight() - 240);
		attachChild(tower4.sprite);

		this.setOnSceneTouchListener(this);

		activity = BaseActivity.getSharedInstance();
		text1 = new Text(20, 20, activity.mFont, "Score : ",
				activity.getVertexBufferObjectManager());
		text1.setScale(0.5f);
		attachChild(text1);
		
		//Enregistrement d'un update handler
		registerUpdateHandler(new GameLoopUpdateHandler());
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.isActionDown()) {
			towerAxe = new TowerAxe(pSceneTouchEvent.getX(),
					pSceneTouchEvent.getY());
			tower1.setStartingAngle(tower1.getAngle());
			this.shoot = true;
		}
		if (pSceneTouchEvent.isActionMove()) {
			tower1.rotateTower(pSceneTouchEvent.getY(), towerAxe.getStartingY());

		}
		if (pSceneTouchEvent.isActionUp()) {
			towerAxe = null;
			this.shoot = false;
		}

		return false;
	}

	public void cleaner() {
		synchronized (this) {
			Iterator<Bullet> it = bulletList.iterator();
			while (it.hasNext()) {
				Bullet b = it.next();
				if (b.sprite.getY() <= -b.sprite.getHeight()) {
					BulletPool.sharedBulletPool().recyclePoolItem(b);
					it.remove();
					continue;
				}
			}
		}
	}

}
