package com.newsenglish.gary.myapplication.view;

import android.content.Context;
import android.support.annotation.Px;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.lang.reflect.Field;

/**
 * Created by Gary on 2017/8/25.
 */

public class MyViewPager extends ViewPager {
    private boolean noScroll = true;
    private int defalutSpeed = 1000; //默认的滑动速度为1秒
    FixedSpeedScroller mScroller = null;
    private Context context;
    public MyViewPager(Context context) {
        super(context);
        this.context = context;
        controViewPagerSpeed();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        controViewPagerSpeed();
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    public void setDefalutSpeed(int defalutSpeed) {
        this.defalutSpeed = defalutSpeed;
    }

    @Override
    public void scrollTo(@Px int x, @Px int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (noScroll){
            return  false;
        }else
            return super.onTouchEvent(ev);
    }
    private void controViewPagerSpeed(){
            try {
                Field mField;
                mField = ViewPager.class.getDeclaredField("mScroller");
                mField.setAccessible(true);
                mScroller = new FixedSpeedScroller(context, new AccelerateDecelerateInterpolator());
                mScroller.setmDuration(defalutSpeed);
                mField.set(this, mScroller);
            }catch (Exception e){
                e.printStackTrace();
            }
    }

}
