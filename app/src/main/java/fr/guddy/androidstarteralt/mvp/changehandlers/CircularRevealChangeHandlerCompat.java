package fr.guddy.androidstarteralt.mvp.changehandlers;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

public class CircularRevealChangeHandlerCompat extends CircularRevealChangeHandler {

    public CircularRevealChangeHandlerCompat() {
    }

    public CircularRevealChangeHandlerCompat(@NonNull View fromView, @NonNull View containerView) {
        super(fromView, containerView);
    }

    @Override
    protected Animator getAnimator(@NonNull ViewGroup container, View from, View to, boolean isPush, boolean toAddedToContainer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return super.getAnimator(container, from, to, isPush, toAddedToContainer);
        } else {
            AnimatorSet animator = new AnimatorSet();
            if (to != null && toAddedToContainer) {
                animator.play(ObjectAnimator.ofFloat(to, View.ALPHA, 0, 1));
            }

            if (from != null) {
                animator.play(ObjectAnimator.ofFloat(from, View.ALPHA, 0));
            }

            return animator;
        }
    }
}
