package com.example.wangwei.musicanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout lyAlbum;
    private ImageView ivAlbum;
    private int left;


    private int downTime;

    private int downX = 0;

    private int downY = 0;

    private boolean firstTouch = false;

    private boolean touchY = false;
    RotateAnimation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anim = new RotateAnimation(0,360,RotateAnimation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(3000);
        anim.setRepeatCount(RotateAnimation.INFINITE);
        anim.setRepeatMode(RotateAnimation.RESTART);
        lyAlbum = (LinearLayout) findViewById(R.id.ly_album);
        ivAlbum = (ImageView) findViewById(R.id.iv_album);
        ivAlbum.post(new Runnable() {
            @Override
            public void run() {
                left = ivAlbum.getLeft();
                int right = lyAlbum.getRight();
                ObjectAnimator.ofFloat(lyAlbum,"translationX",-right,-left, -right , 0).setDuration(1300).start();

                ivAlbum.startAnimation(anim);
            }
        });


        ivAlbum.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) motionEvent.getX();
                        downY = (int) motionEvent.getY();
                        downTime = (int) System.currentTimeMillis();
                        firstTouch = true;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int x = (int) motionEvent.getX();
                        int deltaX = x - downX;

                        int finalX = (int) (lyAlbum.getTranslationX() + deltaX);
                        if(finalX > 0){
                            finalX = 0;
                        }else if(finalX < -left){
                            finalX = -left;
                        }

                        int y = (int) motionEvent.getY();
                        int deltaY = y - downY;

                        int finalY = (int) (lyAlbum.getTranslationY() + deltaY);
                        if(finalY > 0){
                            finalY = 0;
                        }


                        downY = y - deltaY;


                        downX = x - deltaX;

                        float percentAlpha = (float) (300+(lyAlbum.getTranslationY()))/ (float) 300;

                        lyAlbum.setAlpha(percentAlpha);

                        if(firstTouch){
                            firstTouch = false;
                            if(Math.abs(finalX) > Math.abs(finalY)){
                                touchY = false;
                            }else {
                                touchY = true;
                            }
                        }

                        if(touchY){
                            lyAlbum.setTranslationY(finalY);
                            ivAlbum.clearAnimation();
                        }else {
                            lyAlbum.setTranslationX(finalX);
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        int duration = (int) (System.currentTimeMillis() - downTime);
                        System.out.println("upX duration:"+duration);
                        if(duration > 100){
                            int upX = (int) - lyAlbum.getTranslationX();

                            System.out.println("upX:"+upX + " left:"+left);

                            if(upX > left / 2){
                                System.out.println("upX:close");
                                float percent = (float)(left - upX) / (float)left;

                                ObjectAnimator.ofFloat(lyAlbum,"translationX",-left).setDuration((long) (600 * percent)).start();
                            }else {
                                System.out.println("upX:open");
                                float percent = (float)(upX) / (float)left;

                                ObjectAnimator.ofFloat(lyAlbum,"translationX",0).setDuration((long) (600 * percent)).start();
                            }
                        }else {
                            if(lyAlbum.getTranslationX() == 0){
                                System.out.println("upX:close click");
                                ObjectAnimator.ofFloat(lyAlbum,"translationX",-left).setDuration(600).start();
                            }else {
                                System.out.println("upX:open click");
                                ObjectAnimator.ofFloat(lyAlbum,"translationX",0).setDuration(600).start();
                            }
                        }

                        if(lyAlbum.getTranslationY() < -300){
                            lyAlbum.setVisibility(View.GONE);
                        }else {
                            ObjectAnimator.ofFloat(lyAlbum,"translationY",0).setDuration(600).start();
                            ObjectAnimator an = ObjectAnimator.ofFloat(lyAlbum,"alpha",1).setDuration(600);
                            an.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    ivAlbum.startAnimation(anim);
                                }
                            });
                            an.start();

                        }


                        break;
                }

                return false;
            }
        });


    }
}
