package fr.guddy.androidstarteralt.mvp.changehandlers;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import com.bluelinelabs.conductor.changehandler.AnimatorChangeHandler;

/**
 * An {@link AnimatorChangeHandler} that will perform a circular reveal
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CircularRevealChangeHandler extends AnimatorChangeHandler {

    private static final String KEY_CX = "CircularRevealChangeHandler.cx";
    private static final String KEY_CY = "CircularRevealChangeHandler.cy";

    private int mCx;
    private int mCy;

    public CircularRevealChangeHandler() {
    }

    /**
     * Constructor that will create a circular reveal from the center of the fromView parameter.
     *
     * @param fromView      The view from which the circular reveal should originate
     * @param containerView The view that hosts fromView
     */
    public CircularRevealChangeHandler(@NonNull View fromView, @NonNull View containerView) {
        this(fromView, containerView, DEFAULT_ANIMATION_DURATION);
    }

    /**
     * Constructor that will create a circular reveal from the center of the fromView parameter.
     *
     * @param fromView      The view from which the circular reveal should originate
     * @param containerView The view that hosts fromView
     * @param duration      The duration of the animation
     */
    public CircularRevealChangeHandler(@NonNull View fromView, @NonNull View containerView, long duration) {
        this(fromView, containerView, duration, true);
    }

    /**
     * Constructor that will create a circular reveal from the center of the fromView parameter.
     *
     * @param fromView              The view from which the circular reveal should originate
     * @param containerView         The view that hosts fromView
     * @param duration              The duration of the animation
     * @param removesFromViewOnPush If true, the view being replaced will be removed from the view hierarchy on pushes
     */
    public CircularRevealChangeHandler(@NonNull View fromView, @NonNull View containerView, long duration, boolean removesFromViewOnPush) {
        super(duration, removesFromViewOnPush);

        int[] fromLocation = new int[2];
        fromView.getLocationInWindow(fromLocation);

        int[] containerLocation = new int[2];
        containerView.getLocationInWindow(containerLocation);

        int relativeLeft = fromLocation[0] - containerLocation[0];
        int relativeTop = fromLocation[1] - containerLocation[1];

        mCx = fromView.getWidth() / 2 + relativeLeft;
        mCy = fromView.getHeight() / 2 + relativeTop;
    }

    /**
     * Constructor that will create a circular reveal from the center of the fromView parameter.
     *
     * @param fromView              The view from which the circular reveal should originate
     * @param containerView         The view that hosts fromView
     * @param removesFromViewOnPush If true, the view being replaced will be removed from the view hierarchy on pushes
     */
    public CircularRevealChangeHandler(@NonNull View fromView, @NonNull View containerView, boolean removesFromViewOnPush) {
        this(fromView, containerView, DEFAULT_ANIMATION_DURATION, true);
    }

    /**
     * Constructor that will create a circular reveal from the center point passed in.
     *
     * @param cx The center's x-axis
     * @param cy The center's y-axis
     */
    public CircularRevealChangeHandler(int cx, int cy) {
        this(cx, cy, DEFAULT_ANIMATION_DURATION, true);
    }

    /**
     * Constructor that will create a circular reveal from the center point passed in.
     *
     * @param cx                    The center's x-axis
     * @param cy                    The center's y-axis
     * @param duration              The duration of the animation
     * @param removesFromViewOnPush If true, the view being replaced will be removed from the view hierarchy on pushes
     */
    public CircularRevealChangeHandler(int cx, int cy, long duration, boolean removesFromViewOnPush) {
        super(duration, removesFromViewOnPush);
        mCx = cx;
        mCy = cy;
    }

    /**
     * Constructor that will create a circular reveal from the center point passed in.
     *
     * @param cx       The center's x-axis
     * @param cy       The center's y-axis
     * @param duration The duration of the animation
     */
    public CircularRevealChangeHandler(int cx, int cy, long duration) {
        this(cx, cy, duration, true);
    }

    /**
     * Constructor that will create a circular reveal from the center point passed in.
     *
     * @param cx                    The center's x-axis
     * @param cy                    The center's y-axis
     * @param removesFromViewOnPush If true, the view being replaced will be removed from the view hierarchy on pushes
     */
    public CircularRevealChangeHandler(int cx, int cy, boolean removesFromViewOnPush) {
        this(cx, cy, DEFAULT_ANIMATION_DURATION, removesFromViewOnPush);

    }

    @Override
    public void saveToBundle(@NonNull Bundle bundle) {
        super.saveToBundle(bundle);
        bundle.putInt(KEY_CX, mCx);
        bundle.putInt(KEY_CY, mCy);
    }

    @Override
    public void restoreFromBundle(@NonNull Bundle bundle) {
        super.restoreFromBundle(bundle);
        mCx = bundle.getInt(KEY_CX);
        mCy = bundle.getInt(KEY_CY);
    }

    @Override
    protected Animator getAnimator(@NonNull ViewGroup container, View from, View to, boolean isPush, boolean toAddedToContainer) {
        final float radius = (float) Math.hypot(mCx, mCy);
        Animator animator = null;
        if (isPush && to != null) {
            animator = ViewAnimationUtils.createCircularReveal(to, mCx, mCy, 0, radius);
        } else if (!isPush && from != null) {
            animator = ViewAnimationUtils.createCircularReveal(from, mCx, mCy, radius, 0);
        }
        return animator;
    }

    @Override
    protected void resetFromView(@NonNull View from) {
    }
}
