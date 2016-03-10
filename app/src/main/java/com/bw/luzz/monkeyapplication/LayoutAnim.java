package com.bw.luzz.monkeyapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class LayoutAnim extends AppCompatActivity implements View.OnClickListener{
    Button mAddButton;
    Button mRest;
    GridLayout mGridLayout;
    private LayoutTransition mLayoutTransition;
    private int buttonNumbers=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_anim);
        mAddButton=(Button)findViewById(R.id.layout_animator_addbutton);
        mAddButton.setOnClickListener(this);
        mRest=(Button)findViewById(R.id.layout_animator_resetbutton);
        mRest.setOnClickListener(this);
        mGridLayout=(GridLayout)findViewById(R.id.layout_animator_gridview);
        setupLayoutAnimation();
        mGridLayout.setLayoutTransition(mLayoutTransition);


    }

    private void setupLayoutAnimation() {
        mLayoutTransition = new LayoutTransition();
        mLayoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
        mLayoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
        mLayoutTransition.setDuration(300);
        //设置按钮自己的动画
        Animator apperingAnimator= ObjectAnimator.ofFloat(null, View.ROTATION_Y,90f,0f);
        apperingAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                View view = (View) ((ObjectAnimator) animation).getTarget();
                if (view != null) {
                    view.setRotation(0.0f);
                }

            }
        });
        mLayoutTransition.setAnimator(LayoutTransition.APPEARING,apperingAnimator);
        //设置按钮消失动画
        Animator disapperingAnimator=ObjectAnimator.ofFloat(null,View.ROTATION_X,0.0f,90f);
        disapperingAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                View view = (View) ((ObjectAnimator) animation).getTarget();
                if (view != null) {
                    view.setRotation(0.0f);
                }
            }
        });
        mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, disapperingAnimator);

        PropertyValuesHolder pvhSX=PropertyValuesHolder.ofFloat(View.SCALE_X,1.0f,0.0f,1.0f);
        PropertyValuesHolder pvhSY=PropertyValuesHolder.ofFloat(View.SCALE_Y,1.0f,0.0f,1.0f);
        PropertyValuesHolder pvhLeft =
                PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder pvhTop =
                PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder pvhRight =
                PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder pvhBottom =
                PropertyValuesHolder.ofInt("bottom", 0, 1);
        ObjectAnimator canimator=ObjectAnimator.ofPropertyValuesHolder(this,pvhLeft,pvhBottom,pvhRight,pvhTop,pvhSX, pvhSY);
        mLayoutTransition.setAnimator(LayoutTransition.CHANGE_APPEARING, canimator);
        canimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                View v = (View) ((ObjectAnimator) animation).getTarget();
                v.setScaleX(1.0f);
                v.setScaleY(1.0f);

            }
        });

        /*Keyframe mKeyframeStart = Keyframe.ofFloat(0.0f, 0.0f);
        Keyframe mKeyframeMiddle = Keyframe.ofFloat(0.5f, 180.0f);
        Keyframe mKeyframeEndBefore = Keyframe.ofFloat(0.999f, 360.0f);
        Keyframe mKeyframeEnd = Keyframe.ofFloat(1.0f, 0.0f);

        PropertyValuesHolder pvh= PropertyValuesHolder.ofKeyframe(View.ROTATION, mKeyframeStart, mKeyframeMiddle, mKeyframeEndBefore, mKeyframeEnd);*/
        //ObjectAnimator mObjectAnimatorChangeDisAppearing = ObjectAnimator.ofPropertyValuesHolder(this,pvhLeft,pvhBottom,pvhRight,pvhTop, pvh);
        ObjectAnimator mObjectAnimatorChangeDisAppearing=ObjectAnimator.ofFloat(null, View.ROTATION, 0.0f, 180.0f, 360.0f);
        ObjectAnimator ma=ObjectAnimator.ofInt(null,"top",0,1);
        ObjectAnimator mb=ObjectAnimator.ofInt(null,"left",0,1);
        ObjectAnimator mc=ObjectAnimator.ofInt(null,"right",0,1);
        ObjectAnimator md=ObjectAnimator.ofInt(null,"bottom",0,1);
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(mObjectAnimatorChangeDisAppearing,ma,mb,mc,md);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                View v = (View) ((ObjectAnimator)((AnimatorSet) animation).getChildAnimations().get(0)).getTarget();
                v.setRotation(0.0f);
            }
        });
        mLayoutTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, animatorSet);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_animator_addbutton:
                addButton();
                break;
            case R.id.layout_animator_resetbutton:
                resetButton();
                break;
            default:
                break;
        }
    }
    /**
     * 增加Button
     */
    public void addButton(){
        Button mButton = new Button(this);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(120, 120);
        mButton.setLayoutParams(mLayoutParams);
        mButton.setText(String.valueOf(buttonNumbers++));
        mButton.setTextColor(Color.rgb(0, 0, 0));
        if(buttonNumbers % 2 == 1){
            mButton.setBackgroundColor(Color.rgb(45, 118, 87));
        }else{
            mButton.setBackgroundColor(Color.rgb(225, 24, 0));
        }
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mGridLayout.removeView(v);
            }
        });

        mGridLayout.addView(mButton, Math.min(1, mGridLayout.getChildCount()));
    }

    /**
     * 删除所有的Button,重置GridLayout
     */
    public void resetButton(){
        mGridLayout.removeAllViews();
        buttonNumbers = 1;
    }
}
