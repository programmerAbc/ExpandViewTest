package com.example.user1.expandviewtest;

import android.animation.Animator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * Created by user1 on 2016/8/7.
 */
public class MyExpandView extends FrameLayout {
    View rootView = null;
    Button btn = null;
    ViewGroup rootLayout = null;
    View subView = null;
    boolean subViewAdded = false;
    LayoutTransition layoutTransition;

    public MyExpandView(Context context) {
        super(context);
        initUI();
    }

    public MyExpandView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initUI();
    }

    public MyExpandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initUI();
    }

    private void initUI() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.base_layout, this, true);
        subView = LayoutInflater.from(getContext()).inflate(R.layout.sub_layout, this, false);
        rootLayout = (ViewGroup) rootView.findViewById(R.id.rootLayout);
        btn = (Button) rootView.findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (subViewAdded == false) {
                    addViewV2(rootLayout, subView);
                    subViewAdded = true;
                } else {
                    remoteViewV2(rootLayout, subView);
                    subViewAdded = false;
                }
            }
        });


    }

    private void initLayoutAnimator() {
        layoutTransition = new LayoutTransition();
        ObjectAnimator appearingAnimator = ObjectAnimator.ofFloat(null, "rotationX", 90.0f, 0.0f).setDuration(300);
        layoutTransition.setAnimator(LayoutTransition.APPEARING, appearingAnimator);
        ObjectAnimator disappearingAnimator = ObjectAnimator.ofFloat(null, "rotationX", 0.0f, 90.0f).setDuration(300);
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, disappearingAnimator);
        rootLayout.setLayoutTransition(layoutTransition);
    }

    public void addViewV2(final ViewGroup viewGroup, final View view) {
        int viewGroupHeight = viewGroup.getHeight();
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int viewHeight = view.getMeasuredHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(viewGroupHeight, viewGroupHeight + viewHeight).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                viewGroup.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                viewGroup.requestLayout();
            }
        });
        //valueAnimator.setInterpolator(new AnticipateOvershootInterpolator());
        final ObjectAnimator viewAnimator = ObjectAnimator.ofFloat(view, "rotationX", 90, 0).setDuration(200);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewGroup.addView(view);
                viewAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    public void remoteViewV2(final ViewGroup viewGroup, final View view) {
        int viewGroupHeight = viewGroup.getHeight();
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int viewHeight = view.getMeasuredHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(viewGroupHeight, viewGroupHeight - viewHeight).setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                viewGroup.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                viewGroup.requestLayout();
            }
        });
        final ObjectAnimator viewAnimator = ObjectAnimator.ofFloat(view, "rotationX", 0, 90).setDuration(200);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                viewAnimator.start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewGroup.removeView(view);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }
}
