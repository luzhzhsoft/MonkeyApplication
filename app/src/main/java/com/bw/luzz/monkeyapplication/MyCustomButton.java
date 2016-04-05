package com.bw.luzz.monkeyapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by Luzz on 2016/3/31.
 */
public class MyCustomButton extends Button{

    public MyCustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyCustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCustomButton(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("event","ac"+event.getAction()+" x:"+event.getRawX()+" y:"+event.getRawY());
        return super.onTouchEvent(event);
    }
}
