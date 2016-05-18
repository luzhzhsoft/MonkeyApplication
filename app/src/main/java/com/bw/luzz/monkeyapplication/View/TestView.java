package com.bw.luzz.monkeyapplication.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Luzz on 2016/5/11.
 */
public class TestView extends View{
    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

    public TestView(Context context) {
        this(context,null,0);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPaint.setColor(Color.parseColor("#ff00ff00"));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w=getWidth();
        int h=getHeight();

        canvas.drawCircle(w/2,h/2,w/2,mPaint);
    }
}
