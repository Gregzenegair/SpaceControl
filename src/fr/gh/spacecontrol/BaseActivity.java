package fr.gh.spacecontrol;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.graphics.Typeface;

public class BaseActivity extends SimpleBaseGameActivity {

	static final int CAMERA_WIDTH = 480;
	static final int CAMERA_HEIGHT = 800;
	public Font mFont;
	public Camera mCamera;
	 
	//A reference to the current scene
	public Scene mCurrentScene;
	public Sound soundTowerGun;
	public Sound soundTowerGunb;
	public Sound soundImpact;
	public Music soundExplosion;
	public static BaseActivity instance;
	
	
	public EngineOptions onCreateEngineOptions() {
	    instance = this;
	    mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
	    EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
	    engineOptions.getAudioOptions().setNeedsSound(true);
	    engineOptions.getAudioOptions().setNeedsMusic(true);
	    return engineOptions;
	}

	protected void onCreateResources() {
	    mFont = FontFactory.create(this.getFontManager(),this.getTextureManager(), 256, 256,Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
	    mFont.load();
	    
		try {
			soundTowerGun = SoundFactory.createSoundFromAsset(BaseActivity
					.getSharedInstance().getSoundManager(), BaseActivity
					.getSharedInstance().getApplicationContext(),
					"sounds/soundTowerGun.mp3");
			soundTowerGunb = SoundFactory.createSoundFromAsset(BaseActivity
					.getSharedInstance().getSoundManager(), BaseActivity
					.getSharedInstance().getApplicationContext(),
					"sounds/soundTowerGunb.mp3");
			soundImpact = SoundFactory.createSoundFromAsset(BaseActivity
					.getSharedInstance().getSoundManager(), BaseActivity
					.getSharedInstance().getApplicationContext(),
					"sounds/soundImpact.mp3");
			soundExplosion = MusicFactory.createMusicFromAsset(BaseActivity.getSharedInstance().getMusicManager(), BaseActivity.getSharedInstance().getApplicationContext(), "sounds/soundExplosion.mp3");
			soundTowerGun.setVolume(0.2f);
			soundTowerGunb.setVolume(0.2f);
			soundImpact.setVolume(0.05f);
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
	
}
