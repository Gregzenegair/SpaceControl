package fr.gh.spacecontrol;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

public final class particleEmitterExplosion {

	public static void createExplosion(final float posX, final float posY,
			final IEntity target, final SimpleBaseGameActivity activity,
			int mNumPart, final int width, final int height, float rotation) {

		int mTimePart = 2;
		int mRotation = (int) rotation;

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX,
				posY);
		IEntityFactory recFact = new IEntityFactory() {
			@Override
			public Rectangle create(float pX, float pY) {
				Rectangle rect = new Rectangle(posX, posY, width, height,
						activity.getVertexBufferObjectManager());
				return rect;
			}
		};
		final ParticleSystem particleSystem = new ParticleSystem(recFact,
				particleEmitter, 500, 500, mNumPart);

		if (mRotation > 0 && mRotation < 90) {
			particleSystem
					.addParticleInitializer(new VelocityParticleInitializer(0,
							20, 10, 20));
		} else {
			particleSystem
					.addParticleInitializer(new VelocityParticleInitializer(
							-20, 0, 10, 20));
		}

		particleSystem.addParticleModifier(new ColorParticleModifier(0,
				1.6f * mTimePart, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f));

		particleSystem.addParticleModifier(new AlphaParticleModifier(0,
				2.6f * mTimePart, 1, 0));

		particleSystem.addParticleModifier(new RotationParticleModifier(0,
				mTimePart, 0, 360));

		target.attachChild(particleSystem);

		// clean the sprties of the particle system to keep fps
		target.registerUpdateHandler(new TimerHandler(mTimePart * 4,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						particleSystem.detachSelf();
						target.sortChildren();
						target.unregisterUpdateHandler(pTimerHandler);
					}
				}));
	}

	public static void createBulletImpact(final float posX, final float posY,
			final IEntity target, final SimpleBaseGameActivity activity,
			final Color color, int mNumPart, final int width, final int height,
			float rotation) {

		int mTimePart = 2;
		int mRotation = (int) rotation;

		PointParticleEmitter particleEmitter = new PointParticleEmitter(posX,
				posY);
		IEntityFactory recFact = new IEntityFactory() {
			@Override
			public Rectangle create(float pX, float pY) {
				Rectangle rect = new Rectangle(posX, posY, width, height,
						activity.getVertexBufferObjectManager());
				rect.setColor(color);
				return rect;
			}
		};
		final ParticleSystem particleSystem = new ParticleSystem(recFact,
				particleEmitter, 200, 1500, mNumPart);

		if (mRotation > 0 && mRotation < 90) {
			particleSystem
					.addParticleInitializer(new VelocityParticleInitializer(0,
							20, 50, 100));
		} else {
			particleSystem
					.addParticleInitializer(new VelocityParticleInitializer(
							-20, 0, 50, 100));
		}

		particleSystem.addParticleModifier(new AlphaParticleModifier(0,
				2.6f * mTimePart, 1, 0));
		particleSystem.addParticleModifier(new RotationParticleModifier(0,
				mTimePart, 0, 360));

		target.attachChild(particleSystem);

		// clean the sprties of the particle system to keep fps
		target.registerUpdateHandler(new TimerHandler(mTimePart * 4,
				new ITimerCallback() {
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler) {
						particleSystem.detachSelf();
						target.sortChildren();
						target.unregisterUpdateHandler(pTimerHandler);
					}
				}));
	}
}
