package com.example.jack.addviewanim;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    Button btn;
    LinearLayout root;
    LayoutTransition transition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        transition = new LayoutTransition();
        ObjectAnimator anim = ObjectAnimator.ofFloat(null, "", 0F, 90F, 0F);
        anim.setDuration(2000);
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING,anim);
        transition.setAnimator(LayoutTransition.APPEARING,anim);
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,anim);
        transition.setAnimator(LayoutTransition.DISAPPEARING,anim);
        root = (LinearLayout) findViewById(R.id.rootview);
        root.setLayoutTransition(transition);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add, null);



                root.addView(view1);
            }
        });


    }
}
