package com.example.jack.addviewanim;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

/**
 * Created by Jack on 2016/9/8.
 */
public class MyText extends TextView{
    public MyText(Context context) {
        super(context);
    }

    public MyText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(0,1,0,1);
        TranslateAnimation ta = new TranslateAnimation(-200,100,-200,100);
//        set.addAnimation(sa);
        set.addAnimation(ta);
        set.setDuration(2000);
        this.startAnimation(set);
    }
}
