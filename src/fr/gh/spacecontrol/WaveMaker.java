package fr.gh.spacecontrol;

public class WaveMaker {

	private int wave;
	private GameScene scene;

	public static WaveMaker instance;

	public static WaveMaker getSharedWaveMaker(GameScene scene) {
		if (instance == null)
			instance = new WaveMaker(scene);
		return instance;
	}

	private WaveMaker(GameScene scene) {
		super();
		this.scene = scene;
	}

	public void newWave() {
		for (int x = 0; x < this.wave * 1.3f; x++) {
			Enemy enemy = EnemyPool.sharedEnemyPool().obtainPoolItem();
			enemy.getSprite().setVisible(true);
			scene.attachChild(enemy.getSprite());
			scene.getEnemyList().add(enemy);
			enemy.init();
			
			enemy.getReactorLeft().getSprite().setVisible(true);
			enemy.getReactorRight().getSprite().setVisible(true);
			scene.attachChild(enemy.getReactorLeft().getSprite());
			scene.attachChild(enemy.getReactorRight().getSprite());
			scene.getReactorList().add(enemy.getReactorLeft());
			scene.getReactorList().add(enemy.getReactorRight());
			
		}

		this.wave++;
	}

	public int getWave() {
		return wave;
	}

	public void setWave(int wave) {
		this.wave = wave;
	}

}
