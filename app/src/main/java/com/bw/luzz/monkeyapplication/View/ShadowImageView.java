package com.bw.luzz.monkeyapplication.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bw.luzz.monkeyapplication.R;

/**
 * TODO: document your custom view class.
 */
public class ShadowImageView extends ImageView {
    private int mShadowColor = Color.RED; // TODO: use a default from R.color...
    private float mShadowRadius = 0; // TODO: use a default from R.dimen...
    private float mShadowX=0;
    private float mShadowY=0;
    private boolean mUseShadow=false;

    public ShadowImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public ShadowImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ShadowImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ShadowImageView, defStyle, 0);

        mShadowColor = a.getColor(
                R.styleable.ShadowImageView_shadowColor,
                mShadowColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mShadowRadius = a.getDimension(
                R.styleable.ShadowImageView_shadowRadius,
                mShadowRadius);
        mShadowX = a.getDimension(
                R.styleable.ShadowImageView_shadowX,
                mShadowRadius);
        mShadowY = a.getDimension(
                R.styleable.ShadowImageView_shadowY,
                mShadowRadius);

        mUseShadow=a.getBoolean(
                R.styleable.ShadowImageView_setShadow,
                mUseShadow);


        a.recycle();




        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {
        Paint paint=((BitmapDrawable)getDrawable()).getPaint();
        if(mUseShadow){
            paint.setShadowLayer(mShadowRadius,mShadowX,mShadowY,mShadowColor);
        }else {
            paint.clearShadowLayer();
        }
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }


}
