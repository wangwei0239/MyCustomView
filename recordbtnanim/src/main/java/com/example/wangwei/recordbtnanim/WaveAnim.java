package com.example.wangwei.recordbtnanim;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by Jack on 2016/9/1.
 */
public class WaveAnim extends View{

    private int backgroundRadius = 0;

    private int insideRadiusOffset = 10;

    private int outsideRadiusOffset = 160;

    private int radius = 0;

    private int insideRadius = 0;

    private int outsideRadius = 0;

    private int diameter = 0;

    private Bitmap mSrc = null;

    private int angle = 0;

    private Paint iconCirclePaint, iconPaint;

    public WaveAnim(Context context) {
        this(context,null);
    }

    public WaveAnim(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveAnim(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mSrc = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        backgroundRadius = (int) Math.sqrt(Math.pow(mSrc.getHeight(), 2)+Math.pow(mSrc.getWidth(), 2)) / 2;
        radius = backgroundRadius + insideRadiusOffset + outsideRadiusOffset;
        insideRadius = backgroundRadius + insideRadiusOffset;
        outsideRadius = insideRadius + outsideRadiusOffset;
        diameter = radius * 2;
        iconCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        iconCirclePaint.setColor(Color.BLUE);
        iconCirclePaint.setStyle(Paint.Style.STROKE);
        iconCirclePaint.setStrokeWidth(3);
        iconCirclePaint.setShader(new SweepGradient(radius,radius,Color.TRANSPARENT,Color.RED));
//        iconCirclePaint.setShader(new RadialGradient(radius,radius,insideRadius,new int[]{Color.BLUE,Color.RED},null, Shader.TileMode.REPEAT));

    }

    public void scanningAnim(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,360);
        valueAnimator.setDuration(700);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                angle = (int) valueAnimator.getAnimatedValue();
                invalidate();
                System.out.println("angle:"+angle);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(radius*2, radius*2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mSrc, (float) (radius - (mSrc.getWidth() / 2)),(float) ( radius - (mSrc.getHeight() / 2)),iconPaint);
        canvas.save();
        canvas.rotate(angle,radius,radius);
        canvas.drawCircle(radius, radius, insideRadius, iconCirclePaint);
        canvas.restore();
    }
}
