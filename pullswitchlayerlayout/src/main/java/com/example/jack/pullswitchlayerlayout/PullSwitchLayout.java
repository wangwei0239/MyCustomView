package com.example.jack.pullswitchlayerlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by Jack on 2016/8/29.
 */
public class PullSwitchLayout extends FrameLayout{

    private ViewGroup scrollDownShowLayer, defaultShowLayer, loadingLayer;
    private int loadingLayerHeight = 0;
    private Scroller scroller;
    private boolean isShowDefaultLayer = true;

    private int pxScrollTimes = 4;

    public PullSwitchLayout(Context context) {
        this(context,null);
    }

    public PullSwitchLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PullSwitchLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() == 3 && getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup && getChildAt(2) instanceof ViewGroup){
            scrollDownShowLayer = (ViewGroup) getChildAt(0);
            loadingLayer = (ViewGroup) getChildAt(1);
            defaultShowLayer = (ViewGroup) getChildAt(2);
        }else {
            throw new InflateException("You can must have 3 subviews, and all of them need to be viewgroup.");
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        loadingLayerHeight = loadingLayer.getMeasuredHeight();
        scrollDownShowLayer.layout(left,top - loadingLayerHeight - bottom, right, top - loadingLayerHeight);
        loadingLayer.layout(left,top - loadingLayerHeight, right, top);
        defaultShowLayer.layout(left, top, right, bottom);
    }


    private int downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = (int) (event.getY() - downY);
                deltaY = deltaY / pxScrollTimes;
                downY = (int) event.getY();
                System.out.println("getScrollY----------------:"+getScrollY());

                int newScrollY = getScrollY() - deltaY;
                scrollTo(0,newScrollY);
                break;
            case MotionEvent.ACTION_UP:
                onActionUp(event);
                break;
        }
        return true;
    }

    public void onActionUp(MotionEvent event){
        int curScroll = -getScrollY();
        System.out.println("Up getScrollY----------------:"+curScroll+"---loadingLayerHeight------:"+loadingLayerHeight/2);
        System.out.println("Up Result----------------"+ (curScroll < (loadingLayerHeight / 2)));

        boolean result = false;

        if(isShowDefaultLayer){
            result = curScroll < (loadingLayerHeight / 2);
        }else {
            result = -getScrollY() < (scrollDownShowLayer.getMeasuredHeight() + loadingLayerHeight / 2);
        }

        if(result){
            showDefaultLayer();
            System.out.println("showDefaultLayer----------------");
        }else {
            showScrollDownLayer();
            System.out.println("showScrollDownLayer----------------");
        }
    }

    public void showDefaultLayer(){
        isShowDefaultLayer = true;
        scroller.startScroll(0,getScrollY(),0,-getScrollY() ,400);
        invalidate();
    }

    public void showScrollDownLayer(){
        isShowDefaultLayer = false;
        scroller.startScroll(0,getScrollY(),0, -(scrollDownShowLayer.getMeasuredHeight() + loadingLayerHeight + getScrollY()) ,400);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset())
        {
            scrollTo(0,scroller.getCurrY());
            invalidate();
        }
    }
}
