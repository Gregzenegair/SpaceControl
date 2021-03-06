package fr.gh.spacecontrol.items;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import fr.gh.spacecontrol.activities.BaseActivity;
import fr.gh.spacecontrol.logic.MathTool;
import fr.gh.spacecontrol.pools.WreckagePool;
import fr.gh.spacecontrol.scenes.GameScene;

public final class ParticleEmitterExplosion {

	public static void createExplosion(final float posX, final float posY, final IEntity target, final SimpleBaseGameActivity activity, int mNumPart, final int width, final int height, float rotation) {

		if (target == null) {
			return;
		}

		int mTimePart = 2;
		int mRotation = (int) rotation;

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX, posY);
		IEntityFactory<Rectangle> recFact = new IEntityFactory<Rectangle>() {
			@Override
			public Rectangle create(float pX, float pY) {
				Rectangle rect = new Rectangle(posX, posY, width, height, activity.getVertexBufferObjectManager());
				return rect;
			}
		};
		final ParticleSystem particleSystem = new ParticleSystem(recFact, particleEmitter, 500, 500, mNumPart);

		if (mRotation > 0 && mRotation < 90) {
			particleSystem.addParticleInitializer(new VelocityParticleInitializer(-10, 10, -10, -20));
		} else {
			particleSystem.addParticleInitializer(new VelocityParticleInitializer(-10, 10, -10, -20));
		}

		particleSystem.addParticleModifier(new ColorParticleModifier(0, 1.6f * mTimePart, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f));

		particleSystem.addParticleModifier(new AlphaParticleModifier(0, 2.6f * mTimePart, 1, 0));

		particleSystem.addParticleModifier(new RotationParticleModifier(0, mTimePart, 0, 360));

		target.attachChild(particleSystem);

		// clean the sprites of the particle system to keep fps
		target.registerUpdateHandler(new TimerHandler(mTimePart * 4, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				particleSystem.detachSelf();
				target.sortChildren();
				target.unregisterUpdateHandler(pTimerHandler);
			}
		}));
	}

	public static void createSmoke(final float posX, final float posY, final IEntity target, final SimpleBaseGameActivity activity, int mNumPart, final int width, final int height) {

		if (target == null) {
			return;
		}

		int mTimePart = 2;

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX, posY);
		IEntityFactory<Rectangle> recFact = new IEntityFactory<Rectangle>() {
			@Override
			public Rectangle create(float pX, float pY) {
				Rectangle rect = new Rectangle(posX, posY, width, height, activity.getVertexBufferObjectManager());
				return rect;
			}
		};
		final ParticleSystem particleSystem = new ParticleSystem(recFact, particleEmitter, 500, 500, mNumPart);

		particleSystem.addParticleInitializer(new VelocityParticleInitializer(-10, 10, -10, -3));

		particleSystem.addParticleModifier(new ColorParticleModifier(0, 2.6f * mTimePart, 0.2f, 0.2f, 0.2f, 0.6123f, 0.513f, 0.545f));

		particleSystem.addParticleModifier(new AlphaParticleModifier(0, 2.6f * mTimePart, 1, 0));

		target.attachChild(particleSystem);

		// clean the sprites of the particle system to keep fps
		target.registerUpdateHandler(new TimerHandler(mTimePart * 4, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				particleSystem.detachSelf();
				target.sortChildren();
				target.unregisterUpdateHandler(pTimerHandler);
			}
		}));
	}

	public static void createBulletImpact(final float posX, final float posY, final IEntity target, final SimpleBaseGameActivity activity, final Color color, int mNumPart, final int width,
			final int height, float rotation) {

		if (target == null) {
			return;
		}

		GameScene scene = (GameScene) BaseActivity.getSharedInstance().getCurrentScene();
		int mTimePart = 2;
		int mReveredRotation = (int) (rotation - 90 + rotation / 2);

		final Wreckage wreckage = WreckagePool.sharedWreckagePool().obtainPoolItem();
		wreckage.getSprite().setPosition(posX, posY);

		wreckage.getSprite().setRotation(mReveredRotation);
		MoveModifier movMod = new MoveModifier(0.2f, posX, posX - ((float) Math.cos(Math.toRadians(mReveredRotation)) * 10), posY, posY + (float) Math.sin(Math.toRadians(mReveredRotation)) * 10);

		PhysicsHandler movMod2 = new PhysicsHandler(wreckage.getSprite());
		wreckage.getSprite().registerUpdateHandler(movMod2);
		movMod2.setVelocity(0, 100);

		final RotationModifier rotMod = new RotationModifier(1.0f, mReveredRotation, MathTool.randInt(360, 1440));
		AlphaModifier alphaMod = new AlphaModifier(mTimePart * 2, 1.0f, 0.0f);
		wreckage.getSprite().setVisible(true);
		scene.attachChild(wreckage.getSprite());
		scene.getWreckageList().add(wreckage);

		wreckage.getSprite().registerEntityModifier(movMod);
		wreckage.getSprite().registerEntityModifier(alphaMod);

		target.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				// wreckage.sprite.registerEntityModifier(movMod2);
				wreckage.getSprite().registerEntityModifier(rotMod);
				target.sortChildren();
			}
		}));

		// clean the sprites of the particle system to keep fps
		target.registerUpdateHandler(new TimerHandler(mTimePart * 2, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				WreckagePool.sharedWreckagePool().recyclePoolItem(wreckage);
				target.sortChildren();
				target.unregisterUpdateHandler(pTimerHandler);
			}
		}));
	}
}
