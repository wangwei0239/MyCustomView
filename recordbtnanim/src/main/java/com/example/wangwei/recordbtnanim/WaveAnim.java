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

    //----------------on draw parameter--------------------------

    private int backgroundRadius = 0;

    private int insideRadiusOffset = 10;

    private int outsideRadiusOffset = 60;

    private int radius = 0;

    private int insideRadius = 0;

    private int outsideRadius = 0;

    private int diameter = 0;

    private Bitmap mSrc = null;

    private int angle = 0;

    private int waveRadius = 0;

    private float waveAlpha = 1;

    //----------------end of on draw parameter--------------------------



    private Paint iconCirclePaint, iconPaint, wavePaint;

    //----------------animations------------------------------
    private ValueAnimator loadingAnim;
    private ValueAnimator voiceAnim;
    //----------------end of animations----------------------

    //----------------view state--------------------------
    private boolean processing = false;



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
        iconCirclePaint.setColor(Color.RED);
        iconCirclePaint.setStyle(Paint.Style.STROKE);
        iconCirclePaint.setStrokeWidth(3);

        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setColor(Color.BLUE);
        wavePaint.setStyle(Paint.Style.STROKE);
        wavePaint.setStrokeWidth(3);
        iconCirclePaint.setShader(new SweepGradient(radius,radius,Color.TRANSPARENT,Color.RED));
//        iconCirclePaint.setShader(new RadialGradient(radius,radius,insideRadius,new int[]{Color.BLUE,Color.RED},null, Shader.TileMode.REPEAT));
        initScanningAnim();
        initVoiceAnim();

    }

    public void initVoiceAnim(){
        voiceAnim = ValueAnimator.ofInt(insideRadius,outsideRadius);
        voiceAnim.setDuration(1000);
        voiceAnim.setRepeatCount(ValueAnimator.INFINITE);
        voiceAnim.setRepeatMode(ValueAnimator.RESTART);
        voiceAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                waveRadius = (int) valueAnimator.getAnimatedValue();
                waveAlpha = 1- valueAnimator.getAnimatedFraction();
                wavePaint.setAlpha((int) (255 * waveAlpha));
                invalidate();
            }
        });
    }

    public void startVoiceAnim(){
        voiceAnim.start();
    }

    public void initScanningAnim(){
        loadingAnim = ValueAnimator.ofInt(0,360);
        loadingAnim.setDuration(1400);
        loadingAnim.setRepeatMode(ValueAnimator.RESTART);
        loadingAnim.setRepeatCount(ValueAnimator.INFINITE);
        loadingAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                angle = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        loadingAnim.setInterpolator(new LinearInterpolator());
    }

    public void scanningAnim(){
        if(processing){
            loadingAnim.end();
        }else {
            loadingAnim.start();
        }
        processing = !processing;
    }

    public void stopScanningAnim(){
        loadingAnim.end();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(radius*2, radius*2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mSrc, (float) (radius - (mSrc.getWidth() / 2)),(float) ( radius - (mSrc.getHeight() / 2)),iconPaint);
        canvas.drawCircle(radius,radius,waveRadius,wavePaint);
        canvas.save();
        canvas.rotate(angle,radius,radius);
        canvas.drawCircle(radius, radius, insideRadius, iconCirclePaint);
        canvas.restore();
    }
}
