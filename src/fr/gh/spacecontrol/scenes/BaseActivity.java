package fr.gh.spacecontrol.scenes;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.graphics.Typeface;

public class BaseActivity extends SimpleBaseGameActivity {

	static final int CAMERA_WIDTH = 600;
	static final int CAMERA_HEIGHT = 800;
	static final int NO_SCREEN = 0;
	static final int SPLASH_SCREEN = 1;
	static final int MAIN_MENU_SCREEN = 2;
	static final int GAME_SCREEN = 10;

	private Font mFont;
	private Camera mCamera;
	private int currentScreen = 0;
	private Scene gameScene;
	private boolean gameStarted = false;

	// A reference to the current scene
	private Scene mCurrentScene;
	private Sound soundTowerGun;
	private Sound soundTowerGunb;
	private Sound soundImpact;
	private Music soundExplosion;

	private BitmapTextureAtlas textureAtlas;
	public ITextureRegion bunkerTexture;
	public ITextureRegion towerTexture;
	public ITextureRegion enemyCockpitTexture;
	public ITextureRegion enemyReactorTexture;
	public ITextureRegion enemyGunshipTexture;
	public ITextureRegion logoTexture;

	private static BaseActivity instance;

	public EngineOptions onCreateEngineOptions() {
		instance = this;
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR,
				new FillResolutionPolicy(), mCamera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		return engineOptions;
	}

	protected void onCreateResources() {
		try {
			mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 64, Color.WHITE.hashCode());
			mFont.load();
			
			soundTowerGun = SoundFactory.createSoundFromAsset(BaseActivity.getSharedInstance().getSoundManager(),
					BaseActivity.getSharedInstance().getApplicationContext(), "sounds/soundTowerGun.mp3");
			soundTowerGunb = SoundFactory.createSoundFromAsset(BaseActivity.getSharedInstance().getSoundManager(),
					BaseActivity.getSharedInstance().getApplicationContext(), "sounds/soundTowerGunb.mp3");
			soundImpact = SoundFactory.createSoundFromAsset(BaseActivity.getSharedInstance().getSoundManager(),
					BaseActivity.getSharedInstance().getApplicationContext(), "sounds/soundImpact.mp3");
			soundExplosion = MusicFactory.createMusicFromAsset(BaseActivity.getSharedInstance().getMusicManager(),
					BaseActivity.getSharedInstance().getApplicationContext(), "sounds/soundExplosion.mp3");
			soundTowerGun.setVolume(0.2f);
			soundTowerGunb.setVolume(0.2f);
			soundImpact.setVolume(0.05f);

			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			textureAtlas = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.DEFAULT);
			bunkerTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(textureAtlas, this, "bunker.png", 0,
					0);
			towerTexture = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(textureAtlas, this, "tower.png", 32, 0);
			
			enemyCockpitTexture = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(textureAtlas, this, "cockpitGreen_0.png", 64, 0);
			
			enemyReactorTexture = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(textureAtlas, this, "wingGreen_0.png", 96, 0);
			
			enemyGunshipTexture = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(textureAtlas, this, "gun00.png", 128, 0);
			
//			logoTexture = BitmapTextureAtlasTextureRegionFactory
//					.createFromAsset(textureAtlas, this, "logo.png", 0, 64);

			textureAtlas.load();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		mCurrentScene = new SplashScene();
		return mCurrentScene;
	}

	public static BaseActivity getSharedInstance() {
		return instance;
	}

	// to change the current main scene
	public void setCurrentScene(Scene scene) {
		mCurrentScene = scene;
		getEngine().setScene(mCurrentScene);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (this.isGameLoaded()) {
			if (this.currentScreen == GAME_SCREEN) {
				this.mCurrentScene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;
			}
		}

	}

	@Override
	protected synchronized void onResume() {
		super.onResume();
		System.gc();
		if (this.isGameLoaded()) {
			switch (this.currentScreen) {
			case NO_SCREEN:
				this.setCurrentScene(new SplashScene());
				break;
			case SPLASH_SCREEN:
				this.setCurrentScene(new SplashScene());
				break;
			case MAIN_MENU_SCREEN:
				this.setCurrentScene(new MainMenuScene());
				break;
			case GAME_SCREEN:
				this.setCurrentScene(mCurrentScene);
				break;
			default:
				this.setCurrentScene(new SplashScene());
				break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (this.isGameLoaded()) {
			System.exit(0);
		}
	}

	@Override
	public void onBackPressed() {
		if (this.currentScreen == GAME_SCREEN) {
			this.gameScene = (GameScene) BaseActivity.getSharedInstance().mCurrentScene;
			this.setCurrentScene(new MainMenuScene());
		}
	}

	public Font getmFont() {
		return mFont;
	}

	public void setmFont(Font mFont) {
		this.mFont = mFont;
	}

	public Camera getmCamera() {
		return mCamera;
	}

	public void setmCamera(Camera mCamera) {
		this.mCamera = mCamera;
	}

	public int getCurrentScreen() {
		return currentScreen;
	}

	public void setCurrentScreen(int currentScreen) {
		this.currentScreen = currentScreen;
	}

	public boolean isGameStarted() {
		return gameStarted;
	}

	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}

	public Scene getCurrentScene() {
		return mCurrentScene;
	}

	public void setmCurrentScene(Scene mCurrentScene) {
		this.mCurrentScene = mCurrentScene;
	}

	public Sound getSoundTowerGun() {
		return soundTowerGun;
	}

	public void setSoundTowerGun(Sound soundTowerGun) {
		this.soundTowerGun = soundTowerGun;
	}

	public Sound getSoundTowerGunb() {
		return soundTowerGunb;
	}

	public void setSoundTowerGunb(Sound soundTowerGunb) {
		this.soundTowerGunb = soundTowerGunb;
	}

	public Sound getSoundImpact() {
		return soundImpact;
	}

	public void setSoundImpact(Sound soundImpact) {
		this.soundImpact = soundImpact;
	}

	public Music getSoundExplosion() {
		return soundExplosion;
	}

	public void setSoundExplosion(Music soundExplosion) {
		this.soundExplosion = soundExplosion;
	}

	public static BaseActivity getInstance() {
		return instance;
	}

	public static void setInstance(BaseActivity instance) {
		BaseActivity.instance = instance;
	}

	public Scene getGameScene() {
		return gameScene;
	}

	public void setGameScene(Scene gameScene) {
		this.gameScene = gameScene;
	}

	public BitmapTextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	public void setTextureAtlas(BitmapTextureAtlas textureAtlas) {
		this.textureAtlas = textureAtlas;
	}


}
