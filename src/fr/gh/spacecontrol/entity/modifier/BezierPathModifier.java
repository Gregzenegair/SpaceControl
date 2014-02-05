package fr.gh.spacecontrol.entity.modifier;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.EntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.SequenceModifier;
import org.andengine.util.modifier.ease.IEaseFunction;

import android.util.FloatMath;

/**
 * Move object along smooth path with straight lines and quadratic bezier curves
 *
 * @author Poyaganov Oleg
 * @date 12/6/11
 */
public class BezierPathModifier extends EntityModifier {

    private final SequenceModifier<IEntity> mSequenceModifier;

    public BezierPathModifier(final float pDuration, final BezierPath pPath, final IEaseFunction pEaseFuction) {
        this(pDuration, pPath, null, pEaseFuction);
    }

    public BezierPathModifier(final float pDuration, final BezierPath pPath, final IEntityModifierListener pEntityModiferListener, final IEaseFunction pEaseFunction) throws IllegalArgumentException {
        super(pEntityModiferListener);
        final int pathSize = pPath.getSize();

        if (pathSize < 2) {
            throw new IllegalArgumentException("Path needs at least 2 waypoints!");
        }

        final float[] coordinatesX = pPath.getCoordinatesX();
        final float[] coordinatesY = pPath.getCoordinatesY();

        final float[] controlX = pPath.getControlX();
        final float[] controlY = pPath.getmControlY();

        IModifier[] moveModifiers = new IModifier[pathSize - 1];

        final float velocity = pPath.getLength() / pDuration;

        final int modifierCount = moveModifiers.length;

        for (int i = 0; i < modifierCount; i++) {
            final float duration = pPath.getSegmentLength(i) / velocity;

            if (pPath.isLineSegment(i)) {
                moveModifiers[i] = (new MoveModifier(duration,
                        coordinatesX[i], coordinatesX[i + 1],
                        coordinatesY[i], coordinatesY[i + 1],
                        null, pEaseFunction));
            } else {
                moveModifiers[i] = (new QuadraticBezierMoveModifier(duration,
                        coordinatesX[i], coordinatesY[i],
                        controlX[i + 1], controlY[i + 1],
                        coordinatesX[i + 1], coordinatesY[i + 1],
                        pEaseFunction));
            }
        }

        this.mSequenceModifier = new SequenceModifier<IEntity>(
                new SequenceModifier.ISubSequenceModifierListener<IEntity>() {
                    @Override
                    public void onSubSequenceStarted(final IModifier<IEntity> pModifier, final IEntity pEntity, final int pIndex) {
                    }

                    @Override
                    public void onSubSequenceFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity, final int pIndex) {
                    }
                },
                new IEntityModifierListener() {
                    @Override
                    public void onModifierStarted(final IModifier<IEntity> pModifier, final IEntity pEntity) {
                        BezierPathModifier.this.onModifierStarted(pEntity);
                    }

                    @Override
                    public void onModifierFinished(final IModifier<IEntity> pEntityModifier, final IEntity pEntity) {
                        BezierPathModifier.this.onModifierFinished(pEntity);
                    }
                },
                moveModifiers
        );
    }

    protected BezierPathModifier(final BezierPathModifier pPathModifier) throws DeepCopyNotSupportedException {
        this.mSequenceModifier = pPathModifier.mSequenceModifier.deepCopy();
    }

    @Override
    public void reset() {
        this.mSequenceModifier.reset();
    }

    @Override
    public BezierPathModifier deepCopy() throws DeepCopyNotSupportedException {
        return new BezierPathModifier(this);
    }

    @Override
    public float getSecondsElapsed() {
        return this.mSequenceModifier.getSecondsElapsed();
    }

    @Override
    public float getDuration() {
        return this.mSequenceModifier.getDuration();
    }

    @Override
    public float onUpdate(float pSecondsElapsed, IEntity pEntity) {
        return this.mSequenceModifier.onUpdate(pSecondsElapsed, pEntity);
    }

    @Override
    public boolean isFinished() {
        return this.mSequenceModifier.isFinished();
    }

    public static class BezierPath {
        private final float[] mCoordinatesX;
        private final float[] mCoordinatesY;

        private final float[] mControlX;
        private final float[] mControlY;

        private int mIndex;
        private boolean mLengthChanged = false;
        private float mLength;

        /**
         * Representation of path with straight lines and quadratic bezier curves
         *
         * @param pLength - number of points
         */
        public BezierPath(final int pLength) {
            this.mCoordinatesX = new float[pLength];
            this.mCoordinatesY = new float[pLength];
            this.mControlX = new float[pLength];
            this.mControlY = new float[pLength];

            this.mIndex = 0;
            this.mLengthChanged = false;
        }

        /**
         * Builds path to the next point with control point for bezier function
         *
         * @param pX - x coordinate of the next point
         * @param pY - y coordinate of the next point
         * @param pControlX - quadratic bezier control point x coordinate
         * @param pControlY - quadratic bezier control point y coordinate
         * @return
         */
        public BezierPath to(final float pX, final float pY, final float pControlX, final float pControlY) {
            this.mCoordinatesX[this.mIndex] = pX;
            this.mCoordinatesY[this.mIndex] = pY;

            this.mControlX[this.mIndex] = pControlX;
            this.mControlY[this.mIndex] = pControlY;

            this.mIndex++;

            this.mLengthChanged = true;

            return this;
        }

        /**
         * Builds path to the next point without control point.
         * Defines simple straight line
         *
         * @param pX
         * @param pY
         * @return
         */
        public BezierPath to(final float pX, final float pY) {
            return to(pX, pY, pX, pY);
        }

        public float getLength() {
            if (this.mLengthChanged) {
                this.updateLength();
            }
            return this.mLength;
        }

        public float getSegmentLength(final int pSegmentIndex) {
            final int nextSegmentIndex = pSegmentIndex + 1;

            final float x1 = this.mCoordinatesX[pSegmentIndex];
            final float y1 = this.mCoordinatesY[pSegmentIndex];

            final float x2 = this.mControlX[nextSegmentIndex];
            final float y2 = this.mControlY[nextSegmentIndex];

            final float x3 = this.mCoordinatesX[nextSegmentIndex];
            final float y3 = this.mCoordinatesY[nextSegmentIndex];

            if (x3 == x2 && y3 == y2) {
                // straight line
                final float dx = x1 - x3;
                final float dy = y1 - y3;

                return FloatMath.sqrt(dx * dx + dy * dy);
            } else {
                // curve line
                final float ax = x1 - 2 * x2 + x3;
                final float ay = y1 - 2 * y2 + y3;

                final float bx = 2 * x2 - 2 * x1;
                final float by = 2 * y2 - 2 * y1;

                final float A = 4 * (ax * ax + ay * ay);
                final float B = 4 * (ax * bx + ay * by);
                final float C = bx * bx + by * by;

                final float Sabc = 2 * FloatMath.sqrt(A + B + C);

                final float A_2 = FloatMath.sqrt(A);

                final float A_32 = 2 * A * A_2;

                final float C_2 = 2 * FloatMath.sqrt(C);

                final float BA = B / A_2;

                return (A_32 * Sabc + A_2 * B * (Sabc - C_2) + (4 * C * A - B * B) * (float) Math.log((2 * A_2 + BA + Sabc) / (BA + C_2))) / (4 * A_32);
            }
        }

        /**
         * Checks whether path segment is line or quadratic bezier curve
         *
         * @param pSegmentIndex
         * @return
         */
        public boolean isLineSegment(int pSegmentIndex) {
            final int nextSegmentIndex = pSegmentIndex + 1;

            final float x2 = this.mControlX[nextSegmentIndex];
            final float y2 = this.mControlY[nextSegmentIndex];

            final float x3 = this.mCoordinatesX[nextSegmentIndex];
            final float y3 = this.mCoordinatesY[nextSegmentIndex];

            return (x3 == x2 && y3 == y2);
        }

        private void updateLength() {
            float length = 0.0f;

            for (int i = this.mIndex - 2; i >= 0; i--) {
                length += this.getSegmentLength(i);
            }
            this.mLength = length;
        }

        /**
         * @return number of path points
         */
        public int getSize() {
            return this.mCoordinatesX.length;
        }

        public float[] getCoordinatesX() {
            return this.mCoordinatesX;
        }

        public float[] getCoordinatesY() {
            return this.mCoordinatesY;
        }

        public float[] getControlX() {
            return this.mControlX;
        }

        public float[] getmControlY() {
            return this.mControlY;
        }
    }

}