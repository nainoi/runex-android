package com.think.runex.java.Utils.Animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class AnimUtils {
    /**
     * Main variables
     */

    // instance variables
    private static AnimUtils ins;

    // singleton
    private AnimUtils() {
    }

    public static AnimUtils instance() {
        return ins == null ? ins = new AnimUtils() : ins;

    }

    public void alittleTranslateUp(View v, onAnimCallback callback) {
        ValueAnimator va = ValueAnimator.ofInt((v.getHeight() * 10) / 100, 0);
        int mDuration = 1; //in millis
        va.setDuration(mDuration);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationY( (int)animation.getAnimatedValue());
            }
        });
        va.start();
    }
    public void alittleTranslateDown(View v, onAnimCallback callback) {
        ValueAnimator va = ValueAnimator.ofInt(0, (v.getHeight() * 10) / 100);
        int mDuration = 250; //in millis
        va.setDuration(mDuration);
        va.setStartDelay(200);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                v.setTranslationY( (int)animation.getAnimatedValue());
            }
        });
        va.start();
    }
    public void translateDown(View v, onAnimCallback callback) {
        ValueAnimator va = ValueAnimator.ofInt(0, v.getHeight());
        int mDuration = 250; //in millis
        va.setDuration(mDuration);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                // prepare usage variables
                int val = (int)animation.getAnimatedValue();

                // callback
                if( val == 0 ) callback.onStart();

                // update translation y
                v.setTranslationY( val );

                // callback
                if( val >= v.getHeight() ) callback.onEnd();
            }
        });
        va.start();
    }
    public void translateUp(View v, onAnimCallback callback) {
        ValueAnimator va = ValueAnimator.ofInt(v.getHeight(), 0);
        int mDuration = 500; //in millis
        va.setDuration(mDuration);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                // prepare usage variables
                int val = (int)animation.getAnimatedValue();

                // callback
                if( val == 0 ) callback.onStart();

                // update translation y
                v.setTranslationY( val );

                // callback
                if( val >= v.getHeight() ) callback.onEnd();
            }
        });
        va.start();
    }

    public void scaleOut(View v, onAnimCallback callback) {
        float x = v.getX();
        int w = v.getWidth();

        Animation translateAnim = new TranslateAnimation(x - (w * 2), 1f,
                1f, 1f);
        translateAnim.setFillAfter(true); // Needed to keep the result of the animation
        translateAnim.setDuration(250);

        Animation alphaAnim = new AlphaAnimation(0f, 1f);
        alphaAnim.setFillAfter(true);
        alphaAnim.setDuration(150);

        AnimationSet set = new AnimationSet(true);
        set.setDuration(250);
        set.addAnimation(translateAnim);
        set.addAnimation(alphaAnim);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (callback != null) callback.onStart();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (callback != null) callback.onEnd();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        v.startAnimation(set);
    }

    public void fadeOut(View v, long duration, long delay, onAnimCallback callback) {
        v.animate().alpha(0.0f)
                .translationY(-13.0f)
                .setStartDelay(delay)
                .setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (callback != null) callback.onStart();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (callback != null) callback.onEnd();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }

    public void fadeIn(View v, long duration, long delay, onAnimCallback callback) {
        v.animate().alpha(1.0f)
                .translationY(1.0f)
                .setStartDelay(delay)
                .setDuration(duration).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (callback != null) callback.onStart();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (callback != null) callback.onEnd();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        }).start();
    }
}
